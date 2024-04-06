package io.github.hillelmed.jenkins.client.domain.common;

import lombok.*;
import org.jetbrains.annotations.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegerResponse {

    @NotNull
    private Integer values;
}
