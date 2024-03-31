package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;
import org.springframework.lang.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {

    @Nullable
    private String clazz;

    private String name;

    private String url;

    @Nullable
    private String color;


//    @SerializedNames({"_class", "name", "url", "color"})
//    public static Job create(final String clazz, final String name, final String url, final String color) {
//        return new AutoValue_Job(clazz, name, url, color);
//    }
}
