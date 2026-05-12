package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.datamart.project.dto.UserListItemDto;
import ru.datamart.project.models.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value = """
            SELECT
                u.user_id as userId,
                u.username as username,
                u.name as name,
                u.surname as surname,
                u.patronymic as patronymic,
                u.email as email,
                u.role as role,
                u.created_at as createdAt
            FROM users as u
            WHERE
                (:username IS NULL OR u.username ILIKE (CONCAT('%', :username, '%'))) AND
                (:name IS NULL OR u.name ILIKE (CONCAT('%', :name, '%'))) AND
                (:surname IS NULL OR u.surname ILIKE (CONCAT('%', :surname, '%'))) AND
                (:role IS NULL OR u.role = :role)
            ORDER BY u.created_at DESC
            LIMIT 20 OFFSET :offset;
            """,
            nativeQuery = true)
    List<UserListItemDto> getUsers(@Param("offset") int offset,
                                   @Param("username") String username,
                                   @Param("name") String name,
                                   @Param("surname") String surname,
                                   @Param("role") String role
    );
}