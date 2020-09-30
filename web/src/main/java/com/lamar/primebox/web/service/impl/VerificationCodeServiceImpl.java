package com.lamar.primebox.web.service.impl;

import com.lamar.primebox.web.dto.model.VerificationCodeDto;
import com.lamar.primebox.web.model.User;
import com.lamar.primebox.web.model.VerificationCode;
import com.lamar.primebox.web.repo.UserDao;
import com.lamar.primebox.web.repo.VerificationCodeDao;
import com.lamar.primebox.web.service.VerificationCodeService;
import com.lamar.primebox.web.util.StorageUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final UserDao userDao;
    private final VerificationCodeDao verificationCodeDao;
    private final ModelMapper modelMapper;

    public VerificationCodeServiceImpl(UserDao userDao, VerificationCodeDao verificationCodeDao, ModelMapper modelMapper) {
        this.userDao = userDao;
        this.verificationCodeDao = verificationCodeDao;
        this.modelMapper = modelMapper;
    }


    @Override
    public boolean checkVerificationCode(String email, String verificationCode) {
        final List<VerificationCode> verificationCodes = verificationCodeDao.getVerificationCodeByEmail(email);

        return verificationCodes.stream()
                                .filter(code -> code.isActive() && code.getCreated() + 3 * 60 * 1000 > new Date().getTime())
                                .anyMatch(code -> code.getCode().equals(verificationCode));
    }

    @Override
    public VerificationCodeDto createVerificationCode(String email) throws Exception {
        final User user = userDao.getByEmail(email);

        if (user == null) {
            log.error("user not found");
            throw new Exception("user not found");
        }

        final VerificationCode verificationCode = VerificationCode.builder()
                                                                  .code(StorageUtil.randomCode())
                                                                  .active(true)
                                                                  .created(new Date().getTime())
                                                                  .user(user)
                                                                  .build();

        verificationCodeDao.saveVerificationCode(verificationCode);
        return modelMapper.map(verificationCode, VerificationCodeDto.class);
    }
}
