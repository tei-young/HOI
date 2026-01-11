package com.hoimc;

import com.hoimc.gui.CountrySelectionGUI;
import com.hoimc.listeners.PlayerJoinListener;
import com.hoimc.managers.CountryManager;
import com.hoimc.commands.HoiMCCommand;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * HoiMC 플러그인 메인 클래스
 */
public class HoiMCPlugin extends JavaPlugin {
    
    // Managers
    private CountryManager countryManager;
    private ResourceManager resourceManager;
    private RegionManager regionManager;
    
    // GUIs
    private CountrySelectionGUI countrySelectionGUI;
    private ResourceGUI resourceGUI;
    
    // Tasks
    private ResourceProductionTask resourceProductionTask;
    
    // Keys
    private NamespacedKey countryBookKey;
    private NamespacedKey resourcePanelKey;  

    @Override
    public void onEnable() {
        getLogger().info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        getLogger().info("  HoiMC Plugin Starting...");
        getLogger().info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // NamespacedKeys 초기화
        this.countryBookKey = new NamespacedKey(this, "country_selection_book");

        // Managers 초기화
        getLogger().info("Initializing Managers...");
        this.countryManager = new CountryManager(this);

        // GUIs 초기화
        getLogger().info("Initializing GUIs...");
        this.countrySelectionGUI = new CountrySelectionGUI(this);

        // 리스너 등록
        getLogger().info("Registering Listeners...");
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        // 명령어 등록
        getLogger().info("Registering Commands...");
        HoiMCCommand hoiMCCommand = new HoiMCCommand(this);
        getCommand("hoimc").setExecutor(hoiMCCommand);
        getCommand("country").setExecutor(hoiMCCommand);

        getLogger().info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        getLogger().info("  HoiMC Plugin Enabled Successfully!");
        getLogger().info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    @Override
    public void onDisable() {
        getLogger().info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        getLogger().info("  HoiMC Plugin Shutting Down...");
        getLogger().info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // 정리 작업
        if (countryManager != null) {
            // 필요한 경우 저장 로직 추가
        }

        getLogger().info("  HoiMC Plugin Disabled!");
    }
    // Getters
    public CountryManager getCountryManager() {
        return countryManager;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public CountrySelectionGUI getCountrySelectionGUI() {
        return countrySelectionGUI;
    }

    public ResourceGUI getResourceGUI() {
        return resourceGUI;
    }

    public NamespacedKey getCountryBookKey() {
        return countryBookKey;
    }

    public NamespacedKey getResourcePanelKey() {
        return resourcePanelKey;
    }
}
