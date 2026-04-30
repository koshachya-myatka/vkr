package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.datamart.project.models.LimsResultEntity;

public interface LimsResultRepository extends JpaRepository<LimsResultEntity, Long> {
}