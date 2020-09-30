package com.lamar.primebox.web.repo;

import com.lamar.primebox.web.model.VerificationCode;

import java.util.List;

public interface VerificationCodeDao {

    List<VerificationCode> getVerificationCodeByEmail(String email);

    void saveVerificationCode(VerificationCode verificationCode);

}
