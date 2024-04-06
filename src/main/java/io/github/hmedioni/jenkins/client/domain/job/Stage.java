package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;


@Data
@NoArgsConstructor
public class Stage {
    private String id;

    private String name;

    private String status;

    private long startTimeMillis;

    private long endTimeMillis;

    private long pauseDurationMillis;

    private long durationMillis;


}
