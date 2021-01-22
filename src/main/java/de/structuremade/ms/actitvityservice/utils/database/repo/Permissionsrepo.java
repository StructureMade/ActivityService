package de.structuremade.ms.actitvityservice.utils.database.repo;

import de.structuremade.ms.actitvityservice.utils.database.entities.Permissions;
import de.structuremade.ms.actitvityservice.utils.database.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Permissionsrepo extends JpaRepository<Permissions, String> {
}
