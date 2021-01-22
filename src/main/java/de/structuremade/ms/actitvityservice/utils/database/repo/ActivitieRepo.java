package de.structuremade.ms.actitvityservice.utils.database.repo;

import de.structuremade.ms.actitvityservice.utils.database.entities.Activities;
import de.structuremade.ms.actitvityservice.utils.database.entities.LessonRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivitieRepo extends JpaRepository<Activities, String> {
    List<Activities> findAllByLesson(LessonRoles one);

}
