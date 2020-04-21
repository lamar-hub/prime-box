package com.lamar.primebox.web.file.controller;

import com.lamar.primebox.web.file.dto.FileDTO;
import com.lamar.primebox.web.file.entity.File;
import com.lamar.primebox.web.file.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;
    private final ModelMapper modelMapper;

    public FileController(FileService fileService, ModelMapper modelMapper) {
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUserFiles(@RequestHeader(name = "Authorization") String token) {
        String userID = this.fileService.getUserIDFromToken(token);
        List<File> files = fileService.getAllUserFiles(userID);
        List<FileDTO> filesDTO = files.stream().map(file -> modelMapper.map(file, FileDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(filesDTO);
    }

    @PostMapping("")
    public ResponseEntity<?> saveFile(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam("file") MultipartFile multipartFile) throws Exception {
        String userID = this.fileService.getUserIDFromToken(token);
        File file = fileService.saveFileDatabase(multipartFile, userID);
        fileService.saveFileDisk(multipartFile, file);
        FileDTO fileDTO = modelMapper.map(file, FileDTO.class);
        return ResponseEntity.ok(fileDTO);
    }

    @PutMapping("")
    public ResponseEntity<?> updateFilename(@RequestBody FileDTO fileDTO) {
        File file = modelMapper.map(fileDTO, File.class);
        fileService.updateFile(file);
        fileDTO = modelMapper.map(file, FileDTO.class);
        return ResponseEntity.ok(fileDTO);
    }

    @GetMapping("/{fileID}/download")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable String fileID) throws IOException {
        Pair<File, ByteArrayResource> pair = fileService.getFile(fileID);
        MediaType mediaType = MediaType.parseMediaType(pair.getFirst().getType());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + pair.getFirst().getFilename() + "\"")
                .contentLength(pair.getSecond().contentLength())
                .lastModified(pair.getFirst().getLastModified())
                .contentType(mediaType)
                .body(pair.getSecond());
    }

    @DeleteMapping("/{fileID}")
    public ResponseEntity<?> deleteFile(@PathVariable String fileID) throws Exception {
        File file = fileService.deleteFileDatabase(fileID);
        fileService.deleteFileDisk(file);
        return ResponseEntity.ok().build();
    }

}
