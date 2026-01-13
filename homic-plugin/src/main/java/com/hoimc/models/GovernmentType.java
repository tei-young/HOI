package com.hoimc.models;

import org.bukkit.ChatColor;

/**
 * 국가의 정부 형태를 나타내는 Enum
 */
public enum GovernmentType {
    DEMOCRACY("민주주의", ChatColor.BLUE),
    COMMUNISM("공산주의", ChatColor.RED),
    FASCISM("파시즘", ChatColor.DARK_RED),
    MONARCHY("군주제", ChatColor.GOLD),
    NEUTRAL("중립", ChatColor.GRAY);

    private final String displayName;
    private final ChatColor color;

    GovernmentType(String displayName, ChatColor color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getColoredName() {
        return color + displayName;
    }
}
