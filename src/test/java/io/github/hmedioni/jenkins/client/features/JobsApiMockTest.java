//package io.github.hmedioni.jenkins.client.features;
//
//import io.github.hmedioni.jenkins.client.*;
//import io.github.hmedioni.jenkins.client.domain.common.*;
//import io.github.hmedioni.jenkins.client.domain.job.*;
//import okhttp3.mockwebserver.*;
//import org.springframework.http.*;
//import org.testng.annotations.*;
//
//import java.util.*;
//
//import static org.testng.Assert.*;
//
///**
// * Mock tests for the {@link JobsApi} class.
// */
//@Test(groups = "unit", testName = "JobsApiMockTest")
//public class JobsApiMockTest extends BaseJenkinsMockTest {
//
//    public void testGetInnerFolderJobList() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/jobsInJenkinsFolder.json");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            JobList output = api.jobList("Folder1/Folder 2");
//            assertNotNull(output);
//            assertNotNull(output.getJobs());
//            assertEquals(output.getJobs().size(), 1);
//            assertEquals(output.getJobs().get(0), Job.create("hudson.model.FreeStyleProject", "Test Project", "http://localhost:8080/job/username", null));
//            assertSent(server, "GET", "/job/Folder1/job/Folder%202/api/json");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetRootFolderJobList() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/jobsInRootFolder.json");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            JobList output = api.jobList("");
//            assertNotNull(output);
//            assertNotNull(output.getJobs());
//            assertEquals(output.getJobs().size(), 6);
//            assertSent(server, "GET", "/api/json");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetJobInfo() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/job-info.json");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            JobInfo output = api.jobInfo( "fish");
//            assertNotNull(output);
//            assertEquals(output.getName(), "fish");
//            assertEquals(output.builds().size(), 7);
//            assertSent(server, "GET", "/job/fish/api/json");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetJobInfoNotFound() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            JobInfo output = api.jobInfo( "fish");
//            assertNull(output);
//            assertSent(server, "GET", "/job/fish/api/json");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetBuildInfo() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/build-info.json");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            BuildInfo output = api.buildInfo( "fish", 10);
//            assertNotNull(output);
//            assertEquals(output.fullDisplayName(), "fish #10");
//            assertEquals(output.artifacts().size(), 1);
//            assertEquals(output.getActions().size(), 5);
//            assertEquals(output.getActions().get(2).getText(), "<strong>There could be HTML text here</strong>");
//            assertEquals(output.getActions().get(2).getIconPath(), "clipboard.png");
//            assertEquals(output.getActions().get(2)._class(), "com.jenkinsci.plugins.badge.action.BadgeSummaryAction");
//            assertEquals(output.getActions().get(3).getText(), null);
//            assertEquals(output.getActions().get(4)._class(), "org.jenkinsci.plugins.displayurlapi.actions.RunDisplayAction");
//            assertSent(server, "GET", "/job/fish/10/api/json");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetBuildInfoNotFound() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            BuildInfo output = api.buildInfo( "fish", 10);
//            assertNull(output);
//            assertSent(server, "GET", "/job/fish/10/api/json");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testCreateJob() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String configXML = payloadFromResource("/freestyle-project.xml");
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> success = api.create( "DevTest", configXML);
//            assertNotNull(success);
//            assertTrue(success.getValues());
//            assertTrue(success.errors().isEmpty());
//            assertSentWithXMLFormDataAccept(server, "POST", "/createItem?name=DevTest", configXML, MediaType.WILDCARD);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testCreateJobInFolder() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String configXML = payloadFromResource("/freestyle-project.xml");
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> success = api.create("test-folder", "JobInFolder", configXML);
//            assertNotNull(success);
//            assertTrue(success.getValues());
//            assertTrue(success.errors().isEmpty());
//            assertSentWithXMLFormDataAccept(server, "POST", "/job/test-folder/createItem?name=JobInFolder", configXML, MediaType.WILDCARD);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testSimpleFolderPathWithLeadingAndTrailingForwardSlashes() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String configXML = payloadFromResource("/freestyle-project.xml");
//        server.enqueue(new MockResponse().setResponseCode(200));
//
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> success = api.create("/test-folder/job/test-folder-1/", "JobInFolder", configXML);
//            assertNotNull(success);
//            assertTrue(success.getValues());
//            assertTrue(success.errors().isEmpty());
//            assertSentWithXMLFormDataAccept(server, "POST", "/job/test-folder/job/test-folder-1/createItem?name=JobInFolder", configXML, MediaType.WILDCARD);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testCreateJobAlreadyExists() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String configXML = payloadFromResource("/freestyle-project.xml");
//        server.enqueue(new MockResponse().setHeader("X-Error", "A job already exists with the name ?DevTest?")
//            .setResponseCode(400));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> success = api.create( "DevTest", configXML);
//            assertNotNull(success);
//            assertFalse(success.getValues());
//            assertFalse(success.errors().isEmpty());
//            assertSentWithXMLFormDataAccept(server, "POST", "/createItem?name=DevTest", configXML, MediaType.WILDCARD);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetDescription() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setBody("whatever").setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            String output = api.description( "DevTest");
//            assertNotNull(output);
//            assertEquals(output, "whatever");
//            assertSentAcceptText(server, "GET", "/job/DevTest/description");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetDescriptionNonExistentJob() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            String output = api.description( "DevTest");
//            assertNull(output);
//            assertSentAcceptText(server, "GET", "/job/DevTest/description");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testUpdateDescription() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            boolean success = api.description( "DevTest", "whatever");
//            assertTrue(success);
//            assertSentWithFormData(server, "POST", "/job/DevTest/description", "description=whatever",
//                MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testUpdateDescriptionNonExistentJob() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            boolean success = api.description( "DevTest", "whatever");
//            assertFalse(success);
//            assertSentWithFormData(server, "POST", "/job/DevTest/description", "description=whatever",
//                MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetConfig() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String configXML = payloadFromResource("/freestyle-project.xml");
//        server.enqueue(new MockResponse().setBody(configXML).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            String output = api.config( "DevTest");
//            assertNotNull(output);
//            assertEquals(output, configXML);
//            assertSentAcceptText(server, "GET", "/job/DevTest/config.xml");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetConfigNonExistentJob() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            String output = api.config( "DevTest");
//            assertNull(output);
//            assertSentAcceptText(server, "GET", "/job/DevTest/config.xml");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testUpdateConfig() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String configXML = payloadFromResource("/freestyle-project.xml");
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            boolean success = api.config( "DevTest", configXML);
//            assertTrue(success);
//            assertSentAccept(server, "POST", "/job/DevTest/config.xml", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testUpdateConfigNonExistentJob() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String configXML = payloadFromResource("/freestyle-project.xml");
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            boolean success = api.config( "DevTest", configXML);
//            assertFalse(success);
//            assertSentAccept(server, "POST", "/job/DevTest/config.xml", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testDeleteJob() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> success = api.delete( "DevTest");
//            assertNotNull(success);
//            assertTrue(success.getValues());
//            assertTrue(success.errors().isEmpty());
//            assertSentAccept(server, "POST", "/job/DevTest/doDelete", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testDeleteJobNonExistent() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(400));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> success = api.delete( "DevTest");
//            assertNotNull(success);
//            assertFalse(success.getValues());
//            assertFalse(success.errors().isEmpty());
//            assertSentAccept(server, "POST", "/job/DevTest/doDelete", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testEnableJob() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            boolean success = api.enable( "DevTest");
//            assertTrue(success);
//            assertSentAccept(server, "POST", "/job/DevTest/enable", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testEnableJobAlreadyEnabled() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            boolean success = api.enable( "DevTest");
//            assertTrue(success);
//            assertSentAccept(server, "POST", "/job/DevTest/enable", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testDisableJob() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            boolean success = api.disable( "DevTest");
//            assertTrue(success);
//            assertSentAccept(server, "POST", "/job/DevTest/disable", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testDisableJobAlreadyEnabled() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            boolean success = api.disable( "DevTest");
//            assertTrue(success);
//            assertSentAccept(server, "POST", "/job/DevTest/disable", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testBuildJob() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(
//            new MockResponse().setHeader("Location", "http://127.0.1.1:8080/queue/item/1/").setResponseCode(201));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> output = api.build( "DevTest");
//            assertNotNull(output);
//            assertEquals((int) output.getValues(), 1);
//            assertEquals(output.errors().size(), 0);
//            assertSentAccept(server, "POST", "/job/DevTest/build", "application/unknown");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testBuildJobWithNoLocationReturned() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(
//            new MockResponse().setResponseCode(201));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> output = api.build( "DevTest");
//            assertNotNull(output);
//            assertNull(output.getValues());
//            assertEquals(output.errors().size(), 1);
//            assertNull(output.errors().get(0).congetText());
//            assertEquals(output.errors().get(0).message(), "No queue item Location header could be found despite getting a valid HTTP response.");
//            assertEquals(NumberFormatException.class.getCanonicalName(), output.errors().get(0).exceptionName());
//            assertSentAccept(server, "POST", "/job/DevTest/build", "application/unknown");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testBuildJobNonExistentJob() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> output = api.build( "DevTest");
//            assertNotNull(output);
//            assertNull(output.getValues());
//            assertEquals(output.errors().size(), 1);
//            assertEquals(output.errors().get(0).message(), "");
//            assertEquals(output.errors().get(0).exceptionName(), "org.jclouds.rest.ResourceNotFoundException");
//            assertNotNull(output.errors().get(0).congetText());
//            assertSentAccept(server, "POST", "/job/DevTest/build", "application/unknown");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testBuildJobWithParams() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(
//            new MockResponse().setHeader("Location", "http://127.0.1.1:8080/queue/item/1/").setResponseCode(201));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            Map<String, List<String>> params = new HashMap<>();
//            params.put("SomeKey", List.of("SomeVeryNewValue"));
//            ResponseEntity<Void> output = api.buildWithParameters( "DevTest", params);
//            assertNotNull(output);
//            assertEquals((int) output.getValues(), 1);
//            assertEquals(output.errors().size(), 0);
//            assertSentAccept(server, "POST", "/job/DevTest/buildWithParameters", "application/unknown");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testBuildJobWithNullParamsMap() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(
//            new MockResponse().setHeader("Location", "http://127.0.1.1:8080/queue/item/1/").setResponseCode(201));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> output = api.buildWithParameters( "DevTest", null);
//            assertNotNull(output);
//            assertEquals((int) output.getValues(), 1);
//            assertEquals(output.errors().size(), 0);
//            assertSentAccept(server, "POST", "/job/DevTest/buildWithParameters", "application/unknown");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testBuildJobWithEmptyParamsMap() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(
//            new MockResponse().setHeader("Location", "http://127.0.1.1:8080/queue/item/1/").setResponseCode(201));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> output = api.buildWithParameters( "DevTest", new HashMap<>());
//            assertNotNull(output);
//            assertEquals((int) output.getValues(), 1);
//            assertEquals(output.errors().size(), 0);
//            assertSentAccept(server, "POST", "/job/DevTest/buildWithParameters", "application/unknown");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testBuildJobWithParamsNonExistentJob() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            Map<String, List<String>> params = new HashMap<>();
//            params.put("SomeKey", List.of("SomeVeryNewValue"));
//            ResponseEntity<Void> output = api.buildWithParameters( "DevTest", params);
//            assertNotNull(output);
//            assertNull(output.getValues());
//            assertEquals(output.errors().size(), 1);
//            assertEquals(output.errors().get(0).message(), "");
//            assertEquals(output.errors().get(0).exceptionName(), "org.jclouds.rest.ResourceNotFoundException");
//            assertNotNull(output.errors().get(0).congetText());
//            assertSentAccept(server, "POST", "/job/DevTest/buildWithParameters", "application/unknown");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetParams() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/build-info.json");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            List<Parameter> output = api.buildInfo( "fish", 10).getActions().get(0).getParameters();
//            assertNotNull(output);
//            assertEquals(output.get(0).getName(), "bear");
//            assertEquals(output.get(0).getValues(), "true");
//            assertEquals(output.get(1).getName(), "fish");
//            assertEquals(output.get(1).getValues(), "salmon");
//            assertSent(server, "GET", "/job/fish/10/api/json");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetGitCommitInfo() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/build-info-git-commit.json");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            List<ChangeSet> changeSets = api.buildInfo( "fish", 10).changeSets().get(0).items();
//            assertNotNull(changeSets);
//            assertEquals(changeSets.get(0).affectedPaths().get(0), "some/path/in/the/repository");
//            assertEquals(changeSets.get(0).commitId(), "d27afa0805201322d846d7defc29b82c88d9b5ce");
//            assertEquals(changeSets.get(0).timestamp(), 1461091892486L);
//            assertEquals(changeSets.get(0).author().absoluteUrl(), "http://localhost:8080/user/username");
//            assertEquals(changeSets.get(0).author().fullName(), "username");
//            assertEquals(changeSets.get(0).authorEmail(), "username@localhost");
//            assertEquals(changeSets.get(0).comment(), "Commit comment\n");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetParamsWhenNoBuildParams() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/build-info-no-params.json");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            List<Parameter> output = api.buildInfo( "fish", 10).getActions().get(0).getParameters();
//            assertEquals(output.size(), 0);
//            assertSent(server, "GET", "/job/fish/10/api/json");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetParamsWhenEmptyorNullParams() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/build-info-empty-and-null-params.json");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            List<Parameter> output = api.buildInfo( "fish", 10).getActions().get(0).getParameters();
//            assertNotNull(output);
//            assertEquals(output.get(0).getName(), "bear");
//            assertEquals(output.get(0).getValues(), "null");
//            assertEquals(output.get(1).getName(), "fish");
//            assertTrue(output.get(1).getValues().isEmpty());
//            assertSent(server, "GET", "/job/fish/10/api/json");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetCause() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/build-info-no-params.json");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            List<Cause> output = api.buildInfo( "fish", 10).getActions().get(0).causes();
//            assertNotNull(output);
//            assertEquals(output.get(0).shortDescription(), "Started by user anonymous");
//            assertNull(output.get(0).userId());
//            assertEquals(output.get(0).userName(), "anonymous");
//            assertSent(server, "GET", "/job/fish/10/api/json");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetLastBuildNumber() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/build-number.txt");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            Integer output = api.lastBuildNumber( "DevTest");
//            assertNotNull(output);
//            assertEquals((int) output, 123);
//            assertSentAcceptText(server, "GET", "/job/DevTest/lastBuild/buildNumber");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetLastBuildNumberJobNotExist() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            Integer output = api.lastBuildNumber( "DevTest");
//            assertNull(output);
//            assertSentAcceptText(server, "GET", "/job/DevTest/lastBuild/buildNumber");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetLastBuildTimeStamp() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/build-timestamp.txt");
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            String output = api.lastBuildTimestamp("DevTest");
//            assertNotNull(output);
//            assertEquals(body, output);
//            assertSentAcceptText(server, "GET", "/job/DevTest/lastBuild/buildTimestamp");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetLastBuildTimeStampJobNotExist() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            String output = api.lastBuildTimestamp("DevTest");
//            assertNull(output);
//            assertSentAcceptText(server, "GET", "/job/DevTest/lastBuild/buildTimestamp");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetProgressiveText() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/progressive-text.txt");
//        server.enqueue(new MockResponse().setHeader("X-Text-Size", "123").setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ProgressiveText output = api.progressiveText("DevTest", 0);
//            assertNotNull(output);
//            assertEquals(output.size(), 123);
//            assertFalse(output.hasMoreData());
//            assertSentAcceptText(server, "GET", "/job/DevTest/lastBuild/logText/progressiveText?start=0");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetProgressiveTextOfBuildNumber() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        String body = payloadFromResource("/progressive-text.txt");
//        server.enqueue(new MockResponse().setHeader("X-Text-Size", "123").setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ProgressiveText output = api.progressiveText("DevTest", 1, 0);
//            assertNotNull(output);
//            assertEquals(output.size(), 123);
//            assertFalse(output.hasMoreData());
//            assertSentAcceptText(server, "GET", "/job/DevTest/1/logText/progressiveText?start=0");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testGetProgressiveTextJobNotExist() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ProgressiveText output = api.progressiveText("DevTest", 0);
//            assertNull(output);
//            assertSentAcceptText(server, "GET", "/job/DevTest/lastBuild/logText/progressiveText?start=0");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testRenameJob() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            boolean success = api.rename("DevTest", "NewDevTest");
//            assertTrue(success);
//            assertSentAccept(server, "POST", "/job/DevTest/doRename?newName=NewDevTest", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testRenameJobNotExist() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            boolean success = api.rename("DevTest", "NewDevTest");
//            assertFalse(success);
//            assertSentAccept(server, "POST", "/job/DevTest/doRename?newName=NewDevTest", MediaType.TEXT_HTML);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testRunHistory() throws Exception {
//        MockWebServer server = mockWebServer();
//        String body = payloadFromResource("/runHistory.json");
//
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            List<Workflow> workflows = api.runHistory("MockJob");
//            assertNotNull(workflows);
//            assertSent(server, "GET", "/job/MockJob/wfapi/runs");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testRunHistoryNotExist() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            List<Workflow> workflows = api.runHistory("MockJob");
//            assertNull(workflows);
//            assertSent(server, "GET", "/job/MockJob/wfapi/runs");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testWorkflow() throws Exception {
//        MockWebServer server = mockWebServer();
//        String body = payloadFromResource("/workflow.json");
//
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            Workflow success = api.workflow("DevTest", 16);
//            assertNotNull(success);
//            assertSent(server, "GET", "/job/DevTest/16/wfapi/describe");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testWorkflowNotExist() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            Workflow success = api.workflow("DevTest", 16);
//            assertNull(success);
//            assertSent(server, "GET", "/job/DevTest/16/wfapi/describe");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testPipelineNode() throws Exception {
//        MockWebServer server = mockWebServer();
//        String body = payloadFromResource("/pipeline-node.json");
//
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            PipelineNode success = api.pipelineNode("DevTest", 16, 17);
//            assertNotNull(success);
//            assertSent(server, "GET", "/job/DevTest/16/execution/node/17/wfapi/describe");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testPipelineNodeNotExist() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            PipelineNode success = api.pipelineNode("DevTest", 16, 17);
//            assertNull(success);
//            assertSent(server, "GET", "/job/DevTest/16/execution/node/17/wfapi/describe");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testJobTestReportExists() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json").setBody("{ \"empty\": false }").setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            JsonObject testReport = api.testReport("DevTest", 16);
//            assertNotNull(testReport);
//            assertFalse(testReport.get("empty").getAsBoolean());
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testJobTestReportNotExists() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            JsonObject testReport = api.testReport("DevTest", 16);
//            assertNull(testReport);
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testPipelineNodeLog() throws Exception {
//        MockWebServer server = mockWebServer();
//        String body = payloadFromResource("/pipelineNodeLog.json");
//
//        server.enqueue(new MockResponse().setBody(body).setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            PipelineNodeLog pipelineNodeLog = api.pipelineNodeLog("MockJob", 16, 17);
//            assertNotNull(pipelineNodeLog);
//            assertSent(server, "GET", "/job/MockJob/16/execution/node/17/wfapi/log");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testPipelineNodeLogNotExist() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            PipelineNodeLog pipelineNodeLog = api.pipelineNodeLog("MockJob", 16, 17);
//            assertNull(pipelineNodeLog);
//            assertSent(server, "GET", "/job/MockJob/16/execution/node/17/wfapi/log");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testStopBuild() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> status = api.stop("fish", 99);
//            assertNotNull(status);
//            assertTrue(status.getValues());
//            assertTrue(status.errors().isEmpty());
//            assertSent(server, "POST", "/job/fish/99/stop");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testTermBuild() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> status = api.term("fish", 99);
//            assertNotNull(status);
//            assertTrue(status.getValues());
//            assertTrue(status.errors().isEmpty());
//            assertSent(server, "POST", "/job/fish/99/term");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testKillBuild() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse().setResponseCode(200));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> status = api.kill("fish", 99);
//            assertNotNull(status);
//            assertTrue(status.getValues());
//            assertTrue(status.errors().isEmpty());
//            assertSent(server, "POST", "/job/fish/99/kill");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testTermBuildReturns404() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse()
//            .setHeader("Location", "http://localhost:" + server.getPort() + "job/fish/99/term/")
//            .setResponseCode(302));
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> status = api.term("fish", 99);
//            assertSent(server, "POST", "/job/fish/99/term");
//            assertNotNull(status);
//            assertFalse(status.getValues());
//            assertFalse(status.errors().isEmpty());
//            assertEquals(status.errors().size(), 1);
//            System.out.println("Mock Status: " + status);
//            assertEquals(status.errors().get(0).message(), "The term operation does not exist for " +
//                "http://localhost:" + server.getPort() +
//                "job/fish/99/term/, try stop instead.");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//
//    public void testKillBuildReturns404() throws Exception {
//        MockWebServer server = mockWebServer();
//
//        server.enqueue(new MockResponse()
//            .setHeader("Location", "http://localhost:" + server.getPort() + "job/fish/99/kill/")
//            .setResponseCode(302));
//        server.enqueue(new MockResponse().setResponseCode(404));
//        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
//        JobsApi api = jenkinsApi.jobsApi();
//        try {
//            ResponseEntity<Void> status = api.kill("fish", 99);
//            assertSent(server, "POST", "/job/fish/99/kill");
//            assertNotNull(status);
//            assertFalse(status.getValues());
//            assertFalse(status.errors().isEmpty());
//            assertEquals(status.errors().size(), 1);
//            assertEquals(status.errors().get(0).message(), "The kill operation does not exist for " +
//                "http://localhost:" + server.getPort() +
//                "job/fish/99/kill/, try stop instead.");
//        } finally {
//            jenkinsApi.close();
//            server.shutdown();
//        }
//    }
//}
