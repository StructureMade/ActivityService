package de.structuremade.ms.actitvityservice.api.services;

import com.google.gson.Gson;
import de.structuremade.ms.actitvityservice.api.json.CreateActivity;
import de.structuremade.ms.actitvityservice.api.json.VoteActivity;
import de.structuremade.ms.actitvityservice.api.json.answer.GetActivity;
import de.structuremade.ms.actitvityservice.api.json.answer.ShowActivity;
import de.structuremade.ms.actitvityservice.utils.JWTUtil;
import de.structuremade.ms.actitvityservice.utils.database.entities.Activities;
import de.structuremade.ms.actitvityservice.utils.database.entities.User;
import de.structuremade.ms.actitvityservice.utils.database.repo.ActivitieRepo;
import de.structuremade.ms.actitvityservice.utils.database.repo.LessonRepo;
import de.structuremade.ms.actitvityservice.utils.database.repo.UserRepo;
import org.hibernate.hql.internal.ast.tree.AbstractNullnessCheckNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ActivityService {

    @Autowired
    ActivitieRepo activitieRepo;

    @Autowired
    LessonRepo lessonRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    JWTUtil jwtUtil;

    private final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);

    public int create(CreateActivity ca, String jwt) {
        Calendar calendar = Calendar.getInstance();
        try {
            LOGGER.info("Check if jwt is expired");
            if (jwtUtil.isTokenExpired(jwt)) {
                return 0;
            }
            LOGGER.info("Set all informations");
            Activities activitie = new Activities();
            activitie.setSurvey(ca.isSurvey());
            LOGGER.info("Get Lesson and set it to activitie");
            activitie.setLesson(lessonRepo.getOne(ca.getLesson()));
            activitie.setText(ca.getText());
            String[] splitDate = ca.getDate().split("\\.");
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitDate[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(splitDate[1]));
            calendar.set(Calendar.YEAR, Integer.parseInt(splitDate[2]));
            activitie.setValidThru(calendar.getTime());
            LOGGER.info("Get teacher and set it to activitie");
            activitie.setUser(userRepo.getOne(jwtUtil.extractIdOrEmail(jwt)));
            activitieRepo.save(activitie);
            return 1;
        } catch (Exception e) {
            LOGGER.error("Couldn't create Activity", e.fillInStackTrace());
            return 2;
        }
    }

    public List<ShowActivity> show(String jwt) {
        /*Variables*/
        List<ShowActivity> activities = new ArrayList<>();
        try {
            LOGGER.info("Get user");
            User user = userRepo.getOne(jwtUtil.extractIdOrEmail(jwt));
            LOGGER.info("Get all active, activities");
            user.getLessonRoles().forEach(lessonrole -> {
                lessonrole.getActivities().forEach(activity -> {
                    if (user.getWatched().contains(activity)) {
                        activities.add(new ShowActivity(activity.getId(), true));
                    } else {
                        activities.add(new ShowActivity(activity.getId(), false));
                    }
                });
            });
            return activities;
        } catch (Exception e) {
            LOGGER.error("Couldn't get activities", e.fillInStackTrace());
            return null;

        }
    }

    public List<GetActivity> get(String lesson) {
        /*Variables*/
        List<GetActivity> activities = new ArrayList<>();
        try {
            LOGGER.info("Get all activities");
            activitieRepo.findAllByLesson(lessonRepo.getOne(lesson)).forEach(activity -> {
                activities.add(new GetActivity(activity));
            });
            return activities;
        } catch (Exception e) {
            LOGGER.error("Couldn't get Activities", e.fillInStackTrace());
            return null;
        }
    }

    public int vote(VoteActivity va){
        Activities activities;
        try{
            LOGGER.info("Update vote");
            activities = activitieRepo.getOne(va.getActivity());
            if (va.isYesOrNo()){
                activities.setYes(activities.getYes()+1);
            }else {
                activities.setNo(activities.getNo()+1);
            }
            return 0;
        }catch (Exception e){
            LOGGER.error("Couldn't update votes", e.fillInStackTrace());
            return 1;
        }
    }
}

