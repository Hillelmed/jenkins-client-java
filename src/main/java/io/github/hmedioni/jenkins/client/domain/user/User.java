package io.github.hmedioni.jenkins.client.domain.user;


import lombok.*;

@Data
public class User {

    public String absoluteUrl;


    public String description;

    public String fullName;

    public String id;

    // TODO: Find a way to support properties, which is a list of different extensions of a base class
    // public  List<Property> properties;


//    @SerializedNames({"absoluteUrl", "description", "fullName", "id"})
//    public static User create(final String absoluteUrl, final String description, final String fullName, final String id) {
//        return new AutoValue_User(absoluteUrl, description, fullName, id);
//    }
}
