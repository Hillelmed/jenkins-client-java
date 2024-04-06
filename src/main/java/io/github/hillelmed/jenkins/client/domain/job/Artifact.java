package io.github.hillelmed.jenkins.client.domain.job;

import lombok.*;

@Data
@NoArgsConstructor
public class Artifact {


    private String displayPath;

    private String fileName;

    private String relativePath;


}
