package io.github.hmedioni.jenkins.client.domain.user;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class User {

    private String id;
    @JsonProperty("_class")
    private String clazz;
    private String absoluteUrl;
    private Object description;
    private String fullName;
    private List<Property> property;

    @Data
    @NoArgsConstructor
    public static class Property {

        @JsonProperty("_class")
        private String clazz;
        private Object address;
        private boolean insensitiveSearch;
    }

}
