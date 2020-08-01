package com.lamar.primebox.email.constant;

public class PrimeBoxEmailConstants {

    public static final String EMAIL_FROM = "test@test.com";
    public static final String ENDPOINT = "mail/send";


    public static final class Views {
        public static final String ALERT_TEMPLATE = "alert-email.html";
        public static final String SHARED_FILE_TEMPLATE = "shard-file-email.html";
        public static final String VERIFICATION_TEMPLATE = "verification-email.html";
        public static final String ACTIVATION_TEMPLATE = "activation-email.html";
    }

    public static final class Subject {
        public static final String ALERT_TEMPLATE = "ALERT_TEMPLATE";
        public static final String SHARED_FILE_TEMPLATE = "SHARED_FILE_TEMPLATE";
        public static final String VERIFICATION_TEMPLATE = "VERIFICATION_TEMPLATE";
        public static final String ACTIVATION_TEMPLATE = "ACTIVATION_TEMPLATE";
    }

}
