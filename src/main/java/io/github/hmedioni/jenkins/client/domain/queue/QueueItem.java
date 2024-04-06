package io.github.hmedioni.jenkins.client.domain.queue;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class QueueItem {

    @JsonProperty("_class")
    private String clazz;
    private ArrayList<Action> actions;
    private boolean blocked;
    private boolean buildable;
    private Integer id;
    private Long inQueueSince;
    private String params;
    private boolean stuck;
    private Task task;
    private String url;
    private String why;
    private boolean cancelled;
    private Executable executable;

    @Data
    @NoArgsConstructor
    public static class Action {
        @JsonProperty("_class")
        private String clazz;
        private List<Parameter> parameters;
        private List<Cause> causes;
    }

    @Data
    @NoArgsConstructor
    public static class Parameter {
        @JsonProperty("_class")
        private String clazz;
        private String name;
        private String value;
    }


    @Data
    @NoArgsConstructor
    public static class Cause {
        @JsonProperty("_class")
        private String clazz;
        private String shortDescription;
        private String userId;
        private String userName;
    }

    @Data
    @NoArgsConstructor
    public static class Executable {
        @JsonProperty("_class")
        private String clazz;
        private Integer number;
        private String url;
    }

    @Data
    @NoArgsConstructor
    public static class Task {
        @JsonProperty("_class")
        private String clazz;
        private String name;
        private String url;
        private String color;
    }


}
