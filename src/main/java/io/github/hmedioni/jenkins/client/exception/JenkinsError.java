package io.github.hmedioni.jenkins.client.exception;

import lombok.*;

import java.io.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JenkinsError implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;


    private String context;


    private String message;


    private String exceptionName;

    private boolean conflicted;


}
