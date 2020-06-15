package com.lamar.primebox.web.controller;

import com.lamar.primebox.web.dto.model.SharedFileDto;
import com.lamar.primebox.web.dto.model.SharedFileShareDto;
import com.lamar.primebox.web.dto.model.SharedFileUnshareDto;
import com.lamar.primebox.web.dto.request.SharedFileShareRequest;
import com.lamar.primebox.web.dto.response.SharedFileShareResponse;
import com.lamar.primebox.web.dto.response.SharedFileUnshareResponse;
import com.lamar.primebox.web.dto.response.SharedGetAllResponse;
import com.lamar.primebox.web.service.SharedFileService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/shared-files")
@Slf4j
public class SharedFileController {

    private final SharedFileService sharedService;
    private final ModelMapper modelMapper;

    public SharedFileController(SharedFileService sharedService, ModelMapper modelMapper) {
        this.sharedService = sharedService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUserSharedFiles() {
        List<SharedFileDto> sharedFileDtoList = sharedService.getAllUserSharedFiles(getUsernameFromSecurityContext());
        SharedGetAllResponse sharedGetAllResponse = modelMapper.map(sharedFileDtoList, SharedGetAllResponse.class);
        return ResponseEntity.ok(sharedGetAllResponse);
    }

    @PostMapping("")
    public ResponseEntity<?> shareFile(@RequestBody @Valid SharedFileShareRequest sharedFileShareRequest) throws Exception {
        SharedFileShareDto sharedFileShareDto = modelMapper.map(sharedFileShareRequest, SharedFileShareDto.class);
        SharedFileDto sharedFileDto = sharedService.share(sharedFileShareDto);
        SharedFileShareResponse shareResponse = modelMapper.map(sharedFileDto, SharedFileShareResponse.class);
        return ResponseEntity.ok(shareResponse);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> unshareFile(@PathVariable @NotBlank String fileId) throws Exception {
        SharedFileDto sharedFileDto = sharedService.unshare(new SharedFileUnshareDto().setFileId(fileId).setUsername(getUsernameFromSecurityContext()));
        SharedFileUnshareResponse sharedFileUnshareResponse = modelMapper.map(sharedFileDto, SharedFileUnshareResponse.class);
        return ResponseEntity.ok(sharedFileUnshareResponse);
    }

    private String getUsernameFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

}
