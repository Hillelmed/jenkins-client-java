package io.github.hmedioni.jenkins.client.domain.common;


import lombok.*;

@Data
@AllArgsConstructor
public class Error {

    public String context;

    public String message;

    public String exceptionName;

}
