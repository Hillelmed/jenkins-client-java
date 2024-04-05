package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;


@Data
public class Stage {
    public String id;

    public String name;

    public String status;

    public long startTimeMillis;

    public long endTimeMillis;

    public long pauseDurationMillis;

    public long durationMillis;


//    @SerializedNames({"id", "name", "status", "startTimeMillis", "endTimeMillis", "pauseDurationMillis", "durationMillis"})
//    public static Stage create(String id, String name, String status, long startTimeMillis, long endTimeMillis, long pauseDurationMillis, long durationMillis) {
//        return new AutoValue_Stage(id, name, status, startTimeMillis, endTimeMillis, pauseDurationMillis, durationMillis);
//    }
}
