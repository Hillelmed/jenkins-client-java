package io.github.hmedioni.jenkins.client;

import okhttp3.mockwebserver.*;
import org.springframework.http.*;
import org.testng.annotations.*;

import java.io.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Base class for all Jenkins mock tests.
 */
@Test(groups = "unit")
public class BaseJenkinsMockTest extends BaseJenkinsTest {

    /**
     * Create a MockWebServer with an initial bread-crumb response.
     *
     * @return instance of MockWebServer
     * @throws IOException if unable to start/play server
     */

    public static MockWebServer mockWebServer() throws IOException {
        return mockWebServer(true);
    }

    public static MockWebServer mockWebServer(boolean needCrumb) throws IOException {
        final MockWebServer server = new MockWebServer();
        server.start();
        if (needCrumb) {
            final String value = "{\"_class\":\"hudson.security.csrf.DefaultCrumbIssuer\",\"crumb\":\"eafb798e91a90591b7fb2b779b32dcef9311cfdee8de09158f23a16a11aafa22\",\"crumbRequestField\":\"Jenkins-Crumb\"}";
            final String cookieValue = "JSESSIONID.b747c9b2=node04qcr5em5lt8l10mueszes38ac1.node0; Path=/; HttpOnly";
            server.enqueue(new MockResponse()
                .setBody(value)
                .setHeader(HttpHeaders.SET_COOKIE, cookieValue)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));
        }
        return server;
    }

    private static Map<String, String> extractParams(final String path) {

        final int qmIndex = path.indexOf('?');
        if (qmIndex <= 0) {
            return Collections.unmodifiableMap(new HashMap<>());
        }

        final Map<String, String> builder = new HashMap<>();

        final String[] params = path.substring(qmIndex + 1).split("&");
        for (final String param : params) {
            final String[] keyValue = param.split("=", 2);
            if (keyValue.length > 1) {
                builder.put(keyValue[0], keyValue[1]);
            }
        }

        return Collections.unmodifiableMap(builder);
    }

    protected RecordedRequest assertSent(final MockWebServer server,
                                         final String method,
                                         final String path) throws InterruptedException {

        return assertSent(server, method, path, Map.of());
    }

    protected RecordedRequest assertSent(final MockWebServer server,
                                         final String method,
                                         final String expectedPath,
                                         final Map<String, ?> queryParams) throws InterruptedException {

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo(method);
//        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        final String path = request.getPath();
        final String rawPath = path.contains("?") ? path.substring(0, path.indexOf('?')) : path;
        assertThat(rawPath).isEqualTo(expectedPath);

        final Map<String, String> normalizedParams = new HashMap<>();
        queryParams.forEach((k, v) -> normalizedParams.put(k, v.toString()));
        assertThat(normalizedParams).isEqualTo(extractParams(path));

        return request;
    }

    protected RecordedRequest assertSentWithFormData(final MockWebServer server,
                                                     final String method,
                                                     final String path,
                                                     final String body) throws InterruptedException {

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);
        assertThat(new String(request.getBody().readByteArray())).isEqualTo(body);
        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        if (request.getHeader(HttpHeaders.CONTENT_TYPE).contains(";")) {
            assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE).split(";")[0]).isEqualTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        } else {
            assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        }

        return request;
    }

    protected RecordedRequest assertSentWithFormData(MockWebServer server, String method,
                                                     String path, String body,
                                                     String acceptType) throws InterruptedException {

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);
        assertThat(new String(request.getBody().readByteArray())).isEqualTo(body);
        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(acceptType);
        if (request.getHeader(HttpHeaders.CONTENT_TYPE).contains(";")) {
            assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE).split(";")[0]).isEqualTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        } else {
            assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        }
        return request;
    }

    protected RecordedRequest assertSentWithXMLFormDataAccept(MockWebServer server, String method, String path,
                                                              String body, String acceptType) throws InterruptedException {

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);
        assertThat(new String(request.getBody().readByteArray())).isEqualTo(body);
        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(acceptType);
        assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.TEXT_XML_VALUE);
        return request;
    }

    protected RecordedRequest assertSentAcceptText(MockWebServer server, String method, String path) throws InterruptedException {
        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);
//        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.TEXT_PLAIN_VALUE);
        return request;
    }

    protected RecordedRequest assertSentAccept(MockWebServer server, String method, String path, String acceptType) throws InterruptedException {
        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);
        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(acceptType);
        return request;
    }

}
