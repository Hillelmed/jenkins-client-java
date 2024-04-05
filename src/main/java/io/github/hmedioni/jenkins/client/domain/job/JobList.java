package io.github.hmedioni.jenkins.client.domain.job;


import lombok.*;

import java.util.*;

@Data
public class JobList {


    public String clazz;

    public List<Job> jobs;


    public String url;


//    @SerializedNames({"_class", "jobs", "url"})
//    public static JobList create(final String clazz, final List<Job> jobs, final String url) {
//        return new AutoValue_JobList(clazz, jobs, url);
//    }
}
