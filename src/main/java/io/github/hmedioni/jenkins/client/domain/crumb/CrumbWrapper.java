package io.github.hmedioni.jenkins.client.domain.crumb;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrumbWrapper {

    private Crumb crumb;
    private Boolean aBoolean;
}
