package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.datamart.project.customExceptions.InvalidCredentialsException;
import ru.datamart.project.dto.users.UserProfileDto;
import ru.datamart.project.models.UserEntity;
import ru.datamart.project.security.CustomUserDetails;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final ObjectMapper objectMapper;

    public UserProfileDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user;
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            user = userDetails.getUser();
        } catch (NullPointerException e) {
            throw new InvalidCredentialsException("Пользователь не был авторизован.");
        }
        return objectMapper.convertValue(user, UserProfileDto.class);
    }
}