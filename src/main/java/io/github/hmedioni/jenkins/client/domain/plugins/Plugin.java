package io.github.hmedioni.jenkins.client.domain.plugins;


import lombok.*;

@Data
public class Plugin {


    public Boolean active;


    public String backupVersion;


    public Boolean bundled;


    public Boolean deleted;


    public Boolean downgradable;


    public Boolean enabled;


    public Boolean hasUpdate;


    public String longName;


    public Boolean pinned;


    public String requiredCoreVersion;


    public String shortName;


    public String supportsDynamicLoad;


    public String url;


    public String version;


//    @SerializedNames({"active", "backupVersion", "bundled",
//        "deleted", "downgradable", "enabled",
//        "hasUpdate", "longName", "pinned",
//        "requiredCoreVersion", "shortName", "supportsDynamicLoad",
//        "url", "version"})
//    public static Plugin create(Boolean active, String backupVersion, Boolean bundled,
//                                Boolean deleted, Boolean downgradable, Boolean enabled,
//                                Boolean hasUpdate, String longName, Boolean pinned,
//                                String requiredCoreVersion, String shortName, String supportsDynamicLoad,
//                                String url, String version) {
//        return new AutoValue_Plugin(active, backupVersion, bundled,
//            deleted, downgradable, enabled,
//            hasUpdate, longName, pinned,
//            requiredCoreVersion, shortName, supportsDynamicLoad,
//            url, version);
//    }
}
