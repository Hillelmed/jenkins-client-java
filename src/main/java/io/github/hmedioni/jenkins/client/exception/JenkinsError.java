package io.github.hmedioni.jenkins.client.exception;

import lombok.*;
import org.springframework.lang.*;

import java.io.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JenkinsError implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;


    @Nullable
    private String context;

    @Nullable
    private String message;

    @Nullable
    private String exceptionName;

    private boolean conflicted;


}
