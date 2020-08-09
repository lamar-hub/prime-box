package com.lamar.primebox.notification.model;

import com.lamar.primebox.notification.constant.PrimeBoxEmailConstants;
import lombok.Getter;

@Getter
public enum NotificationType {

    EMAIL_VERIFICATION(PrimeBoxEmailConstants.Views.VERIFICATION_TEMPLATE, PrimeBoxEmailConstants.Subject.VERIFICATION_TEMPLATE),
    EMAIL_ALERT(PrimeBoxEmailConstants.Views.ALERT_TEMPLATE, PrimeBoxEmailConstants.Subject.ALERT_TEMPLATE),
    EMAIL_SHARED_FILE(PrimeBoxEmailConstants.Views.SHARED_FILE_TEMPLATE, PrimeBoxEmailConstants.Subject.SHARED_FILE_TEMPLATE),
    EMAIL_ACTIVATION(PrimeBoxEmailConstants.Views.ACTIVATION_TEMPLATE, PrimeBoxEmailConstants.Subject.ACTIVATION_TEMPLATE),
    SMS_MESSAGE("", "");

    private final String view;
    private final String subject;

    NotificationType(String view, String subject) {
        this.view = view;
        this.subject = subject;
    }

}
