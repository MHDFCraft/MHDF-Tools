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
    ),

    ORMLITE_CORE(
            "com.j256.ormlite",
            "ormlite-core",
            "6.1",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ORMLITE_JDBC(
            "com.j256.ormlite",
            "ormlite-jdbc",
            "6.1",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    HIKARI_CP(
            "com.zaxxer",
            "HikariCP",
            "6.1.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    H2_DRIVER(
            "com.h2database",
            "h2",
            "2.3.232",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    MYSQL_DRIVER(
            "com.mysql",
            "mysql-connector-j",
            "9.1.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),

    EXAMINATION_API(
            "net.kyori",
            "examination-api",
            "1.3.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    EXAMINATION_STRING(
            "net.kyori",
            "examination-string",
            "1.3.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_KEY(
            "net.kyori",
            "adventure-key",
            "4.17.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_API(
            "net.kyori",
            "adventure-api",
            "4.17.0",
            Repository.MAVEN_CENTRAL_MIRROR
    ),

    ADVENTURE_TEXT_SERIALIZER_LEGACY(
            "net.kyori",
            "adventure-text-serializer-legacy",
            "4.13.1",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_TEXT_SERIALIZER_GSON(
            "net.kyori",
            "adventure-text-serializer-gson",
            "4.13.1",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_TEXT_SERIALIZER_GSON_LEGACY_IMPL(
            "net.kyori",
            "adventure-text-serializer-gson-legacy-impl",
            "4.13.1",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_TEXT_SERIALIZER_BUNGEECORD(
            "net.kyori",
            "adventure-text-serializer-bungeecord",
            "4.3.4",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_NBT(
            "net.kyori",
            "adventure-nbt",
            "4.13.1",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_PLATFORM_API(
            "net.kyori",
            "adventure-platform-api",
            "4.3.4",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_PLATFORM_FACET(
            "net.kyori",
            "adventure-platform-facet",
            "4.3.4",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_PLATFORM_VIAVERSION(
            "net.kyori",
            "adventure-platform-viaversion",
            "4.3.4",
            Repository.MAVEN_CENTRAL_MIRROR
    ),
    ADVENTURE_PLATFORM_BUKKIT(
            "net.kyori",
            "adventure-platform-bukkit",
            "4.3.4",
            Repository.MAVEN_CENTRAL_MIRROR
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
