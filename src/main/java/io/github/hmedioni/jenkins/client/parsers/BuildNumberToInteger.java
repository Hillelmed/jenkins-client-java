
//
//package io.github.hmedioni.jenkins.client.parsers;
//
//import java.io.*;
//import java.net.http.*;
//
///**
// * Created by dancc on 3/11/16.
// */
//public class BuildNumberToInteger {
//
//    public Integer apply(HttpResponse response) {
//        return Integer.valueOf(getTextOutput(response));
//    }
//
//    public String getTextOutput(HttpResponse response) {
//        InputStream is = null;
////        try {
////            is = response.getPayload().openStream();
////            return CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8)).trim();
////        } catch (Exception e) {
////            Throwables.propagate(e);
////        } finally {
////            if (is != null) {
////                try {
////                    is.close();
////                } catch (Exception e) {
////                    Throwables.propagate(e);
////                }
////            }
////        }
//
//        return null;
//    }
//}
