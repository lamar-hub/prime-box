package com.lamar.primebox.sms.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Component
public class SenderTemplate {

    private String phoneNumber;
    private String message;

    public SenderTemplate(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

}
