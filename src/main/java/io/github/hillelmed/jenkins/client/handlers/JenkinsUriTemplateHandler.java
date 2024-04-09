package io.github.hillelmed.jenkins.client.handlers;

import org.springframework.lang.*;
import org.springframework.util.*;
import org.springframework.web.util.*;

import java.net.*;
import java.util.*;
import java.util.regex.*;

public class JenkinsUriTemplateHandler implements UriBuilderFactory {

    private final UriComponentsBuilder baseUri;

    public JenkinsUriTemplateHandler(String baseUri) {
        this.baseUri = UriComponentsBuilder.fromUriString(baseUri);
    }

    @Override
    public URI expand(String uriTemplate, Map<String, ?> uriVars) {
        return uriString(uriTemplate).build(uriVars);
    }

    @Override
    public URI expand(String uriTemplate, Object... uriVars) {
        return uriString(uriTemplate).build(uriVars);
    }

    // UriBuilderFactory
    @Override
    public UriBuilder builder() {
        return new JenkinsUriBuilder("");
    }


    @Override
    public UriBuilder uriString(String uriTemplate) {
        return new JenkinsUriTemplateHandler.JenkinsUriBuilder(uriTemplate);
    }


    private class JenkinsUriBuilder implements UriBuilder {

        private final UriComponentsBuilder uriComponentsBuilder;

        public JenkinsUriBuilder(String uriTemplate) {
            this.uriComponentsBuilder = initUriComponentsBuilder(uriTemplate);
        }

        private UriComponentsBuilder initUriComponentsBuilder(String uriTemplate) {
            UriComponentsBuilder result;
            if (!StringUtils.hasLength(uriTemplate)) {
                result = (baseUri != null ? baseUri.cloneBuilder() : UriComponentsBuilder.newInstance());
            } else if (baseUri != null) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
                UriComponents uri = builder.build();
                result = (uri.getHost() == null ? baseUri.cloneBuilder().uriComponents(uri) : builder);
            } else {
                result = UriComponentsBuilder.fromUriString(uriTemplate);
            }
            parsePathIfNecessary(result);
            return result;
        }

        private void parsePathIfNecessary(UriComponentsBuilder result) {
            UriComponents uric = result.build();
            String path = uric.getPath();
            result.replacePath(null);
            for (String segment : uric.getPathSegments()) {
                result.pathSegment(segment);
            }
            if (path != null && path.endsWith("/")) {
                result.path("/");
            }
        }


        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder scheme(@Nullable String scheme) {
            this.uriComponentsBuilder.scheme(scheme);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder userInfo(@Nullable String userInfo) {
            this.uriComponentsBuilder.userInfo(userInfo);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder host(@Nullable String host) {
            this.uriComponentsBuilder.host(host);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder port(int port) {
            this.uriComponentsBuilder.port(port);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder port(@Nullable String port) {
            this.uriComponentsBuilder.port(port);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder path(String path) {
            this.uriComponentsBuilder.path(path);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder replacePath(@Nullable String path) {
            this.uriComponentsBuilder.replacePath(path);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder pathSegment(String... pathSegments) {
            this.uriComponentsBuilder.pathSegment(pathSegments);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder query(String query) {
            this.uriComponentsBuilder.query(query);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder replaceQuery(@Nullable String query) {
            this.uriComponentsBuilder.replaceQuery(query);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder queryParam(String name, Object... values) {
            this.uriComponentsBuilder.queryParam(name, values);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder queryParam(String name, @Nullable Collection<?> values) {
            this.uriComponentsBuilder.queryParam(name, values);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder queryParamIfPresent(String name, Optional<?> value) {
            this.uriComponentsBuilder.queryParamIfPresent(name, value);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder queryParams(MultiValueMap<String, String> params) {
            this.uriComponentsBuilder.queryParams(params);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder replaceQueryParam(String name, Object... values) {
            this.uriComponentsBuilder.replaceQueryParam(name, values);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder replaceQueryParam(String name, @Nullable Collection<?> values) {
            this.uriComponentsBuilder.replaceQueryParam(name, values);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder replaceQueryParams(MultiValueMap<String, String> params) {
            this.uriComponentsBuilder.replaceQueryParams(params);
            return this;
        }

        @Override
        public JenkinsUriTemplateHandler.JenkinsUriBuilder fragment(@Nullable String fragment) {
            this.uriComponentsBuilder.fragment(fragment);
            return this;
        }

        @Override
        public URI build(Map<String, ?> uriVars) {
            UriComponents uric = this.uriComponentsBuilder.build().expand(uriVars);
            return createUri(uric);
        }

        @Override
        public URI build(Object... uriVars) {
            UriComponents uric = this.uriComponentsBuilder.build().expand(uriVars);
            return createUri(uric);
        }

        private URI createUri(UriComponents uric) {
            return URI.create(Pattern.compile(" ").matcher(uric.toString()).replaceAll("%20"));
        }

        @Override
        public String toUriString() {
            return this.uriComponentsBuilder.build().toUriString();
        }
    }
}
