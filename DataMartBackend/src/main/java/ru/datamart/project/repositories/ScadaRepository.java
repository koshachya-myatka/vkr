package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.datamart.project.models.ScadaEntity;

public interface ScadaRepository extends JpaRepository<ScadaEntity, String> {
}