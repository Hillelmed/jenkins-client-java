package io.github.hmedioni.jenkins.client;

import io.github.hmedioni.jenkins.client.domain.common.*;
import org.springframework.http.*;

import java.text.*;
import java.util.*;
import java.util.regex.*;

import static io.github.hmedioni.jenkins.client.JenkinsConstants.*;

/**
 * Collection of static methods to be used globally.
 */
public class JenkinsUtils {


    /**
     * Convert passed Map into a JsonElement.
     *
     * @param input the Map to convert.
     * @return JsonElement or empty JsonElement if `input` is null.
     */


    private static final Pattern pattern = Pattern.compile("^.*/queue/item/(\\d+)/$");

    //    public static Object buildFormDataFormMap(Map<String, List<String>> properties) {
//        Map<String, List<String>> props = (Map<String, List<String>>) properties;
//
//        for (Map.Entry<String, List<String>> prop : props.entrySet()) {
//            if (prop.getKey() != null) {
//                String potentialKey = prop.getKey().trim();
//                if (potentialKey.length() > 0) {
//                    if (prop.getValue() == null) {
//                        prop.setValue(Lists.newArrayList(""));
//                    }
//
//                    builder.addFormParam(potentialKey, prop.getValue().toArray(new String[prop.getValue().size()]));
//                }
//            }
//        }
//
//    }

    public static String buildPluginXmlRequest(String pluginId) {
        return MessageFormat.format("<jenkins><install plugin=\"{0}\"/></jenkins>", pluginId);
    }

    public static int getTextSize(HttpHeaders httpHeaders) {
        if (httpHeaders == null) {
            throw new RuntimeException("Unexpected NULL httpHeaders object");
        }
        if (httpHeaders.containsKey("X-Text-Size")) {
            return Integer.parseInt(httpHeaders.getFirst("X-Text-Size"));
        }
        return -1;
    }

    public static boolean hasMoreData(HttpHeaders httpHeaders) {
        if (httpHeaders == null) {
            throw new RuntimeException("Unexpected NULL httpHeaders object");
        }
        if (httpHeaders.containsKey("X-More-Data")) {
            return Boolean.parseBoolean(httpHeaders.getFirst("X-More-Data"));
        }
        return false;
    }

    public static Map<String, String> buildMapFromParamsStr(String params) {
        if (params == null) {
            throw new RuntimeException("Unexpected NULL params object");
        }
        Map<String, String> map = new HashMap<>();
        String[] tmpStr = params.split("\n");
        for (String str : tmpStr) {
            String[] splitter = str.split("=");
            if (splitter.length == 2) {
                String key = splitter[0];
                String value = splitter[1];
                map.put(key, value);
            } else if (splitter.length == 1 && !splitter[0].isEmpty()) {
                String key = splitter[0];
                map.put(key, "");
            }
        }
        return map;
    }

    public static IntegerResponse getQueueItemIntegerResponse(HttpHeaders httpHeaders) {
        if (httpHeaders == null) {
            throw new RuntimeException("Unexpected NULL httpHeaders object");
        }

        List<String> url = httpHeaders.get("Location");
        if (url != null && !url.isEmpty()) {
            Matcher matcher = pattern.matcher(url.get(0));
            if (matcher.find() && matcher.groupCount() == 1) {
                return new IntegerResponse(Integer.valueOf(matcher.group(1)));
            }
        }
        return new IntegerResponse(-1);
    }


    /**
     * If the passed systemProperty is non-null, we will attempt to query
     * the `System Properties` for a value and return it. If no value
     * was found, and environmentVariable is non-null, we will attempt to
     * query the `Environment Variables` for a value and return it. If
     * both are either null or can't be found, then null will be returned.
     *
     * @param systemProperty      possibly existent System Property.
     * @param environmentVariable possibly existent Environment Variable.
     * @return found external value or null.
     */
    public static String retrieveExternalValue(final String systemProperty, final String environmentVariable) {

        // 1.) Search for System Property
        if (systemProperty != null) {
            final String value = System.getProperty(systemProperty);
            if (value != null) {
                return value;
            }
        }

        if (environmentVariable != null) {
            return System.getenv().get(environmentVariable);
        }

        return null;
    }

    /**
     * Find endpoint searching first within `System Properties` and
     * then within `Environment Variables` returning whichever has a
     * value first.
     *
     * @return endpoint or null if it can't be found.
     */
    public static String inferEndpoint() {
        final String possibleValue = JenkinsUtils.retrieveExternalValue(ENDPOINT_SYSTEM_PROPERTY, ENDPOINT_ENVIRONMENT_VARIABLE);
        return possibleValue != null ? possibleValue : DEFAULT_ENDPOINT;
    }

    /**
     * Find credentials (ApiToken, UsernamePassword, or Anonymous) from system/environment.
     *
     * @return JenkinsAuthentication
     */
    public static JenkinsAuthentication inferAuthentication() {

        final JenkinsAuthentication.JenkinsAuthenticationBuilder inferAuth = new JenkinsAuthentication.JenkinsAuthenticationBuilder();
        // 1.) Check for API Token as this requires no crumb hence is faster
        String authValue = JenkinsUtils.retrieveExternalValue(API_TOKEN_SYSTEM_PROPERTY, API_TOKEN_ENVIRONMENT_VARIABLE);
        if (authValue != null) {
            inferAuth.apiToken(authValue);
            return inferAuth.build();
        }

        // 2.) Check for UsernamePassword auth credentials.
        authValue = JenkinsUtils.retrieveExternalValue(CREDENTIALS_SYSTEM_PROPERTY, CREDENTIALS_ENVIRONMENT_VARIABLE);
        if (authValue != null) {
            inferAuth.credentials(authValue);
            return inferAuth.build();
        }

        // 3.) If neither #1 or #2 find anything "Anonymous" access is assumed.
        return inferAuth.build();
    }
}
