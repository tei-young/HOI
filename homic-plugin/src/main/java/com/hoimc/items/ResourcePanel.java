package com.hoimc.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * 자원 패널 아이템
 * 우클릭하면 자원 현황을 볼 수 있는 GUI가 열림
 */
public class ResourcePanel {
    private static final String ITEM_ID = "resource_panel";

    /**
     * 자원 패널 아이템 생성
     */
    public static ItemStack create(NamespacedKey key) {
        ItemStack panel = new ItemStack(Material.CHEST);
        ItemMeta meta = panel.getItemMeta();

        // 아이템 이름
        meta.displayName(Component.text("자원 패널", NamedTextColor.GREEN, TextDecoration.BOLD));

        // 설명 (Lore)
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(Component.text("현재 보유 중인 자원을 확인합니다", NamedTextColor.GRAY));
        lore.add(Component.empty());
        lore.add(Component.text("• 자원 보유량", NamedTextColor.YELLOW));
        lore.add(Component.text("• 자원 생산 현황", NamedTextColor.YELLOW));
        lore.add(Component.text("• 지역 정보", NamedTextColor.YELLOW));
        lore.add(Component.empty());
        lore.add(Component.text("§a§l[우클릭하여 열기]", NamedTextColor.GREEN, TextDecoration.BOLD));

        meta.lore(lore);

        // 커스텀 아이템 식별자 추가
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, ITEM_ID);

        panel.setItemMeta(meta);
        return panel;
    }

    /**
     * 해당 아이템이 자원 패널인지 확인
     */
    public static boolean isResourcePanel(ItemStack item, NamespacedKey key) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            return false;
        }
        
        String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return ITEM_ID.equals(id);
    }
}