package com.canyoncompanion.canyon_api.service.user;

import com.canyoncompanion.canyon_api.dtos.responses.PageResponse;
import com.canyoncompanion.canyon_api.dtos.responses.UserResponseDTO;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.enums.Roles;
import com.canyoncompanion.canyon_api.model.enums.UserStatus;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import com.canyoncompanion.canyon_api.util.helpers.Sort;
import com.canyoncompanion.canyon_api.util.mappers.PageResponseMapper;
import com.canyoncompanion.canyon_api.util.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor

public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserResponseDTO getUserById(Long userId) {
        var userEntity=userRepository
                .findById(userId)
                .orElseThrow(this::userNotFound);
        return userMapper.toUserResponseDTO(userEntity);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        var userEntity=userRepository
                .findByEmail(email)
                .orElseThrow(this::userNotFound);
        return userMapper.toUserResponseDTO(userEntity);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        var userEntity=userRepository
                .findByNameIgnoreCase(username)
                .orElseThrow(this::userNotFound);
        return userMapper.toUserResponseDTO(userEntity);
    }

    @Override
    public PageResponse<UserResponseDTO> getUsersByRole(String field, Boolean desc, Integer numberOfPages, Roles role) {
        final var sorting= Sort.getDescentSort(field,desc);
        var page=userRepository.findAllByRoles_Role(role,
                        PageRequest.of(numberOfPages, Sort.PAGE_SIZE, sorting))
                .map(userMapper::toUserResponseDTO);
        return PageResponseMapper.mapToPageResponse(page);
    }

    @Override
    public PageResponse<UserResponseDTO> getUsersByStatus(String field, Boolean desc, Integer numberOfPages, UserStatus status) {
        final var sorting= Sort.getDescentSort(field,desc);
        var page=userRepository.findAllByStatus(status,
                        PageRequest.of(numberOfPages, Sort.PAGE_SIZE, sorting))
                .map(userMapper::toUserResponseDTO);
        return PageResponseMapper.mapToPageResponse(page);
    }

    @Override
    public PageResponse<UserResponseDTO> getActiveUsers(String field, Boolean desc, Integer numberOfPages) {
        final var sorting= Sort.getDescentSort(field,desc);
        var page=userRepository.findAllActive(
                PageRequest.of(numberOfPages, Sort.PAGE_SIZE, sorting))
                .map(userMapper::toUserResponseDTO);
        return PageResponseMapper.mapToPageResponse(page);
    }



    @Override
    public long countAllUsers() {
        return userRepository.count();
    }

    @Override
    public long countUsersByStatus(UserStatus status) {
        return userRepository.countByStatus(status);
    }

    @Override
    public String getUserProfileImage(Long userId) {
        return "";
    }

    private BusinessException userNotFound() {
        return new BusinessException(
                ErrorCode.USER_NOT_FOUND.name(),
                ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                HttpStatus.NOT_FOUND);
    }
}
