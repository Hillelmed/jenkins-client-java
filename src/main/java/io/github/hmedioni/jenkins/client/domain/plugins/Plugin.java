package io.github.hmedioni.jenkins.client.domain.plugins;


import lombok.*;

@Data
@NoArgsConstructor
public class Plugin {


    private Boolean active;


    private String backupVersion;


    private Boolean bundled;


    private Boolean deleted;


    private Boolean downgradable;


    private Boolean enabled;


    private Boolean hasUpdate;


    private String longName;


    private Boolean pinned;


    private String requiredCoreVersion;


    private String shortName;


    private String supportsDynamicLoad;


    private String url;


    private String version;


}
