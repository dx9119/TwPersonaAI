package com.ukhanov.TwPersonaAI.app.repository;

import com.ukhanov.TwPersonaAI.app.model.AppSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppSettingRepository extends JpaRepository<AppSettingEntity, Long> {
//сущность всегда имеет id = 1;
}
