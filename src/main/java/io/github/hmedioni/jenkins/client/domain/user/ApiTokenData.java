package io.github.hmedioni.jenkins.client.domain.user;


import lombok.*;

@Data
@NoArgsConstructor
public class ApiTokenData {

    public String tokenName;
    public String tokenUuid;
    public String tokenValue;


//    @SerializedNames({"tokenName", "tokenUuid", "tokenValue"})
//    public static ApiTokenData create(final String tokenName, final String tokenUuid, final String tokenValue) {
//        return new AutoValue_ApiTokenData(tokenName, tokenUuid, tokenValue);
//    }
}
