package com.lamar.primebox.web.controller;

import com.lamar.primebox.web.dto.model.FileDownloadDto;
import com.lamar.primebox.web.dto.model.FileDto;
import com.lamar.primebox.web.dto.model.FileSaveDeleteDto;
import com.lamar.primebox.web.dto.model.FileUpdateDto;
import com.lamar.primebox.web.dto.request.FileUpdateRequest;
import com.lamar.primebox.web.dto.response.FileDeleteResponse;
import com.lamar.primebox.web.dto.response.FileGetAllResponse;
import com.lamar.primebox.web.dto.response.FileSaveResponse;
import com.lamar.primebox.web.dto.response.FileUpdateResponse;
import com.lamar.primebox.web.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@CrossOrigin(origins = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/files")
@Slf4j
public class FileController {

    private final FileService fileService;
    private final ModelMapper modelMapper;

    public FileController(FileService fileService, ModelMapper modelMapper) {
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUserFiles() {
        final List<FileDto> fileDtoList = fileService.getAllUserFiles(getUsernameFromSecurityContext());
        final FileGetAllResponse getAllResponse = new FileGetAllResponse().setFiles(fileDtoList);
        return ResponseEntity.ok(getAllResponse);
    }

    @PostMapping("")
    public ResponseEntity<?> saveFile(@RequestParam("file") @NotNull MultipartFile multipartFile) throws Exception {
        final FileSaveDeleteDto fileSaveDeleteDto = fileService.saveFileDatabase(multipartFile, getUsernameFromSecurityContext());
        final FileSaveResponse saveResponse = modelMapper.map(fileSaveDeleteDto, FileSaveResponse.class);
        return ResponseEntity.ok(saveResponse);
    }

    @PutMapping("")
    public ResponseEntity<?> updateFilename(@RequestBody @Valid FileUpdateRequest fileUpdateRequest) throws Exception {
        final FileUpdateDto fileUpdateDto = modelMapper.map(fileUpdateRequest, FileUpdateDto.class);
        final FileDto fileDto = fileService.updateFile(fileUpdateDto);
        final FileUpdateResponse fileUpdateResponse = modelMapper.map(fileDto, FileUpdateResponse.class);
        return ResponseEntity.ok(fileUpdateResponse);
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable @NotBlank String fileId) throws Exception {
        final FileDownloadDto fileDownloadDto = fileService.getFile(fileId);
        final ByteArrayResource resource = new ByteArrayResource(fileDownloadDto.getFile());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileDownloadDto.getFilename() + "\"")
                .contentLength(resource.contentLength())
                .lastModified(fileDownloadDto.getLastModified())
                .contentType(MediaType.parseMediaType(fileDownloadDto.getType()))
                .body(resource);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable @NotBlank String fileId) throws Exception {
        final FileSaveDeleteDto fileSaveDeleteDto = fileService.deleteFile(fileId);
        final FileDeleteResponse fileDeleteResponse = modelMapper.map(fileSaveDeleteDto, FileDeleteResponse.class);
        return ResponseEntity.ok(fileDeleteResponse);
    }

    private String getUsernameFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

}
