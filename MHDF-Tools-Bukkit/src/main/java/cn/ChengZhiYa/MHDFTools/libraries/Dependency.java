package cn.ChengZhiYa.MHDFTools.libraries;

import cn.ChengZhiYa.MHDFTools.libraries.relocation.Relocation;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public enum Dependency {
    ASM(
            "org.ow2.asm",
            "asm",
            "9.7",
            Repository.MAVEN_CENTRAL_MIRROR,
            "asm"
    ),
    ASM_COMMONS(
            "org.ow2.asm",
            "asm-commons",
            "9.7",
            Repository.MAVEN_CENTRAL_MIRROR,
            "asm-commons"
    ),
    JAR_RELOCATOR(
            "me.lucko",
            "jar-relocator",
            "1.7",
            Repository.MAVEN_CENTRAL_MIRROR,
            "jar-relocator"
    ),


    FAST_JSON(
            "com.alibaba",
            "fastjson",
            "1.2.83",
            Repository.MAVEN_CENTRAL_MIRROR,
            "fsatjson"
    ),

    PacketEvent(
            "com.github.retrooper",
            "packetevents-spigot",
            "2.6.0",
            Repository.MAVEN_CENTRAL_MIRROR,
            "packetevent-spigot"
    );


    private final String artifact;
    private final String version;
    @Getter
    private final List<Relocation> relocations;
    @Getter
    private final Repository repository;
    @Getter
    private final String mavenRepoPath;

    Dependency(@NotNull String groupId, @NotNull String artifactId, @NotNull String version, @NotNull Repository repository, @NotNull String artifact) {
        this(groupId, artifactId, version, repository, artifact, new Relocation[0]);
    }

    Dependency(@NotNull String groupId, @NotNull String artifactId, @NotNull String version, @NotNull Repository repository, @NotNull String artifact, Relocation... relocations) {
        this.mavenRepoPath = String.format("%s/%s/%s/%s-%s.jar",
                rewriteEscaping(groupId).replace(".", "/"),
                rewriteEscaping(artifactId),
                version,
                rewriteEscaping(artifactId),
                version
        );
        this.version = version;
        this.relocations = ImmutableList.copyOf(relocations);
        this.repository = repository;
        this.artifact = artifact;
    }

    private static String rewriteEscaping(String s) {
        return s.replace("{}", ".");
    }

    public String getFileName(String classifier) {
        String name = artifact.toLowerCase(Locale.ROOT).replace('_', '-');
        String extra = classifier == null || classifier.isEmpty()
                ? ""
                : "-" + classifier;
        return name + "-" + this.version + extra + ".jar";

    }
}
