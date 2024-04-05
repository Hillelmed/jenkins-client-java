package io.github.hmedioni.jenkins.client.domain.job;

import lombok.*;

@Data
@NoArgsConstructor
public class Cause {


    public String clazz;

    public String shortDescription;


    public String userId;


    public String userName;

//
//    @SerializedNames({"_class", "shortDescription", "userId", "userName"})
//    public static Cause create(final String clazz, final String shortDescription, final String userId, final String userName) {
//        return new AutoValue_Cause(clazz, shortDescription, userId, userName);
//    }
}
