package io.github.hmedioni.jenkins.client;

import io.github.hmedioni.jenkins.client.features.*;

import java.io.*;

public interface JenkinsApi extends Closeable {


    CrumbIssuerApi crumbIssuerApi();


    JobsApi jobsApi();


    PluginManagerApi pluginManagerApi();


    QueueApi queueApi();


    StatisticsApi statisticsApi();


    SystemApi systemApi();


    ConfigurationAsCodeApi configurationAsCodeApi();


    UserApi userApi();
}
