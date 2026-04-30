package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.datamart.project.models.MesEntity;

public interface MesRepository extends JpaRepository<MesEntity, String> {
}