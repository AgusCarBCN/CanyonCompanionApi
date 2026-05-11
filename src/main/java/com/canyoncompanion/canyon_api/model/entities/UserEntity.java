package com.canyoncompanion.canyon_api.model.entities;


import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.enums.Roles;
import com.canyoncompanion.canyon_api.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Slf4j
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "users_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 200)
    private String surname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    private String statusDescription;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    // ---------------------------
    // Roles (ManyToMany)
    // ---------------------------


    @ManyToMany(fetch = FetchType.EAGER)
    @Builder.Default
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();
    // ---------------------------
    // Lifecycle callbacks
    // ---------------------------
    @PrePersist
    protected void onCreate() {
        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime nowTime = LocalDateTime.now();
        createdAt = nowDate;
        updatedAt = nowTime;

        // Default status = ACTIVE
        status = UserStatus.ACTIVE;
        statusDescription = "User active";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PostUpdate
    protected void afterUpdate() {
        log.info("User status updated: {}", status);
    }

    // ---------------------------
    // Utility methods
    // ---------------------------

    public void addRolesToUser(boolean isAdmin){
        Set<RoleEntity>userRoles = new HashSet<>();
        if(isAdmin){
            var adminRol=(RoleEntity.builder()
                    .id(2L)
                    .role(Roles.ROLE_ADMIN)
                    .build());
            userRoles.add(adminRol);
        }
        //Asignar rol de usuario
        RoleEntity userRol=RoleEntity.builder()
                .id(1L)
                .role(Roles.ROLE_USER)
                .build();
        userRoles.add(userRol);
        this.roles=userRoles;
    }

    public void deactivateUser(String reason) {
        if(this.status==UserStatus.DEACTIVATED){
            throw new BusinessException(ErrorCode.USER_IS_DEACTIVATE.name(),
                    ErrorCode.USER_IS_DEACTIVATE.getDefaultMessage(),
                    HttpStatus.CONFLICT);
        }

        this.status = UserStatus.DEACTIVATED;
        this.statusDescription = "User deactivated because "+reason;
        this.updatedAt = LocalDateTime.now();
    }
    public void activateUser(){
        if(this.status==UserStatus.ACTIVE){
            throw new BusinessException(ErrorCode.USER_IS_ACTIVATE.name(),
                    ErrorCode.USER_IS_ACTIVATE.getDefaultMessage(),
                    HttpStatus.CONFLICT);
        }
        if(this.status==UserStatus.SUSPENDED){
            throw new BusinessException(ErrorCode.ADMIN_AUTHORIZE.name(),
                    ErrorCode.ADMIN_AUTHORIZE.getDefaultMessage()+this.status,
                    HttpStatus.UNAUTHORIZED);
        }
        this.status = UserStatus.ACTIVE;
        this.statusDescription = "User active";
        this.updatedAt = LocalDateTime.now();
    }
    public void suspendUser(){
        if(this.status==UserStatus.SUSPENDED){
            throw new BusinessException(ErrorCode.USER_IS_SUSPEND.name(),
                    ErrorCode.USER_IS_SUSPEND.getDefaultMessage(),
                    HttpStatus.CONFLICT);
        }
        this.status = UserStatus.SUSPENDED;
        this.statusDescription = "User suspended";
        this.updatedAt = LocalDateTime.now();
    }
    public void reactivateUser(){
        if(this.status==UserStatus.ACTIVE){
            throw  new BusinessException(ErrorCode.USER_IS_ACTIVATE.name(),
                    ErrorCode.USER_IS_ACTIVATE.getDefaultMessage()+this.status,
                    HttpStatus.CONFLICT);
        }
        this.status = UserStatus.ACTIVE;
        this.statusDescription = "User active";
        this.updatedAt = LocalDateTime.now();
    }
}
