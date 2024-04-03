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

package io.github.hmedioni.jenkins.client;

import okhttp3.mockwebserver.*;
import org.springframework.http.*;

import java.io.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Base class for all Jenkins mock tests.
 */
public class BaseJenkinsMockTest extends BaseJenkinsTest {

    /**
     * Create a MockWebServer with an initial bread-crumb response.
     *
     * @return instance of MockWebServer
     * @throws IOException if unable to start/play server
     */
    public static MockWebServer mockWebServer() throws IOException {
        final MockWebServer server = new MockWebServer();
        server.start();
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
        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON);

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
        assertThat(request.getUtf8Body()).isEqualTo(body);
        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_FORM_URLENCODED);
        return request;
    }

    protected RecordedRequest assertSentWithFormData(MockWebServer server, String method,
                                                     String path, String body,
                                                     String acceptType) throws InterruptedException {

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);
        assertThat(request.getUtf8Body()).isEqualTo(body);
        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(acceptType);
        assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_FORM_URLENCODED);
        return request;
    }

    protected RecordedRequest assertSentWithXMLFormDataAccept(MockWebServer server, String method, String path,
                                                              String body, String acceptType) throws InterruptedException {

        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);
        assertThat(request.getUtf8Body()).isEqualTo(body);
        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(acceptType);
        assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_XML);
        return request;
    }

    protected RecordedRequest assertSentAcceptText(MockWebServer server, String method, String path) throws InterruptedException {
        RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod()).isEqualTo(method);
        assertThat(request.getPath()).isEqualTo(path);
        assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.TEXT_PLAIN);
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
