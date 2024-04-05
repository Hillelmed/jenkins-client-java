package io.github.hmedioni.jenkins.client.domain.job;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class ChangeSet {

    public List<String> affectedPaths;

    public String commitId;

    public long timestamp;

    public Culprit author;


    public String authorEmail;


    public String comment;


//   @SerializedNames({ "affectedPaths", "commitId", "timestamp", "author", "authorEmail", "comment" })
//   public static ChangeSet create(List<String> affectedPaths, String commitId, long timestamp, Culprit author, String authorEmail, String comment) {
//      return new AutoValue_ChangeSet(
//         affectedPaths != null ? ImmutableList.copyOf(affectedPaths) : ImmutableList.<String> of,
//         commitId, timestamp, author, authorEmail, comment);
//   }
}
