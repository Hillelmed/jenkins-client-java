package io.github.hillelmed.jenkins.client.domain.statistics;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class OverallLoad {


    private Map<String, String> availableExecutors;


    private Map<String, String> busyExecutors;


    private Map<String, String> connectingExecutors;


    private Map<String, String> definedExecutors;


    private Map<String, String> idleExecutors;


    private Map<String, String> onlineExecutors;


    private Map<String, String> queueLength;


    private Map<String, String> totalExecutors;


    private Map<String, String> totalQueueLength;


}
