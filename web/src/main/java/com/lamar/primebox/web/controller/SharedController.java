package com.lamar.primebox.web.controller;

import com.lamar.primebox.web.dto.model.SharedDto;
import com.lamar.primebox.web.dto.model.SharedShareDto;
import com.lamar.primebox.web.dto.model.SharedUnshareDto;
import com.lamar.primebox.web.dto.request.SharedShareRequest;
import com.lamar.primebox.web.dto.response.SharedGetAllResponse;
import com.lamar.primebox.web.dto.response.SharedShareResponse;
import com.lamar.primebox.web.dto.response.SharedUnshareResponse;
import com.lamar.primebox.web.model.Shared;
import com.lamar.primebox.web.service.SharedService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/shareds")
public class SharedController {

    private final SharedService sharedService;
    private final ModelMapper modelMapper;

    public SharedController(SharedService sharedService, ModelMapper modelMapper) {
        this.sharedService = sharedService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUserShareds() {
        List<SharedDto> sharedDtoList = sharedService.getAllUserSharedFiles(getUsernameFromSecurityContext());
        SharedGetAllResponse sharedGetAllResponse = modelMapper.map(sharedDtoList, SharedGetAllResponse.class);
        return ResponseEntity.ok(sharedGetAllResponse);
    }

    @PostMapping("")
    public ResponseEntity<?> shareFile(@RequestBody SharedShareRequest sharedShareRequest) throws Exception {
        SharedShareDto sharedShareDto = modelMapper.map(sharedShareRequest, SharedShareDto.class);
        SharedDto sharedDto = sharedService.share(sharedShareDto);
        SharedShareResponse shareResponse = modelMapper.map(sharedDto, SharedShareResponse.class);
        return ResponseEntity.ok(shareResponse);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> unshareFile(@PathVariable String fileId) throws Exception {
        SharedDto sharedDto = sharedService.unshare(new SharedUnshareDto().setFileId(fileId).setUsername(getUsernameFromSecurityContext()));
        SharedUnshareResponse sharedUnshareResponse = modelMapper.map(sharedDto, SharedUnshareResponse.class);
        return ResponseEntity.ok(sharedUnshareResponse);
    }

    private String getUsernameFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

}
