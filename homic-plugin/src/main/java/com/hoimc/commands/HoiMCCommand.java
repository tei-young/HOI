package com.hoimc.commands;

import com.hoimc.HoiMCPlugin;
import com.hoimc.items.CountrySelectionBook;
import com.hoimc.models.Country;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * HoiMC 명령어 처리
 */
public class HoiMCCommand implements CommandExecutor, TabCompleter {
    private final HoiMCPlugin plugin;

    public HoiMCCommand(HoiMCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, 
                           @NotNull String label, @NotNull String[] args) {
        
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c이 명령어는 플레이어만 사용할 수 있습니다.");
            return true;
        }

        // /country 명령어
        if (command.getName().equalsIgnoreCase("country")) {
            return handleCountryCommand(player, args);
        }

        // /hoimc 명령어
        if (command.getName().equalsIgnoreCase("hoimc")) {
            return handleHoiMCCommand(player, args);
        }

        return false;
    }

    /**
     * /country 명령어 처리
     */
    private boolean handleCountryCommand(Player player, String[] args) {
        if (args.length == 0) {
            sendCountryHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "select":
                plugin.getCountrySelectionGUI().openGUI(player);
                return true;

            case "info":
                showCountryInfo(player);
                return true;

            case "list":
                showCountryList(player);
                return true;

            case "reset":
                if (player.hasPermission("hoimc.admin")) {
                    plugin.getCountryManager().removePlayerCountry(player);
                    player.sendMessage(Component.text("국가 선택이 초기화되었습니다.", NamedTextColor.GREEN));
                } else {
                    player.sendMessage(Component.text("권한이 없습니다.", NamedTextColor.RED));
                }
                return true;

            case "givebook":
                if (player.hasPermission("hoimc.admin")) {
                    player.getInventory().addItem(CountrySelectionBook.create(plugin.getCountryBookKey()));
                    player.sendMessage(Component.text("국가 선택북을 받았습니다.", NamedTextColor.GREEN));
                } else {
                    player.sendMessage(Component.text("권한이 없습니다.", NamedTextColor.RED));
                }
                return true;

            default:
                sendCountryHelp(player);
                return true;
        }
    }

    /**
     * /hoimc 명령어 처리
     */
    private boolean handleHoiMCCommand(Player player, String[] args) {
        if (args.length == 0) {
            sendHoiMCHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                sendHoiMCHelp(player);
                return true;

            case "info":
                sendPluginInfo(player);
                return true;

            default:
                sendHoiMCHelp(player);
                return true;
        }
    }

    /**
     * 국가 명령어 도움말 표시
     */
    private void sendCountryHelp(Player player) {
        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("━━━━━━━ ", NamedTextColor.DARK_GRAY)
                .append(Component.text("국가 명령어", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" ━━━━━━━", NamedTextColor.DARK_GRAY)));
        player.sendMessage(Component.text("/country select", NamedTextColor.YELLOW)
                .append(Component.text(" - 국가 선택 GUI 열기", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/country info", NamedTextColor.YELLOW)
                .append(Component.text(" - 내 국가 정보 보기", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/country list", NamedTextColor.YELLOW)
                .append(Component.text(" - 모든 국가 목록", NamedTextColor.GRAY)));
        
        if (player.hasPermission("hoimc.admin")) {
            player.sendMessage(Component.text("/country reset", NamedTextColor.RED)
                    .append(Component.text(" - 국가 선택 초기화", NamedTextColor.GRAY)));
            player.sendMessage(Component.text("/country givebook", NamedTextColor.RED)
                    .append(Component.text(" - 선택북 지급", NamedTextColor.GRAY)));
        }
        
        player.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY));
        player.sendMessage(Component.empty());
    }

    /**
     * HoiMC 명령어 도움말 표시
     */
    private void sendHoiMCHelp(Player player) {
        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("━━━━━━━ ", NamedTextColor.DARK_GRAY)
                .append(Component.text("HoiMC 명령어", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" ━━━━━━━", NamedTextColor.DARK_GRAY)));
        player.sendMessage(Component.text("/hoimc help", NamedTextColor.YELLOW)
                .append(Component.text(" - 도움말 표시", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("/hoimc info", NamedTextColor.YELLOW)
                .append(Component.text(" - 플러그인 정보", NamedTextColor.GRAY)));
        player.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY));
        player.sendMessage(Component.empty());
    }

    /**
     * 현재 플레이어의 국가 정보 표시
     */
    private void showCountryInfo(Player player) {
        Country country = plugin.getCountryManager().getPlayerCountry(player);
        
        if (country == null) {
            player.sendMessage(Component.text("아직 국가를 선택하지 않았습니다.", NamedTextColor.RED));
            player.sendMessage(Component.text("/country select", NamedTextColor.YELLOW)
                    .append(Component.text(" 명령어로 국가를 선택하세요.", NamedTextColor.GRAY)));
            return;
        }

        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("━━━━━━━ ", NamedTextColor.DARK_GRAY)
                .append(Component.text("국가 정보", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" ━━━━━━━", NamedTextColor.DARK_GRAY)));
        player.sendMessage(Component.text("국가명: ", NamedTextColor.GRAY)
                .append(Component.text(country.getName(), NamedTextColor.GOLD)));
        player.sendMessage(Component.text("정부: ", NamedTextColor.GRAY)
                .append(Component.text(country.getGovernmentType().getDisplayName(), NamedTextColor.YELLOW)));
        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("초기 자원:", NamedTextColor.AQUA));
        country.getInitialResources().forEach((resource, amount) -> {
            player.sendMessage(Component.text("  • " + resource + ": " + amount, NamedTextColor.WHITE));
        });
        
        if (!country.getBuffs().isEmpty()) {
            player.sendMessage(Component.empty());
            player.sendMessage(Component.text("국가 버프:", NamedTextColor.LIGHT_PURPLE));
            country.getBuffs().forEach((buff, value) -> {
                player.sendMessage(Component.text("  • " + buff + ": +" + 
                        String.format("%.0f", (value - 1) * 100) + "%", NamedTextColor.GREEN));
            });
        }
        
        player.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY));
        player.sendMessage(Component.empty());
    }

    /**
     * 모든 국가 목록 표시
     */
    private void showCountryList(Player player) {
        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("━━━━━━━ ", NamedTextColor.DARK_GRAY)
                .append(Component.text("국가 목록", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" ━━━━━━━", NamedTextColor.DARK_GRAY)));
        
        for (Country country : plugin.getCountryManager().getAllCountries()) {
            String status = country.getOwnerId() == null ? "§a사용 가능" : "§c선택됨 (" + country.getOwnerName() + ")";
            player.sendMessage(Component.text("• ", NamedTextColor.GRAY)
                    .append(Component.text(country.getName(), NamedTextColor.YELLOW))
                    .append(Component.text(" - " + status, NamedTextColor.WHITE)));
        }
        
        player.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY));
        player.sendMessage(Component.empty());
    }

    /**
     * 플러그인 정보 표시
     */
    private void sendPluginInfo(Player player) {
        player.sendMessage(Component.empty());
        player.sendMessage(Component.text("━━━━━━━ ", NamedTextColor.DARK_GRAY)
                .append(Component.text("HoiMC Plugin", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.text(" ━━━━━━━", NamedTextColor.DARK_GRAY)));
        player.sendMessage(Component.text("버전: ", NamedTextColor.GRAY)
                .append(Component.text("1.0.0-SNAPSHOT", NamedTextColor.YELLOW)));
        player.sendMessage(Component.text("개발: ", NamedTextColor.GRAY)
                .append(Component.text("HoiMC Team", NamedTextColor.YELLOW)));
        player.sendMessage(Component.text("설명: ", NamedTextColor.GRAY)
                .append(Component.text("HOI 스타일 전략 게임", NamedTextColor.YELLOW)));
        player.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY));
        player.sendMessage(Component.empty());
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, 
                                                @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("country")) {
            if (args.length == 1) {
                completions.addAll(Arrays.asList("select", "info", "list"));
                if (sender.hasPermission("hoimc.admin")) {
                    completions.addAll(Arrays.asList("reset", "givebook"));
                }
            }
        } else if (command.getName().equalsIgnoreCase("hoimc")) {
            if (args.length == 1) {
                completions.addAll(Arrays.asList("help", "info"));
            }
        }

        return completions;
    }
}
