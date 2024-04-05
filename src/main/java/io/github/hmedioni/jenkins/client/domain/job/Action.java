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

//    @SerializedNames({"causes", "parameters", "text", "iconPath", "_class"})
//    public static Action create(final List<Cause> causes, final List<Parameter> parameters, final String text, final String iconPath, final String _class) {
//        return new AutoValue_Action(
//            causes != null ? ImmutableList.copyOf(causes) : ImmutableList.<Cause>of,
//            parameters != null ? ImmutableList.copyOf(parameters) : ImmutableList.<Parameter>of,
//            text, iconPath, _class
//        );
//    }
}

