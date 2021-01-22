package de.structuremade.ms.actitvityservice.api.json.answer;

import de.structuremade.ms.actitvityservice.utils.database.entities.Activities;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetActivity {
    private String text;
    private boolean survey;
    private int yes;
    private int no;

    public GetActivity(Activities activities){
        text = activities.getText();
        survey = activities.isSurvey();
        yes = activities.getYes();
        no = activities.getNo();
    }
}
