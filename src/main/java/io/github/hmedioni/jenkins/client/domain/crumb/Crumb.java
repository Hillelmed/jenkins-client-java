package io.github.hmedioni.jenkins.client.domain.crumb;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@NoArgsConstructor
public class Crumb {


    @JsonProperty("_class")
    private String clazz;

    @JsonProperty("crumb")
    private String value;

    private String crumbRequestField;

}
