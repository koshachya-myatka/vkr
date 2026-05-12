package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.datamart.project.customExceptions.InvalidCredentialsException;
import ru.datamart.project.customExceptions.InvalidEmailException;
import ru.datamart.project.customExceptions.InvalidRequestException;
import ru.datamart.project.customExceptions.UserAlreadyExistsException;
import ru.datamart.project.dto.AuthResponseDto;
import ru.datamart.project.dto.LoginRequestDto;
import ru.datamart.project.dto.RegisterRequestDto;
import ru.datamart.project.models.UserEntity;
import ru.datamart.project.models.UserRoleEnum;
import ru.datamart.project.repositories.UserRepository;
import ru.datamart.project.security.CustomUserDetails;
import ru.datamart.project.security.JwtService;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public void register(RegisterRequestDto dto) {
        validateRegister(dto);
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("Пользователь с таким логином уже существует");
        }
        UserEntity user = UserEntity.builder()
                .userId(UUID.randomUUID())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .surname(dto.getSurname())
                .name(dto.getName())
                .patronymic(dto.getPatronymic())
                .email(dto.getEmail())
                .role(UserRoleEnum.valueOf(dto.getRole()))
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        log.info("СОЗДАН ПОЛЬЗОВАТЕЛЬ: " + user);
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        if (dto.getUsername() == null || dto.getPassword() == null) {
            throw new InvalidRequestException("Логин и пароль обязательны");
        }
        UserEntity entity = userRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Пользователь не найден"));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Неверный пароль");
        }
        UserDetails user = new CustomUserDetails(entity);
        String token = jwtService.generateToken(user);
        return new AuthResponseDto(token, entity.getRole().name());
    }

    private void validateRegister(RegisterRequestDto dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()
                ||
                dto.getPassword() == null || dto.getPassword().isBlank()
                ||
                dto.getName() == null || dto.getName().isBlank()
                ||
                dto.getSurname() == null || dto.getSurname().isBlank()
                ||
                dto.getEmail() == null || dto.getEmail().isBlank()
                ||
                dto.getRole() == null || dto.getRole().isBlank()
        ) {
            throw new InvalidRequestException("Все обязательные поля должны быть заполнены");
        }
        if (!dto.getEmail().matches("^[A-Za-z0-9_.]+@+([A-Za-z]+[.]{1}[A-Za-z]+)+$")) {
            throw new InvalidEmailException("Некорректный email");
        }
    }
}