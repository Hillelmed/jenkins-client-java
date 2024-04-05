package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;

import java.util.*;

@Data
public class Workflow {

    public String name;

    public String status;

    public long startTimeMillis;

    public long durationTimeMillis;

    public List<Stage> stages;


//    @SerializedNames({"name", "status", "startTimeMillis", "durationTimeMillis", "stages"})
//    public static Workflow create(String name, String status, long startTimeMillis, long durationTimeMillis, List<Stage> stages) {
//        return new AutoValue_Workflow(name, status, startTimeMillis, durationTimeMillis, stages);
//    }
}
