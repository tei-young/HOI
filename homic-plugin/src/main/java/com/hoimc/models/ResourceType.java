package com.hoimc.models;

import org.bukkit.Material;
import org.bukkit.ChatColor;

/**
 * 게임 내 자원 종류를 정의하는 Enum
 */
public enum ResourceType {
    // 기본 자원
    IRON("철", Material.IRON_INGOT, ChatColor.GRAY, "기본 건설 및 생산에 필요"),
    COAL("석탄", Material.COAL, ChatColor.DARK_GRAY, "에너지 생산에 필요"),
    OIL("석유", Material.LAVA_BUCKET, ChatColor.DARK_RED, "고급 병력 생산에 필요"),
    WHEAT("밀", Material.WHEAT, ChatColor.YELLOW, "인구 유지에 필요"),
    
    // 고급 자원
    STEEL("강철", Material.IRON_BLOCK, ChatColor.WHITE, "고급 건설 및 병력에 필요"),
    ALUMINUM("알루미늄", Material.LIGHT_GRAY_DYE, ChatColor.AQUA, "항공기 생산에 필요"),
    RUBBER("고무", Material.BLACK_DYE, ChatColor.BLACK, "차량 생산에 필요"),
    
    // 특수 자원
    ELECTRONICS("전자제품", Material.REDSTONE, ChatColor.RED, "현대 병력에 필요"),
    MANPOWER("인력", Material.PLAYER_HEAD, ChatColor.GREEN, "병력 모집에 필요"),
    POLITICAL_POWER("정치력", Material.NETHER_STAR, ChatColor.LIGHT_PURPLE, "정책 실행에 필요");

    private final String displayName;
    private final Material material;
    private final ChatColor color;
    private final String description;

    ResourceType(String displayName, Material material, ChatColor color, String description) {
        this.displayName = displayName;
        this.material = material;
        this.color = color;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getColoredName() {
        return color + displayName;
    }
}