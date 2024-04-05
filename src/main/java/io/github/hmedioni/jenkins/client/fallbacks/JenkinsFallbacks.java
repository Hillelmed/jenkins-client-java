//package io.github.hmedioni.jenkins.client.fallbacks;
//
//
//import io.github.hmedioni.jenkins.client.domain.common.Error;
//import io.github.hmedioni.jenkins.client.domain.common.*;
//import io.github.hmedioni.jenkins.client.domain.crumb.*;
//import io.github.hmedioni.jenkins.client.domain.plugins.*;
//import io.github.hmedioni.jenkins.client.domain.system.*;
//
//import java.util.*;
//
//
//public final class JenkinsFallbacks {
//
//    public static SystemInfo createSystemInfoFromErrors(final List<Error> errors) {
//        final String illegalValue = "-1";
//        return SystemInfo.create(illegalValue, illegalValue, illegalValue,
//            illegalValue, illegalValue, illegalValue, errors);
//    }
//
//    /**
//     * Parse list of Error's from generic Exception.
//     *
//     * @param output Exception containing error data
//     * @return List of culled Error's
//     */
//    public static List<Error> getErrors(final Exception output) {
//        final Error error = Error.create( output.getMessage(),
//            output.getClass().getName());
//        return List.of(error);
//    }
//
//    /**
//     * Parse list of Error's from output.
//     *
//     * @param output Throwable containing error data
//     * @return List of culled Error's
//     */
//    public static List<Error> getErrors(final Throwable output) {
//
//        final List<Error> errors = List.of();
//
//        String context = null;
//        String message = output.getMessage();
//        final String[] messageParts = output.getMessage().split("->");
//        switch (messageParts.length) {
//            case 1:
//                message = messageParts[0].trim();
//                break;
//            case 3:
//                context = messageParts[0].trim();
//                message = messageParts[2].trim();
//                break;
//        }
//
//        final Error error = Error.create(context, message, output.getClass().getCanonicalName());
//        errors.add(error);
//
//        return errors;
//    }
//
//    public static final class SystemInfoOnError implements Fallback<Object> {
//        @Override
//        public Object createOrPropagate(final Throwable throwable) {
//            checkNotNull(throwable, "throwable");
//            return createSystemInfoFromErrors(getErrors(throwable));
//        }
//    }
//
//    public static final class ResponseEntity<Void>OnError implements Fallback<Object> {
//        @Override
//        public Object createOrPropagate(final Throwable throwable) {
//            checkNotNull(throwable, "throwable");
//            try {
//                return ResponseEntity<Void>.create(false, getErrors(throwable));
//            } catch (JsonSyntaxException e) {
//                return ResponseEntity<Void>.create(false, getErrors(e));
//            }
//        }
//    }
//
//    public static final class IntegerResponseOnError implements Fallback<Object> {
//        @Override
//        public Object createOrPropagate(final Throwable throwable) {
//            checkNotNull(throwable, "throwable");
//            try {
//                return IntegerResponse.create( getErrors(throwable));
//            } catch (JsonSyntaxException e) {
//                return IntegerResponse.create( getErrors(e));
//            }
//        }
//    }
//
//    public static final class CrumbOnError implements Fallback<Object> {
//        @Override
//        public Object createOrPropagate(final Throwable throwable) {
//            checkNotNull(throwable, "throwable");
//            try {
//                return Crumb.create( getErrors(throwable));
//            } catch (JsonSyntaxException e) {
//                return Crumb.create( getErrors(e));
//            }
//        }
//    }
//
//    public static final class PluginsOnError implements Fallback<Object> {
//        @Override
//        public Object createOrPropagate(final Throwable throwable) {
//            checkNotNull(throwable, "throwable");
//            try {
//                return Plugins.create(  getErrors(throwable));
//            } catch (JsonSyntaxException e) {
//                return Plugins.create(  getErrors(e));
//            }
//        }
//    }
//
//    // fix/hack for Jenkins jira issue: JENKINS-21311
//    public static final class JENKINS_21311 implements Fallback<Object> {
//        @Override
//        public Object createOrPropagate(final Throwable throwable) {
//            checkNotNull(throwable, "throwable");
//            try {
//                if (throwable.getClass() == ResourceNotFoundException.class) {
//                    return ResponseEntity<Void>.create(true, null);
//                } else {
//                    return ResponseEntity<Void>.create(false, getErrors(throwable));
//                }
//            } catch (JsonSyntaxException e) {
//                return ResponseEntity<Void>.create(false, getErrors(e));
//            }
//        }
//    }
//}
