package de.structuremade.ms.actitvityservice.api.routes;

import com.google.gson.Gson;
import de.structuremade.ms.actitvityservice.api.json.CreateActivity;
import de.structuremade.ms.actitvityservice.api.json.VoteActivity;
import de.structuremade.ms.actitvityservice.api.json.answer.GetActivity;
import de.structuremade.ms.actitvityservice.api.json.answer.ShowActivity;
import de.structuremade.ms.actitvityservice.api.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("service/activity")
public class ActivityRoute {


    private final Gson gson = new Gson();
    @Autowired
    ActivityService activityService;

    @CrossOrigin
    @PostMapping("/create")
    public void create(@RequestBody CreateActivity createActivity, HttpServletResponse response, HttpServletRequest request) {
        switch (activityService.create(createActivity, request.getHeader("Authorization").substring(7))) {
            case 0 -> response.setStatus(HttpStatus.UNAUTHORIZED.value());
            case 1 -> response.setStatus(HttpStatus.CREATED.value());
            case 2 -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @CrossOrigin
    @GetMapping("/show")
    public Object show(HttpServletResponse response, HttpServletRequest request) {
        List<ShowActivity> returnValue = activityService.show(request.getHeader("Authorization").substring(7));
        if (returnValue.get(0) == null) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return null;
        }
        response.setStatus(HttpStatus.OK.value());
        return gson.toJson(returnValue);
    }

    @CrossOrigin
    @GetMapping("/get/{lessonid}")
    public Object get(@PathVariable String lessonid, HttpServletResponse response, HttpServletRequest request) {
        List<GetActivity> activities = activityService.get(lessonid,request.getHeader("Authorization").substring(7));
        if (activities != null) {
            if (activities.get(0) != null){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return null;
            }
            response.setStatus(HttpStatus.OK.value());
            return gson.toJson(activities);
        } else response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return null;
    }

    @CrossOrigin
    @PutMapping("/vote")
    public void vote(@RequestBody VoteActivity voteActivity, HttpServletResponse response, HttpServletRequest request){
        switch (activityService.vote(voteActivity, request.getHeader("Authorization").substring(7))){
            case 0 -> response.setStatus(HttpStatus.OK.value());
            case 1 -> response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            case 2 -> response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
