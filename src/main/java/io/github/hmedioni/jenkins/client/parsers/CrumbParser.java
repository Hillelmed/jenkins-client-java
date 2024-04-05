
//
//package io.github.hmedioni.jenkins.client.parsers;
//
//import com.google.common.base.*;
//import io.github.hmedioni.jenkins.client.domain.crumb.*;
//import org.jclouds.http.*;
//import org.jclouds.util.*;
//
//import javax.inject.*;
//
//import java.io.*;
//import java.util.*;
//
//import static io.github.hmedioni.jenkins.client.JenkinsConstants.*;
//
///**
// * Turn a valid response, but one that has no body, into a Crumb.
// */
//@Singleton
//public class CrumbParser implements Function<HttpResponse, Crumb> {
//
//    private static String crumbValue(HttpResponse input) throws IOException {
//        return Strings2.toStringAndClose(input.getPayload().openStream())
//            .split(":")[1];
//    }
//
//    private static String sessionIdCookie(HttpResponse input) {
//        return setCookieValues(input).stream()
//            .filter(c -> c.startsWith(JENKINS_COOKIES_JSESSIONID))
//            .findFirst()
//            .orElse("");
//    }
//
//    private static Collection<String> setCookieValues(HttpResponse input) {
//        Collection<String> setCookieValues = input.getHeaders().get(HttpHeaders.SET_COOKIE);
//        if (setCookieValues.isEmpty()) {
//            return input.getHeaders().get(HttpHeaders.SET_COOKIE.toLowerCase());
//        } else {
//            return setCookieValues;
//        }
//    }
//
//    @Override
//    public Crumb apply(final HttpResponse input) {
//        if (input == null) {
//            throw new RuntimeException("Unexpected NULL HttpResponse object");
//        }
//
//        final int statusCode = input.getStatusCode();
//        if (statusCode >= 200 && statusCode < 400) {
//            try {
//                return Crumb.create(crumbValue(input), sessionIdCookie(input));
//            } catch (final IOException e) {
//                throw new RuntimeException(input.getStatusLine(), e);
//            }
//        } else {
//            throw new RuntimeException(input.getStatusLine());
//        }
//    }
//}
