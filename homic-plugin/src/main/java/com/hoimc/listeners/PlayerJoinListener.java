package com.hoimc.listeners;

import com.hoimc.HoiMCPlugin;
import com.hoimc.items.CountrySelectionBook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

/**
 * 플레이어 관련 이벤트 리스너
 */
public class PlayerJoinListener implements Listener {
    private final HoiMCPlugin plugin;

    public PlayerJoinListener(HoiMCPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 플레이어가 서버에 접속할 때
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 국가를 선택하지 않은 플레이어에게 국가 선택북 지급
        if (!plugin.getCountryManager().hasCountry(player)) {
            ItemStack book = CountrySelectionBook.create(plugin.getCountryBookKey());
            player.getInventory().addItem(book);
            
            player.sendMessage(Component.empty());
            player.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY));
            player.sendMessage(Component.text("  HoiMC에 오신 것을 환영합니다!", NamedTextColor.GOLD));
            player.sendMessage(Component.empty());
            player.sendMessage(Component.text("  국가 선택북을 사용하여", NamedTextColor.YELLOW));
            player.sendMessage(Component.text("  플레이할 국가를 선택하세요!", NamedTextColor.YELLOW));
            player.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY));
            player.sendMessage(Component.empty());
        } else {
            String countryName = plugin.getCountryManager().getPlayerCountry(player).getName();
            player.sendMessage(Component.text("환영합니다! 현재 국가: ", NamedTextColor.GREEN)
                    .append(Component.text(countryName, NamedTextColor.GOLD)));
        }
    }

    /**
     * 플레이어가 아이템을 사용할 때 (우클릭)
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // 우클릭 이벤트만 처리
        if (event.getAction() != Action.RIGHT_CLICK_AIR && 
            event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // 국가 선택북인지 확인
        if (CountrySelectionBook.isCountrySelectionBook(item, plugin.getCountryBookKey())) {
            event.setCancelled(true);
            
            // 이미 국가를 선택한 경우
            if (plugin.getCountryManager().hasCountry(player)) {
                player.sendMessage(Component.text("이미 국가를 선택하셨습니다!", NamedTextColor.RED));
                return;
            }

            // 국가 선택 GUI 열기
            plugin.getCountrySelectionGUI().openGUI(player);
        }
    }
}
