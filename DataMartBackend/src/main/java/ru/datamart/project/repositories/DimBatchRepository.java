package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.datamart.project.models.DimBatchEntity;

public interface DimBatchRepository extends JpaRepository<DimBatchEntity, String> {
}