package com.canyoncompanion.canyon_api.util.mappers;

import com.canyoncompanion.canyon_api.dtos.requests.UserRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.RolesDTO;
import com.canyoncompanion.canyon_api.dtos.responses.UserResponseDTO;
import com.canyoncompanion.canyon_api.model.entities.RoleEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponseDTO toUserResponseDTO(UserEntity userEntity);
    UserEntity toUserEntity(UserRequestDTO userRequestDTO);
    //Roles mapper
    RolesDTO toRolesDTO(RoleEntity roleEntity);
    RoleEntity toRoleEntity(RolesDTO rolesDTO);

}
