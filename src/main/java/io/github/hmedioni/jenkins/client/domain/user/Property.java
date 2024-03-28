package io.github.hmedioni.jenkins.client.domain.user;


import lombok.*;


@Data
public class Property {

    public String clazz;


//    @SerializedNames({"_class"})
//    public static Property create(final String clazz) {
//        return new AutoValue_Property(clazz);
//    }
}
