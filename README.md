[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.hillelmed/jenkins-client-java/badge.png)](https://maven-badges.herokuapp.com/maven-central/io.github.hillelmed/jenkins-client-java)
[![Stack Overflow](https://img.shields.io/badge/stack%20overflow-jenkins&#8211;rest-4183C4.svg)](https://stackoverflow.com/questions/tagged/jenkins+client+java)

# jenkins-client-java

Java client is built on the top of Http interface and webClient for working with Jenkins REST API Support Java 17 and
Java 21.

## Setup

Client's need jenkins properties and can be built like so:

```
JenkinsProperties jenkinsProperties = JenkinsProperties.builder().url("http://localhost:8080")
    .jenkinsAuthentication(JenkinsAuthentication.builder().authType(AuthenticationType.USERNAME_PASSWORD)
        .credentials("admin:password").build()).build();
JenkinsClient jenkinsClient = JenkinsClient.create(jenkinsProperties);

SystemInfo systemInfo = client.api().systemApi().systemInfo();
assertTrue(systemInfo.getUrl().equals("http://localhost:8080/"));
```

If Disabling CSRF Protection in your jenkins

``hudson.security.csrf.GlobalCrumbIssuerConfiguration.DISABLE_CSRF_PROTECTION=true``

Config your properties like that:

```
JenkinsProperties jenkinsProperties = JenkinsProperties.builder().url("http://localhost:8080")
    .jenkinsAuthentication(JenkinsAuthentication.builder().crumbEnabled(false).authType(AuthenticationType.USERNAME_PASSWORD)
        .credentials("admin:password").build()).build();
JenkinsClient jenkinsClient = JenkinsClient.create(jenkinsProperties);

SystemInfo systemInfo = client.api().systemApi().systemInfo();
assertTrue(systemInfo.getUrl().equals("http://localhost:8080/"));
```

Example of override the webClient for bearerAuth (Add the JenkinsUriTemplateHandler from jar):
```
WebClient webClient = WebClient.builder().uriBuilderFactory(new JenkinsUriTemplateHandler("http://localhost:8080"))
	.filter((request, next) -> {
	    request.headers().setBearerAuth("token");
	    return next.exchange(request);
	}).build();
JenkinsProperties jenkinsProperties = JenkinsProperties.builder().url("http://localhost:8080")
	.jenkinsAuthentication(JenkinsAuthentication.builder().crumbEnabled(false).build()).build();
JenkinsClient jenkinsClient = JenkinsClient.create(jenkinsProperties, webClient);
```
We can also add some filter from jar for example:
```
WebClient webClient = WebClient.builder().uriBuilderFactory(new JenkinsUriTemplateHandler("http://localhost:8080"))
	.filter(new ScrubNullFolderParam())
	.filter((request, next) -> {
	    request.headers().setBearerAuth("token");
	    return next.exchange(request);
	}).build();
JenkinsProperties jenkinsProperties = JenkinsProperties.builder().url("http://localhost:8080")
	.jenkinsAuthentication(JenkinsAuthentication.builder().crumbEnabled(false).build()).build();
JenkinsClient jenkinsClient = JenkinsClient.create(jenkinsProperties, webClient);
```

## Latest release

Can be found in maven like so:

```
<dependency>
  <groupId>io.github.hillelmed</groupId>
  <artifactId>jenkins-client-java</artifactId>
  <version>X.Y.Z</version>
  <classifier>sources|tests|javadoc|all</classifier> (Optional)
</dependency>
```

## Documentation

* javadocs can be found via [github pages here](http://hillelmed.github.io/jenkins-client-java/docs/javadoc/)
* the [jenkins-rest wiki](https://github.com/Hillelmed/jenkins-client-java/wiki)

## Property based setup

Client instances do NOT need to supply the endPoint or credentials as a part of instantiating the JenkinsClient object.
Instead, one can supply them through system properties, environment variables, or a combination
of the two. System properties will be searched first and if not found, will attempt to
query the environment.

Setting the `endpoint` can be done with any of the following (searched in order):

- `jenkins.rest.endpoint`
- `jenkinsRestEndpoint`
- `JENKINS_REST_ENDPOINT`

When none is found, the endpoint is set to `http://localhost:8080`.

Setting the `credentials` can be done with any of the following (searched in order):

- `jenkins.rest.api.token`
- `jenkinsRestApiToken`
- `JENKINS_REST_API_TOKEN`
- `jenkins.rest.credentials`
- `jenkinsRestCredentials`
- `JENKINS_REST_CREDENTIALS`

When none is found, no authentication is used (anonymous).

## Credentials

jenkins-rest credentials can take 1 of 3 forms:

- Colon delimited username and api token: __admin:apiToken__
    - use `JenkinsAuthentication.builder().apiToken("admin:apiToken")`
- Colon delimited username and password: __admin:password__
    - use `JenkinsAuthentication.builder().credentials("admin:password")`
- Base64 encoded username followed by password __YWRtaW46cGFzc3dvcmQ=__ or api token __YWRtaW46YXBpVG9rZW4=__
    - use `JenkinsAuthentication.builder().credentials("YWRtaW46cGFzc3dvcmQ=")`
    - use `JenkinsAuthentication.builder().apiToken("YWRtaW46YXBpVG9rZW4=")`

```
JenkinsProperties jenkinsProperties = JenkinsProperties.builder().url("http://localhost:8080")
    .jenkinsAuthentication(JenkinsAuthentication.builder().credentials("admin:password").build()).build();
JenkinsClient jenkinsClient = JenkinsClient.create(jenkinsProperties);
```

The Jenkins crumb is automatically requested when POSTing using the anonymous and the username:password authentication
methods.
It is not requested when you use the apiToken as it is not needed in this case.
For more details, see

* [CSRF Protection on jenkins.io](https://www.jenkins.io/doc/book/security/csrf-protection/)
* [Cloudbees crumb documentation](https://support.cloudbees.com/hc/en-us/articles/219257077-CSRF-Protection-Explained).

## Understanding Error objects

When something pops server-side `Jenkins` will hand us back a list
of [JenkinsError](https://github.com/Hillelmed/jenkins-client-java/blob/main/src/main/java/io/github/hillelmed/jenkins/client/exception/JenkinsError.java)
objects. we're throwing an exception at runtime we attach this List of `Error` objects
The throwing object is
Jenkins [JenkinsAppException.java](https://github.com/Hillelmed/jenkins-client-java/blob/main/src/main/java/io/github/hillelmed/jenkins/client/exception/JenkinsAppException.java)
to
most [domain](https://github.com/Hillelmed/jenkins-client-java/tree/main/src/main/java/io/github/hillelmed/jenkins/client/domain)
objects. Thus, it is up to the user to check the handed back domain object to see if the attached List is empty, and if
not, iterate over the `Error` objects to see if it's something
truly warranting an exception. List of `Error` objects itself will always be non-null but in most cases empty (unless
something has failed).

An example on how one might proceed:

```
    try {
        SystemInfo systemInfo = client.api().systemApi().systemInfo();
    } catch (JenkinsAppException e) {
        for(Error error : e.errors()) {
            if (error.message().matches(".*Service unavailable.*")) {
                throw new RuntimeException(error.message());
            }
        }
    }
```

## Examples

The [mock](https://github.com/cdancy/jenkins-rest/tree/master/src/test/java/com/cdancy/jenkins/rest/features)
and [live](https://github.com/cdancy/jenkins-rest/tree/master/src/test/java/com/cdancy/jenkins/rest/features) tests
provide many examples
that you can use in your own code.

## Components

- Http Interface \- used as the backend for communicating with Jenkins REST API
- WebClient \- used as the backend for communicating with Jenkins REST API
- Spring \- used as the backend for communicating with Jenkins REST API
- Lombok \- used to create immutable value types both to and from the jenkins program

## Testing

Running mock tests can be done like so:

	./gradlew clean build mockTest

Running integration tests require an existing jenkins instance which can be obtained with docker:

    docker build -t jenkins-rest/jenkins src/main/docker
    docker run -d --rm -p 8080:8080 --name jenkins-rest jenkins-rest/jenkins
	./gradlew clean build integTest 

### Integration tests settings

If you use the provided docker instance, there is no other preparation necessary.
If you wish to run integration tests against your own Jenkins server, the requirements are outlined in the next section.

#### Jenkins instance requirements

- a running instance accessible on http://127.0.0.1:8080 (can be changed in the gradle.properties file)
- Jenkins security
    - Authorization: Anyone can do anything (to be able to test the crumb with the anonymous account)
    - an `admin` user (credentials used by the tests can be changed in the gradle.properties file) with `ADMIN` role (
      required as the tests install plugins)
    - [CSRF protection enabled](https://wiki.jenkins.io/display/JENKINS/CSRF+Protection). Not mandatory
      but [recommended by the Jenkins documentation](https://jenkins.io/doc/book/system-administration/security/#protect-users-of-jenkins-from-other-threats).
      The lib supports Jenkins instances with our without this protection (see #14)
- Plugins
    - [CloudBees Credentials](https://plugins.jenkins.io/cloudbees-credentials): otherwise an http 500 error occurs when
      accessing
      to http://127.0.0.1:8080/job/test-folder/job/test-folder-1/ `java.lang.NoClassDefFoundError: com/cloudbees/hudson/plugins/folder/properties/FolderCredentialsProvider`
    - [CloudBees Folder](https://plugins.jenkins.io/cloudbees-folder) plugin installed
    - [OWASP Markup Formatter](https://plugins.jenkins.io/antisamy-markup-formatter) configured to use `Safe HTML`
    - [Configuration As Code](https://plugins.jenkins.io/configuration-as-code) plugin installed
    - [Pipeline](https://plugins.jenkins.io/workflow-aggregator) plugin installed

This project provides instructions to setup a [pre-configured Docker container](src/main/docker/README.md)

#### Integration tests configuration

- jenkins url and authentication method used by the tests are defined in the `gradle.properties` file
- by default, tests use the `credentials` (username:password) authentication method but this can be changed to use the
  API Token. See the `gradle.properties` file.

#### Running integration tests from within your IDE

- the `integTest` gradle task sets various System Properties
- if you don't want to use gradle as tests runner in your IDE, configure the tests with the same kind of System
  Properties

# Additional Resources

* [Jenkins REST API](http://wiki.jenkins-ci.org/display/JENKINS/Remote+access+API)
* [Spring-6-http-interface](https://www.baeldung.com/spring-6-http-interface)
* [WebClient](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/function/client/WebClient.html)

