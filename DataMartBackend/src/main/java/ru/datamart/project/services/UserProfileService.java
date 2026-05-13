package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.datamart.project.dto.UserProfileDto;
import ru.datamart.project.models.UserEntity;
import ru.datamart.project.security.CustomUserDetails;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final ObjectMapper objectMapper;

    public UserProfileDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity user = userDetails.getUser();
        return objectMapper.convertValue(user, UserProfileDto.class);
    }
}
