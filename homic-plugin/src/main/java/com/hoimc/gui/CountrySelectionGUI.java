package com.hoimc.gui;

import com.hoimc.HoiMCPlugin;
import com.hoimc.models.Country;
import com.hoimc.models.GovernmentType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * 국가 선택 GUI
 */
public class CountrySelectionGUI implements Listener {
    private final HoiMCPlugin plugin;
    private static final String GUI_TITLE = "§6§l국가 선택";

    public CountrySelectionGUI(HoiMCPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * 국가 선택 GUI를 엶
     */
    public void openGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, GUI_TITLE);

        List<Country> availableCountries = plugin.getCountryManager().getAvailableCountries();
        
        int slot = 10;
        for (Country country : availableCountries) {
            if (slot >= 17) break; // 한 줄에 최대 6개까지만 표시
            
            ItemStack item = createCountryItem(country);
            gui.setItem(slot, item);
            slot++;
        }

        // 닫기 버튼
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeButton.getItemMeta();
        closeMeta.displayName(Component.text("닫기", NamedTextColor.RED, TextDecoration.BOLD));
        closeButton.setItemMeta(closeMeta);
        gui.setItem(26, closeButton);

        player.openInventory(gui);
    }

    /**
     * 국가 아이템 생성
     */
    private ItemStack createCountryItem(Country country) {
        ItemStack item = new ItemStack(country.getFlagMaterial());
        ItemMeta meta = item.getItemMeta();

        // 이름 설정
        Component displayName = Component.text(country.getName(), NamedTextColor.GOLD, TextDecoration.BOLD);
        meta.displayName(displayName);

        // 설명 (Lore) 설정
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(Component.text("정부: ", NamedTextColor.GRAY)
                .append(Component.text(country.getGovernmentType().getDisplayName(), 
                        convertChatColorToNamedTextColor(country.getGovernmentType().getColor()))));
        lore.add(Component.empty());
        lore.add(Component.text("초기 자원:", NamedTextColor.YELLOW));
        
        country.getInitialResources().forEach((resource, amount) -> {
            lore.add(Component.text("  • " + resource + ": " + amount, NamedTextColor.WHITE));
        });

        if (!country.getBuffs().isEmpty()) {
            lore.add(Component.empty());
            lore.add(Component.text("국가 버프:", NamedTextColor.LIGHT_PURPLE));
            country.getBuffs().forEach((buff, value) -> {
                lore.add(Component.text("  • " + buff + ": +" + ((value - 1) * 100) + "%", NamedTextColor.GREEN));
            });
        }

        lore.add(Component.empty());
        lore.add(Component.text("클릭하여 선택", NamedTextColor.AQUA, TextDecoration.ITALIC));

        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * ChatColor를 NamedTextColor로 변환
     */
    private NamedTextColor convertChatColorToNamedTextColor(org.bukkit.ChatColor chatColor) {
        return switch (chatColor) {
            case RED, DARK_RED -> NamedTextColor.RED;
            case BLUE, DARK_BLUE -> NamedTextColor.BLUE;
            case GOLD -> NamedTextColor.GOLD;
            case GRAY, DARK_GRAY -> NamedTextColor.GRAY;
            default -> NamedTextColor.WHITE;
        };
    }

    /**
     * GUI 클릭 이벤트 처리
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().title().equals(Component.text(GUI_TITLE))) return;

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        // 닫기 버튼
        if (clickedItem.getType() == Material.BARRIER) {
            player.closeInventory();
            return;
        }

        // 국가 선택
        for (Country country : plugin.getCountryManager().getAllCountries()) {
            if (clickedItem.getType() == country.getFlagMaterial()) {
                if (plugin.getCountryManager().assignCountry(player, country.getId())) {
                    player.sendMessage(Component.text("§a✓ ", NamedTextColor.GREEN)
                            .append(Component.text(country.getName(), NamedTextColor.GOLD))
                            .append(Component.text("를 선택했습니다!", NamedTextColor.GREEN)));
                    
                    player.closeInventory();
                } else {
                    player.sendMessage(Component.text("§c✗ 이미 선택된 국가이거나 선택할 수 없습니다.", NamedTextColor.RED));
                }
                break;
            }
        }
    }
}
