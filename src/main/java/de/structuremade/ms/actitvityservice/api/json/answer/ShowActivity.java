package de.structuremade.ms.actitvityservice.api.json.answer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShowActivity {
    private String activity;
    private String lessonId;
    private String lesson;
    private boolean watched;
}
