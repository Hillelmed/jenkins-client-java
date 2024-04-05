package io.github.hmedioni.jenkins.client.domain.statistics;

import lombok.*;

import java.util.*;

@Data
public class OverallLoad {


    public Map<String, String> availableExecutors;


    public Map<String, String> busyExecutors;


    public Map<String, String> connectingExecutors;


    public Map<String, String> definedExecutors;


    public Map<String, String> idleExecutors;


    public Map<String, String> onlineExecutors;


    public Map<String, String> queueLength;


    public Map<String, String> totalExecutors;


    public Map<String, String> totalQueueLength;


//    @SerializedNames({"availableExecutors", "busyExecutors", "connectingExecutors", "definedExecutors", "idleExecutors",
//        "onlineExecutors", "queueLength", "totalExecutors", "totalQueueLength"})
//    public static OverallLoad create(Map<String, String> availableExecutors, Map<String, String> busyExecutors,
//                                     Map<String, String> connectingExecutors, Map<String, String> definedExecutors,
//                                     Map<String, String> idleExecutors, Map<String, String> onlineExecutors, Map<String, String> queueLength,
//                                     Map<String, String> totalExecutors, Map<String, String> totalQueueLength) {
//        return new AutoValue_OverallLoad(availableExecutors, busyExecutors,
//            connectingExecutors, definedExecutors,
//            idleExecutors, onlineExecutors,
//            queueLength, totalExecutors,
//            totalQueueLength);
//    }
}
