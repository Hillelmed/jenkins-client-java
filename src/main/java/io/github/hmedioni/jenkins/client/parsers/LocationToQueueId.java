
//
//package io.github.hmedioni.jenkins.client.parsers;
//
//import com.google.common.base.*;
//import io.github.hmedioni.jenkins.client.domain.common.Error;
//import io.github.hmedioni.jenkins.client.domain.common.*;
//import org.jclouds.http.*;
//
//import javax.inject.*;
//import java.util.regex.*;
//
///**
// * Created by dancc on 3/11/16.
// */
//@Singleton
//public class LocationToQueueId implements Function<HttpResponse, IntegerResponse> {
//
//    private static final Pattern pattern = Pattern.compile("^.*/queue/item/(\\d+)/$");
//
//    public IntegerResponse apply(HttpResponse response) {
//        if (response == null) {
//            throw new RuntimeException("Unexpected NULL HttpResponse object");
//        }
//
//        String url = response.getFirstHeaderOrNull("Location");
//        if (url != null) {
//            Matcher matcher = pattern.matcher(url);
//            if (matcher.find() && matcher.groupCount() == 1) {
//                return IntegerResponse.create(Integer.valueOf(matcher.group(1)), null);
//            }
//        }
//        final Error error = Error.create(
//            "No queue item Location header could be found despite getting a valid HTTP response.",
//            NumberFormatException.class.getCanonicalName());
//        return IntegerResponse.create( List.of(error));
//    }
//}
