package io.github.hmedioni.jenkins.client.domain.job;

import lombok.*;

@Data
@NoArgsConstructor
public class Artifact {


    public String displayPath;

    public String fileName;

    public String relativePath;


//   @SerializedNames({ "displayPath", "fileName", "relativePath" })
//   public static Artifact create(String displayPath, String fileName, String relativePath) {
//      return new AutoValue_Artifact(displayPath, fileName, relativePath);
//   }
}
