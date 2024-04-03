package io.github.hmedioni.jenkins.client.domain.job;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {


    @JsonProperty("_class")
    private String clazz;

    private String name;

    private String url;


    private String color;


//    @SerializedNames({"_class", "name", "url", "color"})
//    public static Job create(final String clazz, final String name, final String url, final String color) {
//        return new AutoValue_Job(clazz, name, url, color);
//    }
}
