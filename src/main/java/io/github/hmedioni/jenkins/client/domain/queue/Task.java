package io.github.hmedioni.jenkins.client.domain.queue;

import lombok.*;

@Data
public class Task {


    public String name;


    public String url;

//
//    @SerializedNames({"name", "url"})
//    public static Task create(String name, String url) {
//        return new AutoValue_Task(name, url);
//    }
}
