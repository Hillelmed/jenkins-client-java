package io.github.hmedioni.jenkins.client.domain.job;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
public class ChangeSetList {

    private List<ChangeSet> items;


    private String kind;


}
