package ru.datamart.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.datamart.project.dto.batchData.BatchMesDto;
import ru.datamart.project.models.MesEntity;

import java.util.Optional;

@Repository
public interface MesRepository extends JpaRepository<MesEntity, String> {
    @Query(value = """
                SELECT
                m.order_id as orderId,
                m.equipment_id as equipmentId,
                m.operator_id as operatorId,  
                m.charge_mass as chargeMass,
                m.output_mass as outputMass,
                m.duration_min as durationMin                
                FROM fact_mes as m
                WHERE m.batch_id = :batchId;
            """, nativeQuery = true)
    Optional<BatchMesDto> getMesByBatchId(@Param("batchId") String batchId);
}