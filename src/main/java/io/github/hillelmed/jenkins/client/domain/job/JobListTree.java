package io.github.hillelmed.jenkins.client.domain.job;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobListTree {

    @JsonProperty("_class")
    private String clazz;

    private String name;

    private String fullName;

    private List<JobListTree> jobs;

    private String color;

    private String url;

}
