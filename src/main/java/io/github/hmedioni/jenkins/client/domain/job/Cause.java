package io.github.hmedioni.jenkins.client.domain.job;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@NoArgsConstructor
public class Cause {


    @JsonProperty("_class")
    private String clazz;

    private String shortDescription;


    private String userId;


    private String userName;


}
