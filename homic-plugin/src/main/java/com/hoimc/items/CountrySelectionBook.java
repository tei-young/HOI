package com.hoimc.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;

/**
 * 국가 선택북 아이템
 */
public class CountrySelectionBook {
    private static final String ITEM_ID = "country_selection_book";

    /**
     * 국가 선택북 아이템 생성
     */
    public static ItemStack create(NamespacedKey key) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta meta = book.getItemMeta();

        // 아이템 이름
        meta.displayName(Component.text("국가 선택북", NamedTextColor.GOLD, TextDecoration.BOLD));

        // 설명 (Lore)
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(Component.text("우클릭하여 국가를 선택하세요", NamedTextColor.YELLOW));
        lore.add(Component.empty());
        lore.add(Component.text("국가를 선택하면 HOI 전략 게임을", NamedTextColor.GRAY));
        lore.add(Component.text("시작할 수 있습니다!", NamedTextColor.GRAY));
        lore.add(Component.empty());
        lore.add(Component.text("§6§l[우클릭하여 열기]", NamedTextColor.GOLD, TextDecoration.BOLD));

        meta.lore(lore);

        // 커스텀 아이템 식별자 추가
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, ITEM_ID);

        book.setItemMeta(meta);
        return book;
    }

    /**
     * 해당 아이템이 국가 선택북인지 확인
     */
    public static boolean isCountrySelectionBook(ItemStack item, NamespacedKey key) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            return false;
        }
        
        String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return ITEM_ID.equals(id);
    }
}
