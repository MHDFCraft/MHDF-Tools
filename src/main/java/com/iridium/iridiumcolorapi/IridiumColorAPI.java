package com.iridium.iridiumcolorapi;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nonnull;

import com.iridium.iridiumcolorapi.patterns.GradientPattern;
import com.iridium.iridiumcolorapi.patterns.IPattern;
import com.iridium.iridiumcolorapi.patterns.SolidPattern;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

public class IridiumColorAPI {
    private static final int VERSION = Integer.parseInt(IridiumColorAPI.getMajorVersion(Bukkit.getVersion()).substring(2));
    private static final boolean SUPPORTSRGB = VERSION >= 16;
    private static final HashMap<Color, ChatColor> colors = new HashMap<Color, ChatColor>(){
        {
            this.put(new Color(0), ChatColor.getByChar((char)'0'));
            this.put(new Color(170), ChatColor.getByChar((char)'1'));
            this.put(new Color(43520), ChatColor.getByChar((char)'2'));
            this.put(new Color(43690), ChatColor.getByChar((char)'3'));
            this.put(new Color(0xAA0000), ChatColor.getByChar((char)'4'));
            this.put(new Color(0xAA00AA), ChatColor.getByChar((char)'5'));
            this.put(new Color(0xFFAA00), ChatColor.getByChar((char)'6'));
            this.put(new Color(0xAAAAAA), ChatColor.getByChar((char)'7'));
            this.put(new Color(0x555555), ChatColor.getByChar((char)'8'));
            this.put(new Color(0x5555FF), ChatColor.getByChar((char)'9'));
            this.put(new Color(0x55FF55), ChatColor.getByChar((char)'a'));
            this.put(new Color(0x55FFFF), ChatColor.getByChar((char)'b'));
            this.put(new Color(0xFF5555), ChatColor.getByChar((char)'c'));
            this.put(new Color(0xFF55FF), ChatColor.getByChar((char)'d'));
            this.put(new Color(0xFFFF55), ChatColor.getByChar((char)'e'));
            this.put(new Color(0xFFFFFF), ChatColor.getByChar((char)'f'));
        }
    };
    private static final List<IPattern> patterns = Arrays.asList(new GradientPattern(), new SolidPattern());

    @Nonnull
    public static String process(@Nonnull String string) {
        string = ChatColor.translateAlternateColorCodes('&', string);
        for (IPattern pattern : patterns) {
            string = pattern.process(string);
        }
        return string;
    }

    @Nonnull
    public static String color(@Nonnull String string, @Nonnull Color color) {
        return (SUPPORTSRGB ? ChatColor.of(color) : IridiumColorAPI.getClosestColor(color)) + string;
    }

    @Nonnull
    public static String color(@Nonnull String string, @Nonnull Color start, @Nonnull Color end) {
        StringBuilder stringBuilder = new StringBuilder();
        ChatColor[] colors = IridiumColorAPI.createGradient(start, end, string.length());
        String[] characters = string.split("");
        for (int i = 0; i < string.length(); ++i) {
            stringBuilder.append(colors[i]).append(characters[i]);
        }
        return stringBuilder.toString();
    }

    @Nonnull
    public static ChatColor getColor(@Nonnull String string) {
        return SUPPORTSRGB ? ChatColor.of(new Color(Integer.parseInt(string, 16))) : IridiumColorAPI.getClosestColor(new Color(Integer.parseInt(string, 16)));
    }

    @Nonnull
    private static ChatColor[] createGradient(@Nonnull Color start, @Nonnull Color end, int step) {
        ChatColor[] colors = new ChatColor[step];
        int stepR = Math.abs(start.getRed() - end.getRed()) / (step - 1);
        int stepG = Math.abs(start.getGreen() - end.getGreen()) / (step - 1);
        int stepB = Math.abs(start.getBlue() - end.getBlue()) / (step - 1);
        int[] direction = new int[]{start.getRed() < end.getRed() ? 1 : -1, start.getGreen() < end.getGreen() ? 1 : -1, start.getBlue() < end.getBlue() ? 1 : -1};
        for (int i = 0; i < step; ++i) {
            Color color = new Color(start.getRed() + stepR * i * direction[0], start.getGreen() + stepG * i * direction[1], start.getBlue() + stepB * i * direction[2]);
            colors[i] = SUPPORTSRGB ? ChatColor.of(color) : IridiumColorAPI.getClosestColor(color);
        }
        return colors;
    }

    @Nonnull
    private static ChatColor getClosestColor(Color color) {
        Color nearestColor = null;
        double nearestDistance = 2.147483647E9;
        for (Color constantColor : colors.keySet()) {
            double distance = Math.pow(color.getRed() - constantColor.getRed(), 2.0) + Math.pow(color.getGreen() - constantColor.getGreen(), 2.0) + Math.pow(color.getBlue() - constantColor.getBlue(), 2.0);
            if (!(nearestDistance > distance)) continue;
            nearestColor = constantColor;
            nearestDistance = distance;
        }
        return colors.get(nearestColor);
    }

    @Nonnull
    private static String getMajorVersion(@Nonnull String version) {
        Validate.notEmpty(version, "Cannot get major Minecraft version from null or empty string");
        int index = version.lastIndexOf("MC:");
        if (index != -1) {
            version = version.substring(index + 4, version.length() - 1);
        } else if (version.endsWith("SNAPSHOT")) {
            index = version.indexOf(45);
            version = version.substring(0, index);
        }
        int lastDot = version.lastIndexOf(46);
        if (version.indexOf(46) != lastDot) {
            version = version.substring(0, lastDot);
        }
        return version;
    }
}
