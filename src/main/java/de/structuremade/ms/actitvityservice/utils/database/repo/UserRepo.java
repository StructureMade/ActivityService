package de.structuremade.ms.actitvityservice.utils.database.repo;

import de.structuremade.ms.actitvityservice.utils.database.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
}
