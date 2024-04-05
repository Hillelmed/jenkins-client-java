package io.github.hmedioni.jenkins.client.domain.user;

import lombok.*;

@Data
@NoArgsConstructor
public class ApiToken {

    private String status;

    private ApiTokenData data;


//    @SerializedNames({"status", "data"})
//    public static ApiToken create(final String status, final ApiTokenData data) {
//        return new AutoValue_ApiToken(status, data);
//    }
}
