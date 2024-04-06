package io.github.hmedioni.jenkins.client.domain.job;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class Action {

    private List<Cause> causes;

    private List<Parameter> parameters;


    private String text;


    private String iconPath;


    @JsonProperty("_class")
    private String clazz;


}

