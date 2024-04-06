package io.github.hmedioni.jenkins.client.domain.common;


import lombok.*;

@Data
@AllArgsConstructor
public class Error {

    private String context;

    private String message;

    private String exceptionName;

}
