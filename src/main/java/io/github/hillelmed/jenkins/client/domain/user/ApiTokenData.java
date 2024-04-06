package io.github.hillelmed.jenkins.client.domain.user;


import lombok.*;

@Data
@NoArgsConstructor
public class ApiTokenData {

    private String tokenName;
    private String tokenUuid;
    private String tokenValue;


}
