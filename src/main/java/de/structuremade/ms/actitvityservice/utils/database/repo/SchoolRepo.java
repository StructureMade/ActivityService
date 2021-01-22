package de.structuremade.ms.actitvityservice.utils.database.repo;

import de.structuremade.ms.actitvityservice.utils.database.entities.School;
import de.structuremade.ms.actitvityservice.utils.database.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepo extends JpaRepository<School, String> {
}
