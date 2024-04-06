package io.github.hmedioni.jenkins.client.domain.queue;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class QueueItem {

    private String _class;
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
        private String _class;
        private List<Parameter> parameters;
        private List<Cause> causes;
    }

    @Data
    @NoArgsConstructor
    public static class Parameter {
        private String _class;
        private String name;
        private String value;
    }


    @Data
    @NoArgsConstructor
    public static class Cause {
        private String _class;
        private String shortDescription;
        private String userId;
        private String userName;
    }

    @Data
    @NoArgsConstructor
    public static class Executable {
        private String _class;
        private Integer number;
        private String url;
    }

    @Data
    @NoArgsConstructor
    public static class Task {
        private String _class;
        private String name;
        private String url;
        private String color;
    }


//    @SerializedNames({"blocked", "buildable", "id", "inQueueSince", "params", "stuck", "task", "url", "why",
//        "buildableStartMilliseconds", "cancelled", "executable", "timestamp"})
//    public static QueueItem create(boolean blocked, boolean buildable, int id, long inQueueSince, String params,
//                                   boolean stuck, Task task, String url, String why, long buildableStartMilliseconds,
//                                   boolean cancelled, Executable executable, Long timestamp) {
//        Map<String, String> parameters = Maps.newHashMap;
//        if (params != null) {
//            params = params.trim;
//            if (params.length > 0) {
//                for (String keyValue : params.split("\n")) {
//                    String[] pair = keyValue.split("=");
//                    parameters.put(pair[0], pair.length > 1 ? pair[1] : "");
//                }
//            }
//        }
//        return new AutoValue_QueueItem(blocked, buildable, id, inQueueSince, parameters, stuck, task, url, why,
//            buildableStartMilliseconds, cancelled, executable, timestamp);
//    }
}
