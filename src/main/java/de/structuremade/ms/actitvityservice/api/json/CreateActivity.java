package de.structuremade.ms.actitvityservice.api.json;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateActivity {

    @NotNull(message = "survey need true or false")
    private boolean survey;

    @NotNull(message = "lesson is needed")
    private String lesson;

    private String text;


}
