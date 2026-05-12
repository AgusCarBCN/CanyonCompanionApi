package com.canyoncompanion.canyon_api.service.user;

import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.enums.UserStatus;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import com.canyoncompanion.canyon_api.util.mappers.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UserAccountSettingServiceImpl implements UserAccountSettingService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void activateAccount(String email) {
        var user=userRepository.findByEmail(email).orElseThrow(this::userNotFound);
        user.activateUser();
    }
    @Override
    public void deactivateAccount(String email,
                                  String reason) {
        var user=userRepository.findByEmail(email).orElseThrow(this::userNotFound);
        user.deactivateUser(reason);
    }


    private BusinessException userNotFound() {
        return new BusinessException(
                ErrorCode.USER_NOT_FOUND.name(),
                ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                HttpStatus.NOT_FOUND);
    }

}
