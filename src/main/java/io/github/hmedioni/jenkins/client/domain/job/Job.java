package io.github.hmedioni.jenkins.client.domain.job;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {


    @JsonProperty("_class")
    private String clazz;

    private String name;

    private String url;


    private String color;


}
