package io.github.hmedioni.jenkins.client.domain.plugins;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class Plugins {


    @JsonProperty("_class")
    private String clazz;

    private List<Plugin> plugins;


}
