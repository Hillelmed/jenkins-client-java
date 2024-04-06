package io.github.hillelmed.jenkins.client.domain.job;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class JobList {

    @JsonProperty("_class")
    private String clazz;

    private List<Job> jobs;


    private String url;


}
