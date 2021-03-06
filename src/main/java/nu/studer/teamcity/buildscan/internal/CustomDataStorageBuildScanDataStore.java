package nu.studer.teamcity.buildscan.internal;

import jetbrains.buildServer.serverSide.CustomDataStorage;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.util.StringUtil;
import nu.studer.teamcity.buildscan.BuildScanDataStore;
import nu.studer.teamcity.buildscan.BuildScanReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static nu.studer.teamcity.buildscan.internal.Util.getBuildScanId;

public final class CustomDataStorageBuildScanDataStore implements BuildScanDataStore {

    private static final String BUILD_SCAN_STORAGE_ID = "nu.studer.teamcity.buildscan.DefaultBuildScanDataStore";
    private static final String BUILD_SCAN_URLS_SEPARATOR = "|";

    @Override
    public void mark(SBuild build) {
        throw new UnsupportedOperationException("Writing to the custom data storage is not supported anymore");
    }

    @Override
    public void store(SBuild build, String buildScanUrl) {
        throw new UnsupportedOperationException("Writing to the custom data storage is not supported anymore");
    }

    @Override
    public List<BuildScanReference> fetch(SBuild build) {
        String buildId = String.valueOf(build.getBuildId());
        CustomDataStorage customDataStorage = getCustomDataStorage(build);
        String existing = customDataStorage.getValue(buildId);

        if (existing == null) {
            return null;
        } else {
            List<BuildScanReference> buildScanReferences = new ArrayList<>();
            List<String> scans = StringUtil.split(existing, BUILD_SCAN_URLS_SEPARATOR);
            for (String scanUrl : scans) {
                if (!scanUrl.isEmpty()) {
                    buildScanReferences.add(new BuildScanReference(getBuildScanId(scanUrl), scanUrl));
                }
            }

            return buildScanReferences;
        }
    }

    @NotNull
    private CustomDataStorage getCustomDataStorage(SBuild build) {
        //noinspection ConstantConditions
        return build.getBuildType().getCustomDataStorage(BUILD_SCAN_STORAGE_ID);
    }

}
