/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hmedioni.jenkins.client.features;

import com.fasterxml.jackson.databind.*;
import io.github.hmedioni.jenkins.client.binders.*;
import io.github.hmedioni.jenkins.client.domain.common.*;
import io.github.hmedioni.jenkins.client.domain.job.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

import java.io.*;
import java.util.*;

//@RequestFilters(JenkinsAuthenticationFilter.class)
//@Path("/")
@HttpExchange(url = "/", accept = MediaType.TEXT_XML_VALUE, contentType = MediaType.TEXT_XML_VALUE)
public interface JobsApi {

    //    // @Named("jobs:get-jobs")
//    @Path("{folderPath}api/json")
//    @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @GetExchange
    @GetExchange("/${folderPath}api/json")
    ResponseEntity<JobList> jobList(@PathVariable("folderPath") String folderPath);

    // @Named("jobs:job-info")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//    @Consumes(MediaType.APPLICATION_JSON)
    @GetExchange("{optionalFolderPath}job/{name}/api/json")
    JobInfo jobInfo(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                    @PathVariable("name") String jobName);

    // @Named("jobs:artifact")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//    @Consumes(MediaType.APPLICATION_JSON)
    @GetExchange("{optionalFolderPath}job/{name}/{number}/api/json")
    BuildInfo buildInfo(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                        @PathVariable("name") String jobName,
                        @PathVariable("number") int buildNumber);

    // @Named("jobs:artifact")
//    @Consumes(MediaType.WILDCARD)
    @GetExchange("{optionalFolderPath}job/{name}/{number}/artifact/{relativeArtifactPath}")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    InputStream artifact(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                         @PathVariable("name") String jobName,
                         @PathVariable("number") int buildNumber,
                         @PathVariable("relativeArtifactPath") String relativeArtifactPath);

    // @Named("jobs:create")
    // @Fallback(JenkinsFallbacks.RequestStatusOnError.class)
//    @ResponseParser(RequestStatusParser.class)
//    @Produces(MediaType.APPLICATION_XML)
//    @Consumes(MediaType.WILDCARD)
    // @Payload("{configXML}")
    @PostExchange("{optionalFolderPath}createItem")
    ResponseEntity<Void> create(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                         @RequestParam("name") String jobName,
                         @RequestBody String configXML);

    // @Named("jobs:get-config")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//    @Consumes(MediaType.TEXT_PLAIN)
    @GetExchange("{optionalFolderPath}job/{name}/config.xml")
    String config(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                  @PathVariable("name") String jobName);

    // @Named("jobs:update-config")
    // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
//    @Produces(MediaType.APPLICATION_XML + ";charset=UTF-8")
//    @Consumes(MediaType.TEXT_HTML)
    // @Payload("{configXML}")
    @PostExchange("{optionalFolderPath}job/{name}/config.xml")
    boolean config(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                   @PathVariable("name") String jobName,
//                   @PayloadParam(value = "configXML")
                   String configXML);

    // @Named("jobs:get-description")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//   @Consumes(MediaType.TEXT_PLAIN)
    @GetExchange("{optionalFolderPath}job/{name}/description")
    String description(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                       @PathVariable("name") String jobName);

    // @Named("jobs:set-description")
    // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
//    @Consumes(MediaType.TEXT_HTML)
    @PostExchange("{optionalFolderPath}job/{name}/description")
    boolean description(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                        @PathVariable("name") String jobName,
                        @RequestParam("description") String description);

    // @Named("jobs:delete")
    // @Consumes(MediaType.TEXT_HTML)
    // @Fallback(JenkinsFallbacks.RequestStatusOnError.class)
    //@ResponseParser(RequestStatusParser.class)
    @PostExchange("{optionalFolderPath}job/{name}/doDelete")
    ResponseEntity<Void> delete(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                         @PathVariable("name") String jobName);

    // @Named("jobs:enable")
    @PostExchange("{optionalFolderPath}job/{name}/enable")
    // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
    // @Consumes(MediaType.TEXT_HTML)
    boolean enable(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                   @PathVariable("name") String jobName);

    // @Named("jobs:disable")
    @PostExchange("{optionalFolderPath}job/{name}/disable")
    // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
    // @Consumes(MediaType.TEXT_HTML)
    boolean disable(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                    @PathVariable("name") String jobName);

    // @Named("jobs:build")
    @PostExchange("{optionalFolderPath}job/{name}/build")
    // @Fallback(JenkinsFallbacks.IntegerResponseOnError.class)
    //@ResponseParser(LocationToQueueId.class)
    // @Consumes("application/unknown")
    IntegerResponse build(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                          @PathVariable("name") String jobName);

    // @Named("jobs:stop-build")
    @PostExchange("{optionalFolderPath}job/{name}/{number}/stop")
    // @Fallback(JenkinsFallbacks.RequestStatusOnError.class)
    //@ResponseParser(RequestStatusParser.class)
    // @Consumes(MediaType.APPLICATION_JSON)
    ResponseEntity<Void> stop(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                       @PathVariable("name") String jobName,
                       @PathVariable("number") int buildNumber);

