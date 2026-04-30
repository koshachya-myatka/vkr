package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.datamart.project.models.LimsEntity;

public interface LimsRepository extends JpaRepository<LimsEntity, String> {
}