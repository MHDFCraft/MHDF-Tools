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
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ASM_COMMONS(
            "org.ow2.asm",
            "asm-commons",
            "9.7",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    JAR_RELOCATOR(
            "me.lucko",
            "jar-relocator",
            "1.7",
            Repository.MAVEN_CENTRAL_MIRROR
    ),


    FAST_JSON(
            "com.alibaba",
            "fastjson",
            "1.2.83",
            Repository.MAVEN_CENTRAL_MIRROR
    ),

    REFLECTIONS(
            "org.reflections",
            "reflections",
            "0.10.2",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    JAVASSITST(
            "org.javassist",
            "javassist",
            "3.28.0-GA",
            Repository.MAVEN_CENTRAL_MIRROR
    ),

    PACKETEVENTS_API(
            "com.github.retrooper",
            "packetevents-api",
            "2.6.0",
            Repository.CODE_MC
    ),
    PACKETEVENTS_NETTY_COMMON(
            "com.github.retrooper",
            "packetevents-netty-common",
            "2.6.0",
            Repository.CODE_MC
    ),
    PACKETEVENTS_SPIGOT(
            "com.github.retrooper",
            "packetevents-spigot",
            "2.6.0",
            Repository.CODE_MC
    ),

    ITEM_NBT_API(
            "de.tr7zw",
            "item-nbt-api",
            "2.14.0",
            Repository.CODE_MC
    );


    private final String artifact;
    private final String version;
    @Getter
    private final List<Relocation> relocations;
    @Getter
    private final Repository repository;
    @Getter
    private final String mavenRepoPath;

    Dependency(@NotNull String groupId, @NotNull String artifactId, @NotNull String version, @NotNull Repository repository) {
        this(groupId, artifactId, version, repository, new Relocation[0]);
    }

    Dependency(@NotNull String groupId, @NotNull String artifactId, @NotNull String version, @NotNull Repository repository, Relocation... relocations) {
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
        this.artifact = artifactId;
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
