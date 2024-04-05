//
//package io.github.hmedioni.jenkins.client.parsers;
//
//import com.google.common.base.*;
//import io.github.hmedioni.jenkins.client.domain.common.*;
//import org.jclouds.http.*;
//
//import javax.inject.*;
//
///**
// * Turn a valid response, but one that has no body, into a ResponseEntity<Void>.
// */
//@Singleton
//public class ResponseEntity<Void>Parser implements Function<HttpResponse, ResponseEntity<Void>> {
//
//    @Override
//    public ResponseEntity<Void> apply(final HttpResponse input) {
//        if (input == null) {
//            throw new RuntimeException("Unexpected NULL HttpResponse object");
//        }
//
//        final int statusCode = input.getStatusCode();
//        if (statusCode >= 200 && statusCode < 400) {
//            return ResponseEntity<Void>.create(true, null);
//        } else {
//            throw new RuntimeException(input.getStatusLine());
//        }
//    }
//}
