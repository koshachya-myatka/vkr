package ru.datamart.project.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.datamart.project.customExceptions.CustomEntityNotFoundException;
import ru.datamart.project.customExceptions.CustomInvalidRequestException;
import ru.datamart.project.dto.PageResponseDto;
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

    public PageResponseDto<UserListItemDto> getUsers(UserListFilterDto dto) {
        if (dto.getOffset() == null || dto.getLimit() == null || dto.getLimit().equals(0)) {
            throw new CustomInvalidRequestException("Кол-во искомых данных не определено.");
        }
        int limit = dto.getLimit();
        List<UserListItemDto> items = userRepository.getUsers(
                dto.getOffset(), limit, dto.getUsername(),
                dto.getName(), dto.getSurname(), dto.getRole());
        long totalItems = userRepository.countUsers(
                dto.getUsername(), dto.getName(),
                dto.getSurname(), dto.getRole());
        int totalPages = (int) Math.ceil((double) totalItems / limit);
        int currentPage = dto.getOffset() / limit + 1;
        return new PageResponseDto<>(items, totalItems, totalPages, currentPage);
    }

    public void updateUser(UUID userId, UserUpdateDto dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomEntityNotFoundException("Пользователь не найден."));
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPatronymic(dto.getPatronymic());
        user.setEmail(dto.getEmail());
        if (dto.getRole() != null) {
            user.setRole(UserRoleEnum.valueOf(dto.getRole()));
        }
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new CustomInvalidRequestException("Пользователь не был обновлен. Проверьте корректность данных.");
        }
    }
}