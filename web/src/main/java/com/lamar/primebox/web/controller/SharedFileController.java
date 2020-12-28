package com.lamar.primebox.web.controller;

import com.lamar.primebox.notification.dto.SendNotificationDto;
import com.lamar.primebox.notification.event.NotificationEvent;
import com.lamar.primebox.notification.manager.NotificationManager;
import com.lamar.primebox.notification.model.NotificationType;
import com.lamar.primebox.web.dto.model.SharedFileDto;
import com.lamar.primebox.web.dto.model.SharedFileShareDto;
import com.lamar.primebox.web.dto.model.SharedFileUnshareDto;
import com.lamar.primebox.web.dto.request.SharedFileShareRequest;
import com.lamar.primebox.web.dto.response.SharedFileShareResponse;
import com.lamar.primebox.web.dto.response.SharedFileUnshareResponse;
import com.lamar.primebox.web.dto.response.SharedGetAllResponse;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.service.SharedFileService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/shared-files")
@Slf4j
public class SharedFileController {

    private final SharedFileService sharedService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public SharedFileController(SharedFileService sharedService,
                                @Qualifier("webModelMapper") ModelMapper modelMapper,
                                ApplicationEventPublisher applicationEventPublisher) {
        this.sharedService = sharedService;
        this.modelMapper = modelMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping
    public ResponseEntity<?> getAllUserSharedFiles() {
        final List<SharedFileDto> sharedFileDtoList = sharedService.getAllUserSharedFiles(getUsernameFromSecurityContext());
        final SharedGetAllResponse sharedGetAllResponse = new SharedGetAllResponse().setSharedFiles(sharedFileDtoList);

        log.info(sharedGetAllResponse.toString());
        return ResponseEntity.ok(sharedGetAllResponse);
    }

    @PostMapping
    public ResponseEntity<?> shareFile(@RequestBody @Valid SharedFileShareRequest sharedFileShareRequest) throws Exception {
        final SharedFileShareDto sharedFileShareDto = modelMapper.map(sharedFileShareRequest, SharedFileShareDto.class);
        final SharedFileDto sharedFileDto = sharedService.share(sharedFileShareDto);

        final SendNotificationDto sendNotificationDto = buildShareFileNotification(sharedFileDto);
        
        dispatchNotification(sendNotificationDto);

        final SharedFileShareResponse shareResponse = modelMapper.map(sharedFileDto, SharedFileShareResponse.class);

        log.info(shareResponse.toString());
        return ResponseEntity.ok(shareResponse);
    }

    private void dispatchNotification(SendNotificationDto notificationDto) {
        final NotificationEvent notificationEvent = new NotificationEvent(this, notificationDto);

        applicationEventPublisher.publishEvent(notificationEvent);
    }

    private SendNotificationDto buildShareFileNotification(SharedFileDto sharedFileDto) {
        final Map<String, String> templateModel = new HashMap<>();

        templateModel.put("name", sharedFileDto.getSharedFileUserName());
        templateModel.put("surname", sharedFileDto.getSharedFileUserSurname());
        templateModel.put("email", getUsernameFromSecurityContext());
        templateModel.put("filename", sharedFileDto.getSharedFileFilename());
        return SendNotificationDto
                .builder()
                .notificationTo(sharedFileDto.getSharedFileUserEmail())
                .notificationType(NotificationType.EMAIL_SHARED_FILE)
                .templateModel(templateModel)
                .build();
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> unshareFile(@PathVariable @NotBlank String fileId) throws Exception {
        final SharedFileDto sharedFileDto = sharedService.unshare(new SharedFileUnshareDto().setFileId(fileId).setUsername(getUsernameFromSecurityContext()));
        final SharedFileUnshareResponse sharedFileUnshareResponse = modelMapper.map(sharedFileDto, SharedFileUnshareResponse.class);

        log.info(sharedFileUnshareResponse.toString());
        return ResponseEntity.ok(sharedFileUnshareResponse);
    }

    private String getUsernameFromSecurityContext() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((User) authentication.getPrincipal()).getEmail();
    }

}
