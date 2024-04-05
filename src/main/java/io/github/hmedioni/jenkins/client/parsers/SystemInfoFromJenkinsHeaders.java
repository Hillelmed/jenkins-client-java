
//
//package io.github.hmedioni.jenkins.client.parsers;
//
//import com.google.common.base.*;
//import io.github.hmedioni.jenkins.client.domain.system.*;
//import org.jclouds.http.*;
//
//import javax.inject.*;
//
///**
// * Created by dancc on 3/11/16.
// */
//@Singleton
//public class SystemInfoFromJenkinsHeaders implements Function<HttpResponse, SystemInfo> {
//
//    @Override
//    public SystemInfo apply(HttpResponse response) {
//        if (response == null) {
//            throw new RuntimeException("Unexpected NULL HttpResponse object");
//        }
//
//        final int statusCode = response.getStatusCode();
//        if (statusCode >= 200 && statusCode < 400) {
//            return SystemInfo.create(response.getFirstHeaderOrNull("X-Hudson"), response.getFirstHeaderOrNull("X-Jenkins"),
//                response.getFirstHeaderOrNull("X-Jenkins-Session"),
//                response.getFirstHeaderOrNull("X-Instance-Identity"), response.getFirstHeaderOrNull("X-SSH-Endpoint"),
//                response.getFirstHeaderOrNull("Server"), null);
//        } else {
//            throw new RuntimeException(response.getStatusLine());
//        }
//    }
//}
