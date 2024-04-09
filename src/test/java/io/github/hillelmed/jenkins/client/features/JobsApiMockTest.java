package io.github.hillelmed.jenkins.client.features;

import com.fasterxml.jackson.databind.*;
import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.domain.common.*;
import io.github.hillelmed.jenkins.client.domain.job.*;
import io.github.hillelmed.jenkins.client.exception.*;




import okhttp3.mockwebserver.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.testng.Assert.*;


@Test(groups = "unit")
public class JobsApiMockTest extends BaseJenkinsMockTest {

    public void testGetInnerFolderJobList() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/jobsInJenkinsFolder.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            JobList output = api.jobList("Folder1/job/Folder 2").getBody();
            assertNotNull(output);
            assertNotNull(output.getJobs());
            assertEquals(output.getJobs().size(), 1);
            assertEquals(output.getJobs().get(0), new Job("hudson.model.FreeStyleProject", "Test Project", "http://localhost:8080/job/username", null));
            assertSent(server, "GET", "/job/Folder1/job/Folder%202/api/json");
        } finally {
            server.shutdown();
        }
    }

    public void testGetRootFolderJobList() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/jobsInRootFolder.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            JobList output = api.jobList().getBody();
            assertNotNull(output);
            assertNotNull(output.getJobs());
            assertEquals(output.getJobs().size(), 6);
            assertSent(server, "GET", "/api/json");
        } finally {
            server.shutdown();
        }
    }

    public void testGetJobInfo() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/job-info.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            JobInfo output = api.jobInfo("fish").getBody();
            assertNotNull(output);
            assertEquals(output.getName(), "fish");
            assertEquals(output.getBuilds().size(), 7);
            assertSent(server, "GET", "/job/fish/api/json");
        } finally {
            server.shutdown();
        }
    }

    public void testGetJobInfoNotFound() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            JobInfo output = api.jobInfo("fish").getBody();
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSent(server, "GET", "/job/fish/api/json");
        } finally {
            server.shutdown();
        }
    }

    public void testGetBuildInfo() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/build-info.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            BuildInfo output = api.buildInfo("fish", 10).getBody();
            assertNotNull(output);
            assertEquals(output.getFullDisplayName(), "fish #10");
            assertEquals(output.getArtifacts().size(), 1);
            assertEquals(output.getActions().size(), 5);
            assertEquals(output.getActions().get(2).getText(), "<strong>There could be HTML text here</strong>");
            assertEquals(output.getActions().get(2).getIconPath(), "clipboard.png");
            assertEquals(output.getActions().get(2).getClazz(), "com.jenkinsci.plugins.badge.action.BadgeSummaryAction");
            assertNull(output.getActions().get(3).getText());
            assertEquals(output.getActions().get(4).getClazz(), "org.jenkinsci.plugins.displayurlapi.actions.RunDisplayAction");
            assertSent(server, "GET", "/job/fish/10/api/json");
        } finally {
            server.shutdown();
        }
    }

    public void testGetBuildInfoNotFound() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            api.buildInfo("fish", 10).getBody();
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSent(server, "GET", "/job/fish/10/api/json");
        } finally {
            server.shutdown();
        }
    }

    public void testCreateJob() throws Exception {
        MockWebServer server = mockWebServer();

        String configXML = payloadFromResource("/freestyle-project.xml");
        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> success = api.create("DevTest", configXML);
            assertNotNull(success);
            assertSentWithXMLFormDataAccept(server, "POST", "/createItem?name=DevTest", configXML, MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testCreateJobInFolder() throws Exception {
        MockWebServer server = mockWebServer();

        String configXML = payloadFromResource("/freestyle-project.xml");
        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> success = api.create("test-folder", "JobInFolder", configXML);
            assertNotNull(success);
            assertSentWithXMLFormDataAccept(server, "POST", "/job/test-folder/createItem?name=JobInFolder", configXML, MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testSimpleFolderPathWithLeadingAndTrailingForwardSlashes() throws Exception {
        MockWebServer server = mockWebServer();

        String configXML = payloadFromResource("/freestyle-project.xml");
        server.enqueue(new MockResponse().setResponseCode(200));

        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> success = api.create("test-folder/job/test-folder-1", "JobInFolder", configXML);
            assertNotNull(success);
            assertSentWithXMLFormDataAccept(server, "POST", "/job/test-folder/job/test-folder-1/createItem?name=JobInFolder"
                , configXML, MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testCreateJobAlreadyExists() throws Exception {
        MockWebServer server = mockWebServer();

        String configXML = payloadFromResource("/freestyle-project.xml");
        String errorMessage = "A job already exists with the name ?DevTest?";
        server.enqueue(new MockResponse().setHeader("X-Error", errorMessage)
            .setResponseCode(400));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            api.create("DevTest", configXML);
        } catch (JenkinsAppException e) {
            assertNotNull(e);
            assertFalse(e.errors().isEmpty());
            assertEquals(e.errors().get(1).getMessage(), errorMessage);
        } finally {
            server.shutdown();
        }
        assertSentWithXMLFormDataAccept(server, "POST", "/createItem?name=DevTest", configXML, MediaType.TEXT_XML_VALUE);

    }

    public void testGetDescription() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(new MockResponse().setBody("whatever").setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            String output = api.description("DevTest").getBody();
            assertNotNull(output);
            assertEquals(output, "whatever");
            assertSentAcceptText(server, "GET", "/job/DevTest/description");
        } finally {
            server.shutdown();
        }
    }

    public void testGetDescriptionNonExistentJob() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            String output = api.description("DevTest").getBody();
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSentAcceptText(server, "GET", "/job/DevTest/description");
        } finally {
            server.shutdown();
        }
    }

    public void testUpdateDescription() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> success = api.pushDescription("DevTest", "whatever");
            assertTrue(success.getStatusCode().is2xxSuccessful());
            assertSentWithFormData(server, "POST", "/job/DevTest/description", "description=whatever",
                MediaType.TEXT_HTML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testUpdateDescriptionNonExistentJob() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> success = api.pushDescription("DevTest", "whatever");
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSentWithFormData(server, "POST", "/job/DevTest/description", "description=whatever",
                MediaType.TEXT_HTML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testGetConfig() throws Exception {
        MockWebServer server = mockWebServer(false);

        String configXML = payloadFromResource("/freestyle-project.xml");
        server.enqueue(new MockResponse().setBody(configXML).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            String output = api.config("DevTest").getBody();
            assertNotNull(output);
            assertEquals(output, configXML);
            assertSentAcceptText(server, "GET", "/job/DevTest/config.xml");
        } finally {
            server.shutdown();
        }
    }

    public void testGetConfigNonExistentJob() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            String output = api.config("DevTest").getBody();
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSentAcceptText(server, "GET", "/job/DevTest/config.xml");
        } finally {
            server.shutdown();
        }
    }

    public void testUpdateConfig() throws Exception {
        MockWebServer server = mockWebServer();

        String configXML = payloadFromResource("/freestyle-project.xml");
        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> success = api.pushConfig("DevTest", configXML);
            assertTrue(success.getStatusCode().is2xxSuccessful());
            assertSentAccept(server, "POST", "/job/DevTest/config.xml", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testUpdateConfigNonExistentJob() throws Exception {
        MockWebServer server = mockWebServer();

        String configXML = payloadFromResource("/freestyle-project.xml");
        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            api.pushConfig("DevTest", configXML);
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSentAccept(server, "POST", "/job/DevTest/config.xml", MediaType.TEXT_HTML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testDeleteJob() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> success = api.delete("DevTest");
            assertNotNull(success);
            assertSentAccept(server, "POST", "/job/DevTest/doDelete", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testDeleteJobNonExistent() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(400));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            api.delete("DevTest");
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSentAccept(server, "POST", "/job/DevTest/doDelete", MediaType.TEXT_HTML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testEnableJob() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> success = api.enable("DevTest");
            assertTrue(success.getStatusCode().is2xxSuccessful());
            assertSentAccept(server, "POST", "/job/DevTest/enable", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testEnableJobAlreadyEnabled() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> success = api.enable("DevTest");
            assertTrue(success.getStatusCode().is2xxSuccessful());
            assertSentAccept(server, "POST", "/job/DevTest/enable", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testDisableJob() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> success = api.disable("DevTest");
            assertTrue(success.getStatusCode().is2xxSuccessful());
            assertSentAccept(server, "POST", "/job/DevTest/disable", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testDisableJobAlreadyEnabled() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> success = api.disable("DevTest");
            assertTrue(success.getStatusCode().is2xxSuccessful());
            assertSentAccept(server, "POST", "/job/DevTest/disable", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testBuildJob() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(
            new MockResponse().setHeader("Location", "http://127.0.1.1:8080/queue/item/1/").setResponseCode(201));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> output = api.build("DevTest");
            assertNotNull(output);
            IntegerResponse integerResponse = JenkinsUtils.getQueueItemIntegerResponse(output.getHeaders());
            assertEquals((int) integerResponse.getValues(), 1);
            assertSentAccept(server, "POST", "/job/DevTest/build", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testBuildJobWithNoLocationReturned() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(
            new MockResponse().setResponseCode(201));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> output = api.build("DevTest");
            assertNotNull(output);
            IntegerResponse response = JenkinsUtils.getQueueItemIntegerResponse(output.getHeaders());
            assertEquals(response.getValues(), -1);
            assertSentAccept(server, "POST", "/job/DevTest/build", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testBuildJobNonExistentJob() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> output = api.build("DevTest");
            assertNotNull(output);
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSentAccept(server, "POST", "/job/DevTest/build", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testBuildJobWithParams() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(
            new MockResponse().setHeader("Location", "http://127.0.1.1:8080/queue/item/1/").setResponseCode(201));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            Map<String, List<String>> params = new HashMap<>();
            params.put("SomeKey", List.of("SomeVeryNewValue"));
            ResponseEntity<Void> output = api.buildWithParameters("DevTest", params);
            assertNotNull(output);
            IntegerResponse response = JenkinsUtils.getQueueItemIntegerResponse(output.getHeaders());
            assertEquals((int) response.getValues(), 1);
            assertSentAccept(server, "POST", "/job/DevTest/buildWithParameters", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testBuildJobWithNullParamsMap() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(
            new MockResponse().setHeader("Location", "http://127.0.1.1:8080/queue/item/1/").setResponseCode(201));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> output = api.buildWithParameters("DevTest");
            assertNotNull(output);
            IntegerResponse response = JenkinsUtils.getQueueItemIntegerResponse(output.getHeaders());
            assertEquals((int) response.getValues(), 1);
            assertSentAccept(server, "POST", "/job/DevTest/buildWithParameters", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testBuildJobWithEmptyParamsMap() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(
            new MockResponse().setHeader("Location", "http://127.0.1.1:8080/queue/item/1/").setResponseCode(201));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> output = api.buildWithParameters("DevTest", new HashMap<>());
            assertNotNull(output);
            IntegerResponse response = JenkinsUtils.getQueueItemIntegerResponse(output.getHeaders());
            assertEquals((int) response.getValues(), 1);
            assertSentAccept(server, "POST", "/job/DevTest/buildWithParameters", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testGetJobListWithDepth() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/getJobListByDepth.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            JobListTree output = api.jobList( 0, null).getBody();
            assertNotNull(output);
            assertNotNull(output.getJobs());
            assertEquals(output.getJobs().size(), 1);
            assertEquals(output.getJobs().get(0), new JobListTree("hudson.model.FreeStyleProject", "DevTest", null, null, "notbuilt", "http://localhost:8080/job/DevTest/"));
            assertSent(server, "GET", "/api/json", Map.of("depth", "0"));
        } finally {
            server.shutdown();
        }
    }

    public void testGetJobListWithTreeByFullName() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/jobsInJenkinsFolderByFullName.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        Map<String, String> queryParams = Map.of("tree", "jobs[fullName]");
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            JobListTree output = api.jobList("Folder1/job/Folder 2", null, "jobs[fullName]").getBody();
            assertNotNull(output);
            assertNotNull(output.getJobs());
            assertEquals(output.getJobs().size(), 1);
            assertEquals(output.getJobs().get(0), new JobListTree("hudson.model.FreeStyleProject", null, "Test Project", null, null, null));
            assertSent(server, "GET", "/job/Folder1/job/Folder%202/api/json", queryParams);
        } finally {
            server.shutdown();
        }
    }

    public void testGetNestedJobList() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/nestedJobList.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        Map<String, String> queryParams = Map.of("tree", "jobs[fullName,jobs[fullName]]");
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            JobListTree output = api.jobList("Folder1/job/Folder 2", null, "jobs[fullName,jobs[fullName]]").getBody();
            assertNotNull(output);
            assertNotNull(output.getJobs());
            assertEquals(output.getJobs().size(), 2);
            assertEquals(output.getJobs().get(0), new JobListTree("hudson.model.FreeStyleProject", null, "DevTest", null, null, null));
            JobListTree actualFolder = output.getJobs().get(1);
            assertEquals(actualFolder.getClazz(), "com.cloudbees.hudson.plugins.folder.Folder");
            assertEquals(actualFolder.getFullName(), "test-folder");
            assertEquals(actualFolder.getJobs().size(), 1);
            assertEquals(actualFolder.getJobs().get(0).getFullName(), "test-folder/test-folder-1");
            assertSent(server, "GET", "/job/Folder1/job/Folder%202/api/json", queryParams);
        } finally {
            server.shutdown();
        }
    }


    public void testBuildJobWithParamsNonExistentJob() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            Map<String, List<String>> params = new HashMap<>();
            params.put("SomeKey", List.of("SomeVeryNewValue"));
            ResponseEntity<Void> output = api.buildWithParameters("DevTest", params);
            assertNotNull(output);
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSentAccept(server, "POST", "/job/DevTest/buildWithParameters", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testGetParams() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/build-info.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            List<Parameter> output = Objects.requireNonNull(api.buildInfo("fish", 10).getBody()).getActions().get(0).getParameters();
            assertNotNull(output);
            assertEquals(output.get(0).getName(), "bear");
            assertEquals(output.get(0).getValue(), "true");
            assertEquals(output.get(1).getName(), "fish");
            assertEquals(output.get(1).getValue(), "salmon");
            assertSent(server, "GET", "/job/fish/10/api/json");
        } finally {
            server.shutdown();
        }
    }

    public void testGetGitCommitInfo() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/build-info-git-commit.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            List<ChangeSet> changeSets = api.buildInfo("fish", 10).getBody().getChangeSets().get(0).getItems();
            assertNotNull(changeSets);
            assertEquals(changeSets.get(0).getAffectedPaths().get(0), "some/path/in/the/repository");
            assertEquals(changeSets.get(0).getCommitId(), "d27afa0805201322d846d7defc29b82c88d9b5ce");
            assertEquals(changeSets.get(0).getTimestamp(), 1461091892486L);
            assertEquals(changeSets.get(0).getAuthor().getAbsoluteUrl(), "http://localhost:8080/user/username");
            assertEquals(changeSets.get(0).getAuthor().getFullName(), "username");
            assertEquals(changeSets.get(0).getAuthorEmail(), "username@localhost");
            assertEquals(changeSets.get(0).getComment(), "Commit comment\n");
        } finally {
            server.shutdown();
        }
    }

    public void testGetParamsWhenNoBuildParams() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/build-info-no-params.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            BuildInfo buildInfo = api.buildInfo("fish", 10).getBody();
            assertNotNull(buildInfo);
            assertEquals(buildInfo.getActions().size(), 9);
            List<Parameter> output = buildInfo.getActions().get(0).getParameters();
            assertNull(output);
            assertSent(server, "GET", "/job/fish/10/api/json");
        } finally {
            server.shutdown();
        }
    }

    public void testGetParamsWhenEmptyorNullParams() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/build-info-empty-and-null-params.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            List<Parameter> output = api.buildInfo("fish", 10).getBody().getActions().get(0).getParameters();
            assertNotNull(output);
            assertEquals(output.get(0).getName(), "bear");
            assertEquals(output.get(0).getValue(), "null");
            assertEquals(output.get(1).getName(), "fish");
            assertTrue(output.get(1).getValue().isEmpty());
            assertSent(server, "GET", "/job/fish/10/api/json");
        } finally {
            server.shutdown();
        }
    }

    public void testGetCause() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/build-info-no-params.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            List<Cause> output = api.buildInfo("fish", 10).getBody().getActions().get(0).getCauses();
            assertNotNull(output);
            assertEquals(output.get(0).getShortDescription(), "Started by user anonymous");
            assertNull(output.get(0).getUserId());
            assertEquals(output.get(0).getUserName(), "anonymous");
            assertSent(server, "GET", "/job/fish/10/api/json");
        } finally {
            server.shutdown();
        }
    }

    public void testGetLastBuildNumber() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/build-number.txt");
        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            Integer output = Integer.valueOf(Objects.requireNonNull(api.lastBuildNumber("DevTest").getBody()));
            assertNotNull(output);
            assertEquals((int) output, 123);
            assertSentAcceptText(server, "GET", "/job/DevTest/lastBuild/buildNumber");
        } finally {
            server.shutdown();
        }
    }

    public void testGetLastBuildNumberJobNotExist() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            Integer output = Integer.valueOf(Objects.requireNonNull(api.lastBuildNumber("DevTest").getBody()));
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSentAcceptText(server, "GET", "/job/DevTest/lastBuild/buildNumber");
        } finally {
            server.shutdown();
        }
    }

    public void testGetLastBuildTimeStamp() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/build-timestamp.txt");
        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            String output = api.lastBuildTimestamp("DevTest");
            assertNotNull(output);
            assertEquals(body, output);
            assertSentAcceptText(server, "GET", "/job/DevTest/lastBuild/buildTimestamp");
        } finally {
            server.shutdown();
        }
    }

    public void testGetLastBuildTimeStampJobNotExist() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            api.lastBuildTimestamp("DevTest");
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSentAcceptText(server, "GET", "/job/DevTest/lastBuild/buildTimestamp");
        } finally {
            server.shutdown();
        }
    }

    public void testGetProgressiveText() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/progressive-text.txt");
        server.enqueue(new MockResponse().setHeader("X-Text-Size", "123").setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<String> output = api.progressiveText("DevTest", 0);
            assertNotNull(output);
            assertEquals(JenkinsUtils.getTextSize(output.getHeaders()), 123);
            assertSentAcceptText(server, "GET", "/job/DevTest/lastBuild/logText/progressiveText?start=0");
        } finally {
            server.shutdown();
        }
    }

    public void testGetProgressiveTextOfBuildNumber() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/progressive-text.txt");
        server.enqueue(new MockResponse().setHeader("X-Text-Size", "123").setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<String> output = api.progressiveText("DevTest", 1, 0);
            assertNotNull(output);
            assertEquals(JenkinsUtils.getTextSize(output.getHeaders()), 123);
            assertFalse(JenkinsUtils.hasMoreData(output.getHeaders()));
            assertSentAcceptText(server, "GET", "/job/DevTest/1/logText/progressiveText?start=0");
        } finally {
            server.shutdown();
        }
    }

    public void testGetProgressiveTextOfBuildNumberWithHasMoreData() throws Exception {
        MockWebServer server = mockWebServer(false);

        String body = payloadFromResource("/progressive-text.txt");
        server.enqueue(new MockResponse().setHeader("X-Text-Size", "8999").setHeader("X-More-Data", "true").setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<String> output = api.progressiveText("DevTest", 1, 0);
            assertNotNull(output);
            assertEquals(JenkinsUtils.getTextSize(output.getHeaders()), 8999);
            assertTrue(JenkinsUtils.hasMoreData(output.getHeaders()));
            assertSentAcceptText(server, "GET", "/job/DevTest/1/logText/progressiveText?start=0");
        } finally {
            server.shutdown();
        }
    }


    public void testGetProgressiveTextJobNotExist() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<String> output = api.progressiveText("DevTest", 0);
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSentAcceptText(server, "GET", "/job/DevTest/lastBuild/logText/progressiveText?start=0");
        } finally {
            server.shutdown();
        }
    }

    public void testRenameJob() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(new MockResponse().setResponseCode(302));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> success = api.rename("DevTest", "NewDevTest");
            assertEquals(success.getStatusCode(), HttpStatus.FOUND);
            assertSentAccept(server, "POST", "/job/DevTest/doRename?newName=NewDevTest", MediaType.TEXT_XML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testRenameJobNotExist() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            api.rename("DevTest", "NewDevTest");
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSentAccept(server, "POST", "/job/DevTest/doRename?newName=NewDevTest", MediaType.TEXT_HTML_VALUE);
        } finally {
            server.shutdown();
        }
    }

    public void testRunHistory() throws Exception {
        MockWebServer server = mockWebServer(false);
        String body = payloadFromResource("/runHistory.json");

        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            List<Workflow> workflows = api.runHistory("MockJob");
            assertNotNull(workflows);
            assertSent(server, "GET", "/job/MockJob/wfapi/runs");
        } finally {
            server.shutdown();
        }
    }

    public void testRunHistoryNotExist() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            List<Workflow> workflows = api.runHistory("MockJob");
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSent(server, "GET", "/job/MockJob/wfapi/runs");
        } finally {
            server.shutdown();
        }
    }

    public void testWorkflow() throws Exception {
        MockWebServer server = mockWebServer();
        String body = payloadFromResource("/workflow.json");

        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            Workflow success = api.workflow("DevTest", 16);
            assertNotNull(success);
            assertSent(server, "GET", "/job/DevTest/16/wfapi/describe");
        } finally {
            server.shutdown();
        }
    }

    public void testWorkflowNotExist() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            api.workflow("DevTest", 16);
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSent(server, "GET", "/job/DevTest/16/wfapi/describe");
        } finally {
            server.shutdown();
        }
    }

    public void testPipelineNode() throws Exception {
        MockWebServer server = mockWebServer();
        String body = payloadFromResource("/pipeline-node.json");

        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            PipelineNode success = api.pipelineNode("DevTest", 16, 17);
            assertNotNull(success);
            assertSent(server, "GET", "/job/DevTest/16/execution/node/17/wfapi/describe");
        } finally {
            server.shutdown();
        }
    }

    public void testPipelineNodeNotExist() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            PipelineNode success = api.pipelineNode("DevTest", 16, 17);
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSent(server, "GET", "/job/DevTest/16/execution/node/17/wfapi/describe");
        } finally {
            server.shutdown();
        }
    }

    public void testJobTestReportExists() throws Exception {
        MockWebServer server = mockWebServer(false);

        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("{ \"empty\": false }").setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            JsonNode testReport = api.testReport("DevTest", 16).getBody();
            assertNotNull(testReport);
            assertFalse(testReport.get("empty").booleanValue());
        } finally {
            server.shutdown();
        }
    }

    public void testJobTestReportNotExists() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            JsonNode testReport = api.testReport("DevTest", 16).getBody();
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
        } finally {
            server.shutdown();
        }
    }

    public void testPipelineNodeLog() throws Exception {
        MockWebServer server = mockWebServer();
        String body = payloadFromResource("/pipelineNodeLog.json");

        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            PipelineNodeLog pipelineNodeLog = api.pipelineNodeLog("MockJob", 16, 17).getBody();
            assertNotNull(pipelineNodeLog);
            assertSent(server, "GET", "/job/MockJob/16/execution/node/17/wfapi/log");
        } finally {
            server.shutdown();
        }
    }

    public void testPipelineNodeLogNotExist() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            PipelineNodeLog pipelineNodeLog = api.pipelineNodeLog("MockJob", 16, 17).getBody();
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSent(server, "GET", "/job/MockJob/16/execution/node/17/wfapi/log");
        } finally {
            server.shutdown();
        }
    }

    public void testStopBuild() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> status = api.stop("fish", 99);
            assertNotNull(status);
            assertTrue(status.getStatusCode().is2xxSuccessful());
            assertSent(server, "POST", "/job/fish/99/stop");
        } finally {
            server.shutdown();
        }
    }

    public void testTermBuild() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> status = api.term("fish", 99);
            assertNotNull(status);
            assertSent(server, "POST", "/job/fish/99/term");
        } finally {
            server.shutdown();
        }
    }

    public void testKillBuild() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse().setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> status = api.kill("fish", 99);
            assertNotNull(status);
            assertSent(server, "POST", "/job/fish/99/kill");
        } finally {
            server.shutdown();
        }
    }

    public void testKillBuildReturns404() throws Exception {
        MockWebServer server = mockWebServer();

        server.enqueue(new MockResponse()
            .setHeader("Location", "http://localhost:" + server.getPort() + "job/fish/99/kill/")
            .setResponseCode(302));
        server.enqueue(new MockResponse().setResponseCode(404));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            JobsApi api = jenkinsApi.jobsApi();
            ResponseEntity<Void> status = api.kill("fish", 99);
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertSent(server, "POST", "/job/fish/99/kill");
            assertNotNull(e);
            assertFalse(e.errors().isEmpty());
            assertEquals(e.errors().size(), 1);
            assertEquals(e.errors().get(0).getMessage(), "The kill operation does not exist for " +
                "http://localhost:" + server.getPort() +
                "job/fish/99/kill/, try stop instead.");
        } finally {
            server.shutdown();
        }
    }
}
