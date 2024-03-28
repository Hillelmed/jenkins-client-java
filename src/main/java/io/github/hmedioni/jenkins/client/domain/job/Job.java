package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;
import org.springframework.lang.*;

@Data
public class Job {

    @Nullable
    public String clazz;

    public String name;

    public String url;

    @Nullable
    public String color;


//    @SerializedNames({"_class", "name", "url", "color"})
//    public static Job create(final String clazz, final String name, final String url, final String color) {
//        return new AutoValue_Job(clazz, name, url, color);
//    }
}
