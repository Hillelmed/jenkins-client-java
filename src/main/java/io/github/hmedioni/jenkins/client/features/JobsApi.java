package io.github.hmedioni.jenkins.client.features;

import com.fasterxml.jackson.databind.*;
import io.github.hmedioni.jenkins.client.domain.job.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

import java.io.*;
import java.util.*;

//@RequestFilters(JenkinsAuthenticationFilter.class)
//@Path("/")
@HttpExchange(accept = MediaType.TEXT_XML_VALUE, contentType = MediaType.TEXT_XML_VALUE)
public interface JobsApi {

    //    // @Named("jobs:get-jobs")
//    @Path("{folderPath}api/json")
//    @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @GetExchange
    @GetExchange("/job/{folderPath}/api/json")
    ResponseEntity<JobList> jobList(@PathVariable("folderPath") String folderPath);

    @GetExchange("/api/json")
    ResponseEntity<JobList> jobList();

    // @Named("jobs:job-info")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//    @Consumes(MediaType.APPLICATION_JSON)
    @GetExchange("/job/{optionalFolderPath}/job/{name}/api/json")
    ResponseEntity<JobInfo> jobInfo(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                    @PathVariable("name") String jobName);

    @GetExchange("/job/{name}/api/json")
    ResponseEntity<JobInfo> jobInfo(@PathVariable("name") String jobName);

    @GetExchange("/job/{optionalFolderPath}/job/{name}/{number}/api/json")
    ResponseEntity<BuildInfo> buildInfo(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                        @PathVariable("name") String jobName,
                                        @PathVariable("number") int buildNumber);

    @GetExchange("/job/{name}/{number}/api/json")
    ResponseEntity<BuildInfo> buildInfo(@PathVariable("name") String jobName,
                                        @PathVariable("number") int buildNumber);

    // @Named("jobs:artifact")
//    @Consumes(MediaType.WILDCARD)
    @GetExchange("/job/{optionalFolderPath}/job/{name}/{number}/artifact/{relativeArtifactPath}")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    ResponseEntity<InputStream> artifact(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                         @PathVariable("name") String jobName,
                                         @PathVariable("number") int buildNumber,
                                         @PathVariable("relativeArtifactPath") String relativeArtifactPath);

    // @Named("jobs:create")
    // @Fallback(JenkinsFallbacks.ResponseEntity<Void>OnError.class)
//    @ResponseParser(ResponseEntity<Void>Parser.class)
//    @Produces(MediaType.APPLICATION_XML)
//    @Consumes(MediaType.WILDCARD)
    // @Payload("{configXML}")
    @PostExchange("/job/{optionalFolderPath}/createItem")
    ResponseEntity<Void> create(@PathVariable(name = "optionalFolderPath") String optionalFolderPath,
                                @RequestParam("name") String jobName,
                                @RequestBody String configXML);

    @PostExchange("/createItem")
    ResponseEntity<Void> create(@RequestParam("name") String jobName,
                                @RequestBody String configXML);

    // @Named("jobs:get-config")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//    @Consumes(MediaType.TEXT_PLAIN)
    @GetExchange("/job/{optionalFolderPath}/job/{name}/config.xml")
    String config(@PathVariable(value = "optionalFolderPath") String optionalFolderPath,
                  @PathVariable("name") String jobName);

    @GetExchange("/job/{name}/config.xml")
    String config(@PathVariable("name") String jobName);

    // @Named("jobs:update-config")
    // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
//    @Produces(MediaType.APPLICATION_XML + ";charset=UTF-8")
//    @Consumes(MediaType.TEXT_HTML)
    // @Payload("{configXML}")
    @PostExchange("/job/{optionalFolderPath}/job/{name}/config.xml")
    ResponseEntity<Void> pushConfig(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                    @PathVariable("name") String jobName,
                                    @RequestBody String configXML);

    @PostExchange("/job/{name}/config.xml")
    ResponseEntity<Void> pushConfig(@PathVariable("name") String jobName,
                                    @RequestBody String configXML);

