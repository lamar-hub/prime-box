package com.lamar.primebox.web.controller;

import com.lamar.primebox.notification.dto.SendNotificationDto;
import com.lamar.primebox.notification.manager.NotificationManager;
import com.lamar.primebox.notification.model.NotificationType;
import com.lamar.primebox.web.dto.model.FileDownloadDto;
import com.lamar.primebox.web.dto.model.FileDto;
import com.lamar.primebox.web.dto.model.FileSaveDeleteDto;
import com.lamar.primebox.web.dto.model.FileUpdateDto;
import com.lamar.primebox.web.dto.request.FileUpdateRequest;
import com.lamar.primebox.web.dto.response.FileDeleteResponse;
import com.lamar.primebox.web.dto.response.FileGetAllResponse;
import com.lamar.primebox.web.dto.response.FileSaveResponse;
import com.lamar.primebox.web.dto.response.FileUpdateResponse;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.model.UserCredentials;
import com.lamar.primebox.web.service.FileService;
import com.lamar.primebox.web.util.StorageProperties;
import com.lamar.primebox.web.util.StorageUtil;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/files")
@Slf4j
public class FileController {

    private final FileService fileService;
    private final NotificationManager notificationManager;
    private final ModelMapper modelMapper;
    private final StorageProperties storageProperties;

    public FileController(FileService fileService, NotificationManager notificationManager, ModelMapper modelMapper, StorageProperties storageProperties) {
        this.fileService = fileService;
        this.notificationManager = notificationManager;
        this.modelMapper = modelMapper;
        this.storageProperties = storageProperties;
    }

    @GetMapping
    public ResponseEntity<?> getAllUserFiles() {
        final List<FileDto> fileDtoList = fileService.getAllUserFiles(getUsernameFromSecurityContext());
        final FileGetAllResponse getAllResponse = new FileGetAllResponse().setFiles(fileDtoList);

        log.info(getAllResponse.toString());
        return ResponseEntity.ok(getAllResponse);
    }

    @PostMapping
    public ResponseEntity<?> saveFile(@RequestParam("file") @NotNull MultipartFile multipartFile) throws Exception {
        final FileSaveDeleteDto fileSaveDeleteDto = fileService.saveFile(multipartFile, getUsernameFromSecurityContext());

        try {
            StorageUtil.saveFileToDisk(multipartFile, fileSaveDeleteDto.getFileId(), storageProperties.getPath());
        } catch (IOException ioException) {
            log.error("file not saved to disk", ioException);
            fileService.deleteFile(fileSaveDeleteDto.getFileId());
        }

        if (fileSaveDeleteDto.isSendNotification()) {
            final SendNotificationDto sendNotificationDto = buildAlertNotification(fileSaveDeleteDto);
            try {
                notificationManager.queueNotification(sendNotificationDto);
            } catch (Exception exception) {
                log.error("notification error", exception);
            }
        }

        final FileSaveResponse saveResponse = modelMapper.map(fileSaveDeleteDto, FileSaveResponse.class);

        log.info(saveResponse.toString());
        return ResponseEntity.ok(saveResponse);
    }

    private SendNotificationDto buildAlertNotification(FileSaveDeleteDto fileSaveDeleteDto) {
        final Map<String, String> templateModel = new HashMap<>();

        templateModel.put("used", "" + "50");
        return SendNotificationDto
                .builder()
                .notificationTo(getUsernameFromSecurityContext())
                .notificationType(NotificationType.EMAIL_ALERT)
                .templateModel(templateModel)
                .build();
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable @NotBlank String fileId) throws Exception {
        final FileDownloadDto fileDownloadDto = fileService.getFile(fileId);
        final ByteArrayResource resource = new ByteArrayResource(fileDownloadDto.getFile());

        log.info(fileDownloadDto.toString());
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

        try {
            StorageUtil.deleteFileFromDisk(fileSaveDeleteDto.getFileId(), storageProperties.getPath());
        } catch (IOException ioException) {
            log.error("file not deleted from disk", ioException);
        }

        final FileDeleteResponse fileDeleteResponse = modelMapper.map(fileSaveDeleteDto, FileDeleteResponse.class);

        log.info(fileDeleteResponse.toString());
        return ResponseEntity.ok(fileDeleteResponse);
    }

    private String getUsernameFromSecurityContext() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserCredentials userCredentials = (UserCredentials) authentication.getPrincipal();

        return userCredentials.getUsername();
    }

}
