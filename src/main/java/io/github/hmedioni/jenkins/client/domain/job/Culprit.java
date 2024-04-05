package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;


@Data
public class Culprit {

    public String absoluteUrl;

    public String fullName;


//    @SerializedNames({"absoluteUrl", "fullName"})
//    public static Culprit create(String absoluteUrl, String fullName) {
//        return new AutoValue_Culprit(absoluteUrl, fullName);
//    }
}