    // @Named("jobs:get-description")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//   @Consumes(MediaType.TEXT_PLAIN)
    @GetExchange(value = "/job/{optionalFolderPath}/job/{name}/description", accept = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> description(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                       @PathVariable("name") String jobName);

    @GetExchange(value = "/job/{name}/description", accept = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> description(@PathVariable("name") String jobName);

    //    @Consumes(MediaType.TEXT_HTML)
    @PostExchange(value = "/job/{optionalFolderPath}/job/{name}/description", accept = MediaType.TEXT_HTML_VALUE
        , contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<Void> pushDescription(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                         @PathVariable("name") String jobName,
                                         @RequestParam("description") String description);

    @PostExchange(value = "/job/{name}/description", accept = MediaType.TEXT_HTML_VALUE
        , contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<Void> pushDescription(@PathVariable("name") String jobName,
                                         @RequestParam(value = "description") String description);


    // @Named("jobs:set-description")
    // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
//    @Consumes(MediaType.TEXT_HTML)

    // @Named("jobs:delete")
    // @Consumes(MediaType.TEXT_HTML)
    // @Fallback(JenkinsFallbacks.ResponseEntity<Void>OnError.class)
    //@ResponseParser(ResponseEntity<Void>Parser.class)
    @PostExchange("/job/{optionalFolderPath}/job/{name}/doDelete")
    ResponseEntity<Void> delete(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                @PathVariable("name") String jobName);

    @PostExchange("/job/{name}/doDelete")
    ResponseEntity<Void> delete(@PathVariable("name") String jobName);

    // @Named("jobs:enable")
    @PostExchange("/job/{optionalFolderPath}/job/{name}/enable")
    // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
    // @Consumes(MediaType.TEXT_HTML)
    ResponseEntity<Void> enable(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                @PathVariable("name") String jobName);

    @PostExchange("/job/{name}/enable")
        // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
        // @Consumes(MediaType.TEXT_HTML)
    ResponseEntity<Void> enable(@PathVariable("name") String jobName);

    // @Named("jobs:disable")
    @PostExchange("/job/{optionalFolderPath}/job/{name}/disable")
    // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
    // @Consumes(MediaType.TEXT_HTML)
    ResponseEntity<Void> disable(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                 @PathVariable("name") String jobName);

    @PostExchange("/job/{name}/disable")
        // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
        // @Consumes(MediaType.TEXT_HTML)
    ResponseEntity<Void> disable(@PathVariable("name") String jobName);

    // @Named("jobs:build")
    @PostExchange("/job/{optionalFolderPath}/job/{name}/build")
    ResponseEntity<Void> build(@PathVariable("optionalFolderPath") String optionalFolderPath,
                               @PathVariable("name") String jobName);

    @PostExchange("/job/{name}/build")
    ResponseEntity<Void> build(@PathVariable("name") String jobName);

    @PostExchange("/job/{optionalFolderPath}/job/{name}/{number}/stop")
    ResponseEntity<Void> stop(@PathVariable("optionalFolderPath") String optionalFolderPath,
                              @PathVariable("name") String jobName,
                              @PathVariable("number") int buildNumber);

    @PostExchange("/job/{name}/{number}/stop")
    ResponseEntity<Void> stop(@PathVariable("name") String jobName,
                              @PathVariable("number") int buildNumber);

    @PostExchange("/job/{optionalFolderPath}/job/{name}/{number}/term")
    ResponseEntity<Void> term(@PathVariable("optionalFolderPath") String optionalFolderPath,
                              @PathVariable("name") String jobName,
                              @PathVariable("number") int buildNumber);

    @PostExchange("/job/{name}/{number}/term")
    ResponseEntity<Void> term(@PathVariable("name") String jobName,
                              @PathVariable("number") int buildNumber);

    // @Named("jobs:kill-build")
    @PostExchange("/job/{optionalFolderPath}/job/{name}/{number}/kill")
    ResponseEntity<Void> kill(@PathVariable("optionalFolderPath") String optionalFolderPath,
                              @PathVariable("name") String jobName,
                              @PathVariable("number") int buildNumber);

    @PostExchange("/job/{name}/{number}/kill")
    ResponseEntity<Void> kill(@PathVariable("name") String jobName,
                              @PathVariable("number") int buildNumber);

    // @Named("jobs:build-with-params")
    @PostExchange("/job/{optionalFolderPath}/job/{name}/buildWithParameters")
    ResponseEntity<Void> buildWithParameters(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                             @PathVariable("name") String jobName,
                                             @RequestParam Map<String, List<String>> properties);

    @PostExchange(value = "/job/{name}/buildWithParameters", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<Void> buildWithParameters(@PathVariable("name") String jobName,
                                             @RequestParam Map<String, List<String>> properties);

    @PostExchange("/job/{optionalFolderPath}/job/{name}/buildWithParameters")
    ResponseEntity<Void> buildWithParameters(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                             @PathVariable("name") String jobName);

    @PostExchange(value = "/job/{name}/buildWithParameters", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<Void> buildWithParameters(@PathVariable("name") String jobName);

    // @Named("jobs:last-build-number")
    @GetExchange(value = "/job/{optionalFolderPath}/job/{name}/lastBuild/buildNumber", accept = "text/plain;charset=US-ASCII")
    ResponseEntity<String> lastBuildNumber(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                           @PathVariable("name") String jobName);

    @GetExchange(value = "/job/{name}/lastBuild/buildNumber", accept = "text/plain;charset=US-ASCII")
    ResponseEntity<String> lastBuildNumber(@PathVariable("name") String jobName);

    @GetExchange("/job/{optionalFolderPath}/job/{name}/lastBuild/buildTimestamp")
    String lastBuildTimestamp(@PathVariable("optionalFolderPath") String optionalFolderPath,
                              @PathVariable("name") String jobName);

    @GetExchange("/job/{name}/lastBuild/buildTimestamp")
    String lastBuildTimestamp(@PathVariable("name") String jobName);

    // @Named("jobs:progressive-text")
    @GetExchange("/job/{optionalFolderPath}/job/{name}/lastBuild/logText/progressiveText")
    ResponseEntity<String> progressiveText(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                           @PathVariable("name") String jobName,
                                           @RequestParam("start") int start);

    @GetExchange("/job/{name}/lastBuild/logText/progressiveText")
    ResponseEntity<String> progressiveText(@PathVariable("name") String jobName,
                                           @RequestParam("start") int start);

    @GetExchange("/job/{optionalFolderPath}/job/{name}/{number}/logText/progressiveText")
    ResponseEntity<String> progressiveText(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                           @PathVariable("name") String jobName,
                                           @PathVariable("number") int buildNumber,
                                           @RequestParam("start") int start);

    @GetExchange("/job/{name}/{number}/logText/progressiveText")
    ResponseEntity<String> progressiveText(@PathVariable("name") String jobName,
                                           @PathVariable("number") int buildNumber,
                                           @RequestParam("start") int start);

    // @Named("jobs:rename")
    @PostExchange("/job/{optionalFolderPath}/job/{name}/doRename")
    // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
    // @Consumes(MediaType.TEXT_HTML)
    ResponseEntity<Boolean> rename(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                   @PathVariable("name") String jobName,
                                   @RequestParam("newName") String newName);

    @PostExchange("/job/{name}/doRename")
        // @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
        // @Consumes(MediaType.TEXT_HTML)
    ResponseEntity<Boolean> rename(@PathVariable("name") String jobName,
                                   @RequestParam("newName") String newName);

    // below four apis are for "pipeline-stage-view-plugin",
    // see https://github.com/jenkinsci/pipeline-stage-view-plugin/tree/master/rest-api
    // @Named("jobs:run-history")
    @GetExchange("/job/{optionalFolderPath}/job/{name}/wfapi/runs")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    // @Consumes(MediaType.APPLICATION_JSON)
    List<Workflow> runHistory(@PathVariable("optionalFolderPath") String optionalFolderPath,
                              @PathVariable("name") String jobName);

    // @Named("jobs:workflow")
    @GetExchange("/job/{optionalFolderPath}/job/{name}/{number}/wfapi/describe")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    // @Consumes(MediaType.APPLICATION_JSON)
    Workflow workflow(@PathVariable("optionalFolderPath") String optionalFolderPath,
                      @PathVariable("name") String jobName,
                      @PathVariable("number") int buildNumber);

    // @Named("jobs:pipeline-node")
    @GetExchange("/job/{optionalFolderPath}/job/{name}/{number}/execution/node/{nodeId}/wfapi/describe")
    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
    // @Consumes(MediaType.APPLICATION_JSON)
    PipelineNode pipelineNode(@PathVariable("optionalFolderPath") String optionalFolderPath,
                              @PathVariable("name") String jobName,
                              @PathVariable("number") int buildNumber, @PathVariable("nodeId") int nodeId);

    //    @Named("jobs:pipeline-node-log")
//    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//    @Consumes(MediaType.APPLICATION_JSON)
    @GetExchange("/job/{optionalFolderPath}/job/{name}/{number}/execution/node/{nodeId}/wfapi/log")
    ResponseEntity<PipelineNodeLog> pipelineNodeLog(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                                    @PathVariable("name") String jobName,
                                                    @PathVariable("number") int buildNumber, @PathVariable("nodeId") int nodeId);

    //    @Named("jobs:testReport")
//    // @Fallback(Fallbacks.NullOnNotFoundOr404.class)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @GetExchange
    @GetExchange("/job/{optionalFolderPath}/job/{name}/{number}/testReport/api/json")
    ResponseEntity<JsonNode> testReport(@PathVariable("optionalFolderPath") String optionalFolderPath,
                                        @PathVariable("name") String jobName,
                                        @PathVariable("number") int buildNumber);

}
