package io.github.hmedioni.jenkins.client.domain.common;

import lombok.*;
import org.jetbrains.annotations.*;

/**
 * Integer response to be returned when an endpoint returns
 * an integer.
 *
 * <p>When the HTTP response code is valid the `value` parameter will
 * be set to the integer value while a non-valid response has the `value` set to
 * null along with any potential `error` objects returned from Jenkins.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegerResponse {

    @NotNull
    private Integer values;
}
