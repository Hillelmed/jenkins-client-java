package io.github.hillelmed.jenkins.client.features;


import io.github.hillelmed.jenkins.client.*;
import io.github.hillelmed.jenkins.client.domain.queue.*;
import io.github.hillelmed.jenkins.client.exception.*;
import okhttp3.mockwebserver.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.util.*;

import static org.testng.Assert.*;

/**
 * Mock tests for the {@link QueueApi} class.
 */
@Test(groups = "unit")
public class QueueApiMockTest extends BaseJenkinsMockTest {

    public void testGetQueue() throws Exception {
        MockWebServer server = mockWebServer(false);
        String body = payloadFromResource("/queue.json");
        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        try (jenkinsApi) {
            QueueApi api = jenkinsApi.queueApi();
            ResponseEntity<QueueItemsArray> output = api.queue();
            assertEquals(output.getBody().getItems().size(), 2);
            assertSent(server, "GET", "/queue/api/json");
        } finally {
            server.shutdown();
        }
    }

    public void testGetPendingQueueItem() throws Exception {
        MockWebServer server = mockWebServer(false);
        String body = payloadFromResource("/queueItemPending.json");
        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        int queueItemId = 143;
        QueueItem queueItem = jenkinsApi.queueApi().queueItem(queueItemId);
        try {
            assertFalse(queueItem.isCancelled());
            assertEquals(queueItem.getWhy(), "Build #9 is already in progress (ETA:15 sec)");
            assertNull(queueItem.getExecutable());
            assertSent(server, "GET", "/queue/item/" + queueItemId + "/api/json");
        } finally {
            jenkinsApi.close();
            server.shutdown();
        }
    }

    public void testGetCancelledQueueItem() throws Exception {
        MockWebServer server = mockWebServer(false);
        String body = payloadFromResource("/queueItemCancelled.json");
        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        int queueItemId = 143;
        QueueItem queueItem = jenkinsApi.queueApi().queueItem(queueItemId);
        try {
            assertTrue(queueItem.isCancelled());
            assertNull(queueItem.getWhy());
            assertNull(queueItem.getExecutable());
            assertSent(server, "GET", "/queue/item/" + queueItemId + "/api/json");
        } finally {
            jenkinsApi.close();
            server.shutdown();
        }
    }

    public void testGetRunningQueueItem() throws Exception {
        MockWebServer server = mockWebServer(false);
        String body = payloadFromResource("/queueItemRunning.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        int queueItemId = 143;
        int buildNumber = 14;
        QueueItem queueItem = jenkinsApi.queueApi().queueItem(queueItemId);
        Map<String, String> map = new HashMap<>();
        map.put("a", "4");
        try {
            assertEquals(queueItem.getActions().get(0).getParameters().size(), map.size());
            assertFalse(queueItem.isCancelled());
            assertNull(queueItem.getWhy());
            assertNotNull(queueItem.getExecutable());
            assertEquals((int) queueItem.getExecutable().getNumber(), buildNumber);
            assertEquals(queueItem.getExecutable().getUrl(), "http://localhost:8082/job/test/" + buildNumber + "/");
            assertSent(server, "GET", "/queue/item/" + queueItemId + "/api/json");
        } finally {
            jenkinsApi.close();
            server.shutdown();
        }
    }

    public void testQueueItemMultipleParameters() throws Exception {
        MockWebServer server = mockWebServer(false);
        String body = payloadFromResource("/queueItemMultipleParameters.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        int queueItemId = 143;
        QueueItem queueItem = jenkinsApi.queueApi().queueItem(queueItemId);
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        try {
            assertEquals(JenkinsUtils.buildMapFromParamsStr(queueItem.getParams()), map);
        } finally {
            jenkinsApi.close();
            server.shutdown();
        }
    }

    public void testQueueItemEmptyParameterValue() throws Exception {
        MockWebServer server = mockWebServer(false);
        String body = payloadFromResource("/queueItemEmptyParameterValue.json");
        server.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        int queueItemId = 143;
        QueueItem queueItem = jenkinsApi.queueApi().queueItem(queueItemId);
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "");
        map.put("c", "3");
        try {
            assertEquals(queueItem.getActions().get(0).getParameters().size(), map.size());
        } finally {
            jenkinsApi.close();
            server.shutdown();
        }
    }

    public void testCancelQueueItem() throws Exception {
        MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setResponseCode(404));
        int queueItemId = 143;
        try (JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort())) {
            jenkinsApi.queueApi().cancel(queueItemId);
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertNotNull(e);
            assertFalse(e.errors().isEmpty());
            assertSentWithFormData(server, "POST", "/queue/cancelItem", "id=" + queueItemId);
        } finally {
            server.shutdown();
        }
    }

    public void testCancelNonExistentQueueItem() throws Exception {
        MockWebServer server = mockWebServer();
        server.enqueue(new MockResponse().setResponseCode(500));
        int queueItemId = 143;
        try (JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort())) {
            ResponseEntity<Void> result = jenkinsApi.queueApi().cancel(queueItemId);
        } catch (JenkinsAppException e) {
            assertTrue(e.code().is4xxClientError());
            assertFalse(e.errors().isEmpty());
            assertSentWithFormData(server, "POST", "/queue/cancelItem", "id=" + queueItemId);
        } finally {
            server.shutdown();
        }
    }

    public void testQueueItemNullTaskName() throws Exception {
        MockWebServer server = mockWebServer(false);
        String body = payloadFromResource("/queueItemNullTaskName.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        int queueItemId = 143;
        QueueItem queueItem = jenkinsApi.queueApi().queueItem(queueItemId);
        try {
            assertFalse(queueItem.isCancelled());
            assertEquals(queueItem.getWhy(), "Build #9 is already in progress (ETA:15 sec)");
            assertNull(queueItem.getExecutable());
            assertSent(server, "GET", "/queue/item/" + queueItemId + "/api/json");
        } finally {
            jenkinsApi.close();
            server.shutdown();
        }
    }

    public void testQueueItemMissingTaskUrl() throws Exception {
        MockWebServer server = mockWebServer(false);
        String body = payloadFromResource("/queueItemMissingTaskUrl.json");
        server.enqueue(new MockResponse()
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(body).setResponseCode(200));
        JenkinsApi jenkinsApi = api("http://localhost:" + server.getPort());
        int queueItemId = 143;
        QueueItem queueItem = jenkinsApi.queueApi().queueItem(queueItemId);
        try {
            assertFalse(queueItem.isCancelled());
            assertEquals(queueItem.getWhy(), "Just a random message here");
            assertNull(queueItem.getExecutable());
            assertSent(server, "GET", "/queue/item/" + queueItemId + "/api/json");
        } finally {
            jenkinsApi.close();
            server.shutdown();
        }
    }

}
