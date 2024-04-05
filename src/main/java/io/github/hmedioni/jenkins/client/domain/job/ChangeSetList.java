package io.github.hmedioni.jenkins.client.domain.job;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class ChangeSetList {

    public List<ChangeSet> items;


    public String kind;


//   @SerializedNames({ "items", "kind" })
//   public static ChangeSetList create(List<ChangeSet> items, String kind) {
//      return new AutoValue_ChangeSetList(
//         items != null ? ImmutableList.copyOf(items) : ImmutableList.<ChangeSet> of,
//         kind);
//   }
}
