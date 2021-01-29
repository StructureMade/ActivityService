package de.structuremade.ms.actitvityservice.api.services;

import de.structuremade.ms.actitvityservice.api.json.CreateActivity;
import de.structuremade.ms.actitvityservice.api.json.VoteActivity;
import de.structuremade.ms.actitvityservice.api.json.answer.GetActivity;
import de.structuremade.ms.actitvityservice.api.json.answer.ShowActivity;
import de.structuremade.ms.actitvityservice.utils.JWTUtil;
import de.structuremade.ms.actitvityservice.utils.database.entities.Activities;
import de.structuremade.ms.actitvityservice.utils.database.entities.LessonRoles;
import de.structuremade.ms.actitvityservice.utils.database.entities.User;
import de.structuremade.ms.actitvityservice.utils.database.repo.ActivitieRepo;
import de.structuremade.ms.actitvityservice.utils.database.repo.LessonRepo;
import de.structuremade.ms.actitvityservice.utils.database.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ActivityService {

    private final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);
    @Autowired
    ActivitieRepo activitieRepo;
    @Autowired
    LessonRepo lessonRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    JWTUtil jwtUtil;

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
            LessonRoles lr = lessonRepo.getOne(ca.getLesson());
            if (lr.getSchool().getId().equals(jwtUtil.extractSpecialClaim(jwt, "schoolid"))) return 2;
            activitie.setLesson(lr);
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
            List<Activities> watchedActivity = user.getWatched();
            user.getLessonRoles().forEach(lessonrole -> {
                lessonrole.getActivities().forEach(activity -> {
                    if (watchedActivity.contains(activity)) {
                        activities.add(new ShowActivity(activity.getId(), activity.getLesson().getId(), activity.getLesson().getName(), true));
                    } else {
                        activities.add(new ShowActivity(activity.getId(), activity.getLesson().getId(), activity.getLesson().getName(), false));
                    }
                });
            });
            return activities;
        } catch (Exception e) {
            LOGGER.error("Couldn't get activities", e.fillInStackTrace());
            return null;

        }
    }

    public List<GetActivity> get(String lesson, String jwt) {
        /*Variables*/
        List<GetActivity> activities = new ArrayList<>();
        try {
            LessonRoles lr = lessonRepo.getOne(lesson);
            if (lr.getSchool().getId().equals(jwtUtil.extractSpecialClaim(jwt, "schoolid"))) return new ArrayList<>();
            LOGGER.info("Get all activities");
            activitieRepo.findAllByLesson(lr).forEach(activity -> {
                activities.add(new GetActivity(activity));
            });
            return activities;
        } catch (Exception e) {
            LOGGER.error("Couldn't get Activities", e.fillInStackTrace());
            return null;
        }
    }

    public int vote(VoteActivity va, String jwt) {
        Activities activity;
        try {
            LOGGER.info("Update vote");
            activity = activitieRepo.getOne(va.getActivity());
            if (activity.getLesson().getSchool().getId().equals(jwtUtil.extractSpecialClaim(jwt, "schoolid"))) return 2;
            if (va.isYesOrNo()) {
                activity.setYes(activity.getYes() + 1);
            } else {
                activity.setNo(activity.getNo() + 1);
            }
            return 0;
        } catch (Exception e) {
            LOGGER.error("Couldn't update votes", e.fillInStackTrace());
            return 1;
        }
    }
}

