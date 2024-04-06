package io.github.hmedioni.jenkins.client.domain.system;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class SystemInfo {

    @JsonProperty("_class")
    private String clazz;
    private ArrayList<AssignedLabel> assignedLabels;
    private String mode;
    private String nodeDescription;
    private String nodeName;
    private int numExecutors;
    private String description;
    private ArrayList<Object> jobs;
    private OverallLoad overallLoad;
    private PrimaryView primaryView;
    private Object quietDownReason;
    private boolean quietingDown;
    private int slaveAgentPort;
    private UnlabeledLoad unlabeledLoad;
    private String url;
    private boolean useCrumbs;
    private boolean useSecurity;
    private ArrayList<View> views;


    @Data
    @NoArgsConstructor
    public static class AssignedLabel {
        private String name;
    }

    @Data
    @NoArgsConstructor
    public static class OverallLoad {
    }

    @Data
    @NoArgsConstructor
    public static class PrimaryView {
        @JsonProperty("_class")
        private String clazz;
        private String name;
        private String url;
    }

    @Data
    @NoArgsConstructor

    public static class UnlabeledLoad {
        @JsonProperty("_class")
        private String clazz;
    }

    @Data
    @NoArgsConstructor

    public static class View {
        @JsonProperty("_class")
        private String clazz;
        private String name;
        private String url;
    }

}
