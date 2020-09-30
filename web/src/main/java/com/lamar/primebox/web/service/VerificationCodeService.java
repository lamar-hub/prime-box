package com.lamar.primebox.web.service;

import com.lamar.primebox.web.dto.model.VerificationCodeDto;

public interface VerificationCodeService {

    boolean checkVerificationCode(String email, String verificationCode);

    VerificationCodeDto createVerificationCode(String email) throws Exception;

}
