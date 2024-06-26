package io.github.hillelmed.jenkins.client.domain.plugins;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class PluginsWrapper {


    @JsonProperty("_class")
    private String clazz;

    private List<Plugin> plugins;


}
