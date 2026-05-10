package com.canyoncompanion.canyon_api.service.user;

import com.canyoncompanion.canyon_api.dtos.requests.AuthRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.TokenRequestDTO;
import com.canyoncompanion.canyon_api.dtos.requests.UserRequestDTO;
import com.canyoncompanion.canyon_api.dtos.responses.AuthResponse;
import com.canyoncompanion.canyon_api.exception.BusinessException;
import com.canyoncompanion.canyon_api.exception.ErrorCode;
import com.canyoncompanion.canyon_api.model.entities.RefreshTokenEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.repository.UserRepository;
import com.canyoncompanion.canyon_api.security.JwtService;
import com.canyoncompanion.canyon_api.security.RefreshTokenService;
import com.canyoncompanion.canyon_api.util.mappers.UserMapper;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponse registerUser(UserRequestDTO request) {
        return registerUser(request,false);
    }

    @Override
    public AuthResponse registerAdminUser(UserRequestDTO request) {
        return registerUser(request,true);
    }

    @Override
    public AuthResponse login(AuthRequestDTO loginRequest) {

        // 1️⃣ Autenticación del usuario usando email y contraseña
        // Si las credenciales son incorrectas, lanzará una excepción
       try{
        Authentication authentication = authenticationManager.authenticate(
                //Se crea un Authentication NO autenticado
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),      // Email enviado por el cliente
                        loginRequest.getPassword()    // Contraseña enviada por el cliente
                ));

        // 2️⃣ Guardar la autenticación en el contexto de seguridad de Spring
        // Esto permite que el usuario quede "logueado" dentro del contexto de la aplicación
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3️⃣ Obtener los detalles del usuario autenticado
           UserDetails userDetails = getDetails(authentication);
           String accessToken = jwtService.generateToken(userDetails);        
           String refreshToken = refreshTokenService.createOrRefreshToken(userDetails);

           // 5️⃣ Construir la respuesta para el cliente
           return AuthResponse.builder()
                   .accessToken(accessToken) // token de acceso
                   .refreshToken(refreshToken) // token de refresco
                   .userName(Objects.requireNonNull(userDetails).getUsername()) // nombre de usuario o email
                   .roles(userDetails.getAuthorities().stream() // roles del usuario
                           .map(GrantedAuthority::getAuthority)
                           .collect(Collectors.toList()))
                   .build();
       }catch (UsernameNotFoundException ex) {
           // Usuario no existe
           throw new BusinessException(
                   ErrorCode.USER_NOT_FOUND.name(),
                   ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                   HttpStatus.NOT_FOUND
           );
       } catch (BadCredentialsException ex) {
           // Contraseña incorrecta
           throw new BusinessException(
                   ErrorCode.INVALID_CREDENTIALS_PASSWORD.name(),
                   ErrorCode.INVALID_CREDENTIALS_PASSWORD.getDefaultMessage(),
                   HttpStatus.UNAUTHORIZED
           );
       }
    }

    private static @NonNull UserDetails getDetails(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // UserDetails incluye username, roles, etc. (la contraseña ya no se necesita aquí)

        // 4️⃣ Generar tokens JWT
        // Access token: se usa para autenticar futuras peticiones
        // Refresh token: se usa para obtener un nuevo access token cuando caduque
        if (userDetails == null) {
               throw new BusinessException(ErrorCode.USER_CANNOT_NULL.name(),
                       ErrorCode.USER_CANNOT_NULL.getDefaultMessage(),
                       HttpStatus.NOT_ACCEPTABLE);
        }
        return userDetails;
    }

    @Override
    public AuthResponse refreshToken(TokenRequestDTO request) {

        // Buscar el refresh token en la base de datos
        RefreshTokenEntity refreshTokenEntity =refreshTokenService.findByToken(request.getToken());

       // Obtener información del usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(
                refreshTokenEntity.getUser().getEmail()
        );
        // Generar nuevo access token
        String newAccessToken = jwtService.generateToken(userDetails);

        // Generar nuevo refresh token
        String newRefreshToken = refreshTokenService.createOrRefreshToken(userDetails);

        // Construir la respuesta
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)  // nuevo refresh token
                .userName(userDetails.getUsername())
                .roles(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public boolean resendVerificationEmail(String email) {
        return false;
    }

    @Override
    public boolean confirmUserAccount(String verificationToken) {
        return false;
    }

    private AuthResponse registerUser(UserRequestDTO request, boolean isAdmin){

        // Verificar si el nombre y/o el email están disponibles
        if (!isEmailAvailable(request.getEmail())) {
            throw new BusinessException(
                    ErrorCode.INVALID_CREDENTIALS_EMAIL.name(),
                    ErrorCode.INVALID_CREDENTIALS_EMAIL.getDefaultMessage(),
                    HttpStatus.UNAUTHORIZED);
        }
        try {
            UserEntity userEntity = userMapper.toUserEntity(request);

            userEntity.addRolesToUser(isAdmin);
            userEntity.encodePassword(request.getPassword());

            //Guarda usuario en base de datos
            userRepository.save(userEntity);

            // Autenticación inmediata
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var userDetails = (UserDetails) authentication.getPrincipal();
            // Generar tokens
            if (userDetails == null) {
                throw new BusinessException(ErrorCode.USER_CANNOT_NULL.name(),
                        ErrorCode.USER_CANNOT_NULL.getDefaultMessage(),
                        HttpStatus.NOT_ACCEPTABLE);
            }
            String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = refreshTokenService.createOrRefreshToken(userDetails);

            // Devolver respuesta con tokens

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userName(userDetails.getUsername())
                    .roles(userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .build();
        }catch (UsernameNotFoundException ex) {
            // Usuario no existe
            throw new BusinessException(
                    ErrorCode.USER_NOT_FOUND.name(),
                    ErrorCode.USER_NOT_FOUND.getDefaultMessage(),
                    HttpStatus.NOT_FOUND
            );
        }
        catch (AuthenticationException ex) { // <<<<<< Aquí
            // Lanzamos tu excepción personalizada con mensaje uniforme
            throw new BusinessException(
                    ErrorCode.INVALID_CREDENTIALS_PASSWORD.name(),
                    ErrorCode.INVALID_CREDENTIALS_PASSWORD.getDefaultMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
    private boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmailIgnoreCase(email);
    }
}
