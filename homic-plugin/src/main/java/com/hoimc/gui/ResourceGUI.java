package com.hoimc.gui;

import com.hoimc.HoiMCPlugin;
import com.hoimc.models.Country;
import com.hoimc.models.Region;
import com.hoimc.models.ResourceType;
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
import java.util.Map;

/**
 * 자원 현황 GUI
 */
public class ResourceGUI implements Listener {
    private final HoiMCPlugin plugin;
    private static final String GUI_TITLE = "§2§l자원 현황";

    public ResourceGUI(HoiMCPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * 자원 GUI를 엶
     */
    public void openGUI(Player player) {
        Country country = plugin.getCountryManager().getPlayerCountry(player);
        if (country == null) {
            player.sendMessage(Component.text("먼저 국가를 선택해야 합니다!", NamedTextColor.RED));
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE);

        // 자원 표시 (상단 2줄)
        displayResources(gui, player);

        // 지역 정보 (하단)
        displayRegions(gui, player);

        // 닫기 버튼
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeButton.getItemMeta();
        closeMeta.displayName(Component.text("닫기", NamedTextColor.RED, TextDecoration.BOLD));
        closeButton.setItemMeta(closeMeta);
        gui.setItem(53, closeButton);

        player.openInventory(gui);
    }

    /**
     * 자원 표시
     */
    private void displayResources(Inventory gui, Player player) {
        Map<ResourceType, Integer> resources = plugin.getResourceManager().getAllResources(player.getUniqueId());
        
        int slot = 0;
        for (ResourceType type : ResourceType.values()) {
            if (slot >= 18) break; // 2줄까지만
            
            int amount = resources.getOrDefault(type, 0);
            int capacity = plugin.getResourceManager().getResourceCapacity(player.getUniqueId(), type);
            
            ItemStack item = createResourceItem(type, amount, capacity, player);
            gui.setItem(slot, item);
            slot++;
        }
    }

    /**
     * 자원 아이템 생성
     */
    private ItemStack createResourceItem(ResourceType type, int amount, int capacity, Player player) {
        ItemStack item = new ItemStack(type.getMaterial());
        ItemMeta meta = item.getItemMeta();

        // 이름
        meta.displayName(Component.text(type.getDisplayName(), 
                convertToNamedTextColor(type.getColor()), TextDecoration.BOLD));

        // 설명
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(Component.text("보유량: " + formatNumber(amount) + " / " + formatNumber(capacity), 
                NamedTextColor.WHITE));
        
        // 퍼센트 바
        double percentage = capacity > 0 ? (double) amount / capacity * 100 : 0;
        lore.add(Component.text(createProgressBar(percentage), NamedTextColor.GRAY));
        
        lore.add(Component.empty());
        lore.add(Component.text(type.getDescription(), NamedTextColor.GRAY, TextDecoration.ITALIC));
        
        // 생산 정보
        int totalProduction = calculateTotalProduction(player, type);
        if (totalProduction > 0) {
            lore.add(Component.empty());
            lore.add(Component.text("생산량: +" + totalProduction + "/5초", NamedTextColor.GREEN));
        }

        meta.lore(lore);
        item.setItemMeta(meta);
        item.setAmount(Math.min(Math.max(1, amount / 100), 64)); // 시각적 표시

        return item;
    }

    /**
     * 지역 정보 표시
     */
    private void displayRegions(Inventory gui, Player player) {
        List<Region> regions = plugin.getRegionManager().getRegionsByPlayer(player.getUniqueId());
        
        int slot = 27; // 3번째 줄부터
        for (Region region : regions) {
            if (slot >= 53) break;
            
            ItemStack item = createRegionItem(region);
            gui.setItem(slot, item);
            slot++;
        }

        // 지역이 없을 경우
        if (regions.isEmpty()) {
            ItemStack noRegion = new ItemStack(Material.BARRIER);
            ItemMeta meta = noRegion.getItemMeta();
            meta.displayName(Component.text("소유 지역 없음", NamedTextColor.RED));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.empty());
            lore.add(Component.text("아직 점령한 지역이 없습니다", NamedTextColor.GRAY));
            meta.lore(lore);
            noRegion.setItemMeta(meta);
            gui.setItem(27, noRegion);
        }
    }

