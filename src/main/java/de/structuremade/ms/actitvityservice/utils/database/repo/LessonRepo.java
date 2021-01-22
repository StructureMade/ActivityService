package de.structuremade.ms.actitvityservice.utils.database.repo;

import de.structuremade.ms.actitvityservice.utils.database.entities.LessonRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepo extends JpaRepository<LessonRoles, String> {
}
