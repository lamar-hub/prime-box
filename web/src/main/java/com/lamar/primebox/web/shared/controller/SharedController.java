package com.lamar.primebox.web.shared.controller;

import com.lamar.primebox.web.shared.dto.SharedDTO;
import com.lamar.primebox.web.shared.entity.Shared;
import com.lamar.primebox.web.shared.service.SharedService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping()
    public ResponseEntity<?> getAllUserShareds(@RequestHeader(name = "Authorization") String token) {
        String userID = this.sharedService.getUserIDFromToken(token);
        List<Shared> shareds = sharedService.getAllUserSharedFiles(userID);
        List<SharedDTO> sharedsDTO = shareds.stream().map(shared -> modelMapper.map(shared, SharedDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(sharedsDTO);
    }

    @PostMapping("")
    public ResponseEntity<?> shareFile(@RequestBody SharedDTO sharedDTO) {
        Shared shared = modelMapper.map(sharedDTO, Shared.class);
        sharedService.share(shared);
        sharedDTO = modelMapper.map(shared, SharedDTO.class);
        return ResponseEntity.ok(sharedDTO);
    }

    @DeleteMapping("/{fileID}")
    public ResponseEntity<?> unshareFile(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable String fileID) throws Exception {
        String userID = this.sharedService.getUserIDFromToken(token);
        sharedService.unshare(userID, fileID);
        return ResponseEntity.ok().build();
    }

}
