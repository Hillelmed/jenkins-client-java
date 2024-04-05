package io.github.hmedioni.jenkins.client.domain.system;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class SystemInfo {

    public String _class;
    public ArrayList<AssignedLabel> assignedLabels;
    public String mode;
    public String nodeDescription;
    public String nodeName;
    public int numExecutors;
    public String description;
    public ArrayList<Object> jobs;
    public OverallLoad overallLoad;
    public PrimaryView primaryView;
    public Object quietDownReason;
    public boolean quietingDown;
    public int slaveAgentPort;
    public UnlabeledLoad unlabeledLoad;
    public Object url;
    public boolean useCrumbs;
    public boolean useSecurity;
    public ArrayList<View> views;

    public static class AssignedLabel {
        public String name;
    }

    public static class OverallLoad {
    }

    public static class PrimaryView {
        public String _class;
        public String name;
        public String url;
    }

    public static class UnlabeledLoad {
        public String _class;
    }

    public static class View {
        public String _class;
        public String name;
        public String url;
    }

}
