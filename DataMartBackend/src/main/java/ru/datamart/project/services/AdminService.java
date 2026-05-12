package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.customExceptions.InvalidCredentialsException;
import ru.datamart.project.dto.UserListFilterDto;
import ru.datamart.project.dto.UserListItemDto;
import ru.datamart.project.dto.UserUpdateDto;
import ru.datamart.project.models.UserEntity;
import ru.datamart.project.models.UserRoleEnum;
import ru.datamart.project.repositories.UserRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public List<UserListItemDto> getUsers(UserListFilterDto dto) {
        return userRepository.getUsers(dto.getOffset(), dto.getUsername(),
                dto.getName(), dto.getSurname(), dto.getRole());
    }

    public void updateUser(UUID userId, UserUpdateDto dto) {
        UserEntity user = userRepository
                .findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("Пользователь не найден"));
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPatronymic(dto.getPatronymic());
        user.setEmail(dto.getEmail());
        if (dto.getRole() != null) {
            user.setRole(UserRoleEnum.valueOf(dto.getRole()));
        }
        userRepository.save(user);
    }
}