    /**
     * 지역 아이템 생성
     */
    private ItemStack createRegionItem(Region region) {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(region.getName(), NamedTextColor.GOLD, TextDecoration.BOLD));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(Component.text("인구: " + formatNumber(region.getPopulation()), NamedTextColor.WHITE));
        lore.add(Component.text("인프라: " + region.getInfrastructure() + "/10", NamedTextColor.AQUA));
        
        if (region.isContested()) {
            lore.add(Component.text("⚔ 전투 중!", NamedTextColor.RED, TextDecoration.BOLD));
        }
        
        lore.add(Component.empty());
        lore.add(Component.text("생산:", NamedTextColor.YELLOW));
        
        region.getResourceProduction().forEach((type, amount) -> {
            int effective = region.getEffectiveProduction(type);
            lore.add(Component.text("  • " + type.getDisplayName() + ": " + effective + "/5초", 
                    NamedTextColor.WHITE));
        });

        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * 플레이어의 총 자원 생산량 계산
     */
    private int calculateTotalProduction(Player player, ResourceType type) {
        List<Region> regions = plugin.getRegionManager().getRegionsByPlayer(player.getUniqueId());
        int total = 0;
        
        for (Region region : regions) {
            total += region.getEffectiveProduction(type);
        }
        
        // 국가 버프 적용
        Country country = plugin.getCountryManager().getPlayerCountry(player);
        if (country != null) {
            double bonus = country.getBuffs().getOrDefault("production_speed", 1.0);
            total = (int) (total * bonus);
        }
        
        return total;
    }

    /**
     * 숫자 포맷팅 (1000 -> 1K, 1000000 -> 1M)
     */
    private String formatNumber(int number) {
        if (number >= 1000000) {
            return String.format("%.1fM", number / 1000000.0);
        } else if (number >= 1000) {
            return String.format("%.1fK", number / 1000.0);
        }
        return String.valueOf(number);
    }

    /**
     * 진행 바 생성 (0-100%)
     */
    private String createProgressBar(double percentage) {
        int filled = (int) (percentage / 10);
        int empty = 10 - filled;
        
        StringBuilder bar = new StringBuilder("§7[");
        
        if (percentage >= 90) {
            bar.append("§c");
        } else if (percentage >= 70) {
            bar.append("§e");
        } else {
            bar.append("§a");
        }
        
        bar.append("■".repeat(Math.max(0, filled)));
        bar.append("§8");
        bar.append("□".repeat(Math.max(0, empty)));
        bar.append("§7] ");
        bar.append(String.format("%.1f%%", percentage));
        
        return bar.toString();
    }

    /**
     * ChatColor를 NamedTextColor로 변환
     */
    private NamedTextColor convertToNamedTextColor(org.bukkit.ChatColor chatColor) {
        return switch (chatColor) {
            case RED, DARK_RED -> NamedTextColor.RED;
            case GREEN, DARK_GREEN -> NamedTextColor.GREEN;
            case BLUE, DARK_BLUE -> NamedTextColor.BLUE;
            case YELLOW, GOLD -> NamedTextColor.YELLOW;
            case AQUA, DARK_AQUA -> NamedTextColor.AQUA;
            case LIGHT_PURPLE, DARK_PURPLE -> NamedTextColor.LIGHT_PURPLE;
            case WHITE -> NamedTextColor.WHITE;
            case GRAY, DARK_GRAY -> NamedTextColor.GRAY;
            case BLACK -> NamedTextColor.BLACK;
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
        }
    }
}