    // @Named("jobs:term-build")
    @PostExchange("{optionalFolderPath}job/{name}/{number}/term")
    // @Fallback(JenkinsFallbacks.RequestStatusOnError.class)
    //@ResponseParser(RequestStatusParser.class)
    // @Consumes(MediaType.APPLICATION_JSON)
    ResponseEntity<Void> term(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                       @PathVariable("name") String jobName,
                       @PathVariable("number") int buildNumber);

    // @Named("jobs:kill-build")
    @PostExchange("{optionalFolderPath}job/{name}/{number}/kill")
    // @Fallback(JenkinsFallbacks.RequestStatusOnError.class)
    //@ResponseParser(RequestStatusParser.class)
    // @Consumes(MediaType.APPLICATION_JSON)
    ResponseEntity<Void> kill(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                       @PathVariable("name") String jobName,
                       @PathVariable("number") int buildNumber);

    // @Named("jobs:build-with-params")
    @PostExchange("{optionalFolderPath}job/{name}/buildWithParameters")
    // @Fallback(JenkinsFallbacks.IntegerResponseOnError.class)
    //@ResponseParser(LocationToQueueId.class)
    // @Consumes("application/unknown")
    IntegerResponse buildWithParameters(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                                        @PathVariable("name") String jobName,
                                        @Nullable @RequestBody Map<String, List<String>> properties);

    // @Named("jobs:last-build-number")
    @GetExchange("{optionalFolderPath}job/{name}/lastBuild/buildNumber")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    //@ResponseParser(BuildNumberToInteger.class)
    // @Consumes(MediaType.TEXT_PLAIN)

    Integer lastBuildNumber(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                            @PathVariable("name") String jobName);

    // @Named("jobs:last-build-timestamp")
    @GetExchange("{optionalFolderPath}job/{name}/lastBuild/buildTimestamp")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    // @Consumes(MediaType.TEXT_PLAIN)

    String lastBuildTimestamp(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                              @PathVariable("name") String jobName);

    // @Named("jobs:progressive-text")
    @GetExchange("{optionalFolderPath}job/{name}/lastBuild/logText/progressiveText")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    //@ResponseParser(OutputToProgressiveText.class)
    // @Consumes(MediaType.TEXT_PLAIN)

    ProgressiveText progressiveText(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                                    @PathVariable("name") String jobName,
                                    @RequestParam("start") int start);

    // @Named("jobs:progressive-text")
    @GetExchange("{optionalFolderPath}job/{name}/{number}/logText/progressiveText")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    //@ResponseParser(OutputToProgressiveText.class)
    // @Consumes(MediaType.TEXT_PLAIN)
    ProgressiveText progressiveText(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                                    @PathVariable("name") String jobName,
                                    @PathVariable("number") int buildNumber,
                                    @RequestParam("start") int start);

    // @Named("jobs:rename")
    @PostExchange("{optionalFolderPath}job/{name}/doRename")
    // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
    // @Consumes(MediaType.TEXT_HTML)
    boolean rename(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                   @PathVariable("name") String jobName,
                   @RequestParam("newName") String newName);

    // below four apis are for "pipeline-stage-view-plugin",
    // see https://github.com/jenkinsci/pipeline-stage-view-plugin/tree/master/rest-api
    // @Named("jobs:run-history")
    @GetExchange("{optionalFolderPath}job/{name}/wfapi/runs")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    // @Consumes(MediaType.APPLICATION_JSON)
    List<Workflow> runHistory(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                              @PathVariable("name") String jobName);

    // @Named("jobs:workflow")
    @GetExchange("{optionalFolderPath}job/{name}/{number}/wfapi/describe")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    // @Consumes(MediaType.APPLICATION_JSON)
    Workflow workflow(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                      @PathVariable("name") String jobName,
                      @PathVariable("number") int buildNumber);

    // @Named("jobs:pipeline-node")
    @GetExchange("{optionalFolderPath}job/{name}/{number}/execution/node/{nodeId}/wfapi/describe")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    // @Consumes(MediaType.APPLICATION_JSON)
    PipelineNode pipelineNode(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                              @PathVariable("name") String jobName,
                              @PathVariable("number") int buildNumber, @PathVariable("nodeId") int nodeId);

    //    @Named("jobs:pipeline-node-log")
//    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//    @Consumes(MediaType.APPLICATION_JSON)
    @GetExchange("{optionalFolderPath}job/{name}/{number}/execution/node/{nodeId}/wfapi/log")
    ResponseEntity<PipelineNodeLog> pipelineNodeLog(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                                                    @PathVariable("name") String jobName,
                                                    @PathVariable("number") int buildNumber, @PathVariable("nodeId") int nodeId);

    //    @Named("jobs:testReport")
//    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @GetExchange
    @GetExchange("${optionalFolderPath}job/{name}/{number}/testReport/api/json")
    ResponseEntity<JsonNode> testReport(@Nullable @PathVariable("optionalFolderPath") String optionalFolderPath,
                                        @PathVariable("name") String jobName,
                                        @PathVariable("number") int buildNumber);

}
