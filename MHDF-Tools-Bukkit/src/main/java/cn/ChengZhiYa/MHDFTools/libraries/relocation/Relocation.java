package cn.ChengZhiYa.MHDFTools.libraries.relocation;

import lombok.Getter;

import java.util.Objects;

@Getter
public final class Relocation {
    private final String pattern;
    private final String relocatedPattern;

    private Relocation(String pattern, String relocatedPattern) {
        this.pattern = pattern;
        this.relocatedPattern = relocatedPattern;
    }

    public static Relocation of(String id, String pattern) {
        return new Relocation(
                pattern.replace("{}", "."),
                "cn.ChengZhiYa.MHDFTools.utils.libraries." + id
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relocation that = (Relocation) o;
        return Objects.equals(this.pattern, that.pattern) &&
                Objects.equals(this.relocatedPattern, that.relocatedPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pattern, this.relocatedPattern);
    }
}
