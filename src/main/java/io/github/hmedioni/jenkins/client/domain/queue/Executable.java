package io.github.hmedioni.jenkins.client.domain.queue;


import lombok.*;

@Data
public class Executable {

    public Integer number;

    public String url;


//    @SerializedNames({"number", "url"})
//    public static Executable create(Integer number, String url) {
//        return new AutoValue_Executable(number, url);
//    }
}
