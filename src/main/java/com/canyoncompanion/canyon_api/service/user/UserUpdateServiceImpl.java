package com.canyoncompanion.canyon_api.service.user;

import com.canyoncompanion.canyon_api.dtos.requests.UpdateUserRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.UserResponseDTO;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import com.canyoncompanion.canyon_api.util.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class UserUpdateServiceImpl implements UserUpdateService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;



    @Override
    public String updateProfileImage(Long userId, byte[] imageData, String imageType) {
        return "";
    }


    @Override

    public UserResponseDTO updateUserFields(String email, UpdateUserRequestDTO request) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND.name(),
                        ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                        HttpStatus.NOT_FOUND));

        if (request.getName() != null) user.setUsername(request.getName());
        if (request.getSurname() != null) user.setSurname(request.getSurname());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null) user.setPassword(request.getPassword());
      //  if (request.getProfileImage() != null) user.se(request.getProfileImage());

        user.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserResponseDTO(user);
    }
}
