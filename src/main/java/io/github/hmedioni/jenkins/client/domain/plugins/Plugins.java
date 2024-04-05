package io.github.hmedioni.jenkins.client.domain.plugins;


import lombok.*;

import java.util.*;

@Data
public class Plugins {


    public String clazz;

    public List<Plugin> plugins;

//    @SerializedNames({"_class", "plugins", "errors"})
//    public static Plugins create(final String clazz,
//                                 final List<Plugin> plugins,
//                                 final List<Error> errors) {
//        return new AutoValue_Plugins(JenkinsUtils.nullToEmpty(errors),
//            clazz,
//            JenkinsUtils.nullToEmpty(plugins));
//    }
}
