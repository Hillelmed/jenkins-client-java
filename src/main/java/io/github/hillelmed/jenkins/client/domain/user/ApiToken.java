package io.github.hillelmed.jenkins.client.domain.user;

import lombok.*;

@Data
@NoArgsConstructor
public class ApiToken {

    private String status;

    private ApiTokenData data;


}
