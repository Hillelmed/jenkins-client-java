package io.github.hillelmed.jenkins.client.domain.job;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@NoArgsConstructor
public class Parameter {


    @JsonProperty("_class")
    private String clazz;

    private String name;


    private String value;

}
