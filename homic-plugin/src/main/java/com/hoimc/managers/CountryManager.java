package com.hoimc.managers;

import com.hoimc.HoiMCPlugin;
import com.hoimc.models.Country;
import com.hoimc.models.GovernmentType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * 국가 데이터 및 플레이어-국가 매핑을 관리하는 Manager
 */
public class CountryManager {
    private final HoiMCPlugin plugin;
    private final Map<String, Country> countries;
    private final Map<UUID, Country> playerCountries;

    public CountryManager(HoiMCPlugin plugin) {
        this.plugin = plugin;
        this.countries = new HashMap<>();
        this.playerCountries = new HashMap<>();
        initializeCountries();
    }

    /**
     * 기본 국가들을 초기화
     */
    private void initializeCountries() {
        // 독일
        Country germany = new Country.Builder("germany", "독일", GovernmentType.FASCISM, Material.BLACK_BANNER)
                .addResource("iron", 1000)
                .addResource("coal", 800)
                .addBuff("production_speed", 1.2)
                .build();

        // 소련
        Country soviet = new Country.Builder("soviet", "소련", GovernmentType.COMMUNISM, Material.RED_BANNER)
                .addResource("iron", 900)
                .addResource("oil", 700)
                .addBuff("manpower", 1.5)
                .build();

        // 미국
        Country usa = new Country.Builder("usa", "미국", GovernmentType.DEMOCRACY, Material.BLUE_BANNER)
                .addResource("iron", 800)
                .addResource("oil", 1000)
                .addBuff("research_speed", 1.3)
                .build();

        // 영국
        Country uk = new Country.Builder("uk", "영국", GovernmentType.DEMOCRACY, Material.CYAN_BANNER)
                .addResource("iron", 700)
                .addResource("coal", 600)
                .addBuff("naval_power", 1.4)
                .build();

        // 일본
        Country japan = new Country.Builder("japan", "일본", GovernmentType.FASCISM, Material.WHITE_BANNER)
                .addResource("iron", 600)
                .addResource("oil", 400)
                .addBuff("army_speed", 1.2)
                .build();

        // 한국
        Country korea = new Country.Builder("korea", "한국", GovernmentType.DEMOCRACY, Material.LIGHT_BLUE_BANNER)
                .addResource("wheat", 500)
                .addResource("iron", 400)
                .addBuff("defense", 1.1)
                .build();

        // 국가 등록
        registerCountry(germany);
        registerCountry(soviet);
        registerCountry(usa);
        registerCountry(uk);
        registerCountry(japan);
        registerCountry(korea);

        plugin.getLogger().info("Initialized " + countries.size() + " countries");
    }

    /**
     * 국가를 시스템에 등록
     */
    public void registerCountry(Country country) {
        countries.put(country.getId(), country);
    }

    /**
     * 플레이어에게 국가를 할당
     */
    public boolean assignCountry(Player player, String countryId) {
        Country country = countries.get(countryId);
        if (country == null) {
            return false;
        }

        // 이미 다른 플레이어가 선택한 국가인지 확인
        if (country.getOwnerId() != null) {
            return false;
        }

        // 플레이어가 이미 다른 국가를 가지고 있는지 확인
        if (playerCountries.containsKey(player.getUniqueId())) {
            Country oldCountry = playerCountries.get(player.getUniqueId());
            oldCountry.setOwner(null, null);
        }

        // 국가 할당
        country.setOwner(player.getUniqueId(), player.getName());
        playerCountries.put(player.getUniqueId(), country);

        plugin.getLogger().info(player.getName() + " selected country: " + country.getName());
        return true;
    }

    /**
     * 플레이어의 국가를 가져옴
     */
    public Country getPlayerCountry(Player player) {
        return playerCountries.get(player.getUniqueId());
    }

    /**
     * 플레이어의 국가를 가져옴 (UUID)
     */
    public Country getPlayerCountry(UUID playerId) {
        return playerCountries.get(playerId);
    }

    /**
     * ID로 국가를 가져옴
     */
    public Country getCountry(String countryId) {
        return countries.get(countryId);
    }

    /**
     * 모든 국가 목록을 가져옴
     */
    public Collection<Country> getAllCountries() {
        return countries.values();
    }

    /**
     * 선택 가능한 국가 목록 (아직 선택되지 않은 국가)
     */
    public List<Country> getAvailableCountries() {
        List<Country> available = new ArrayList<>();
        for (Country country : countries.values()) {
            if (country.getOwnerId() == null) {
                available.add(country);
            }
        }
        return available;
    }

    /**
     * 플레이어가 국가를 선택했는지 확인
     */
    public boolean hasCountry(Player player) {
        return playerCountries.containsKey(player.getUniqueId());
    }

    /**
     * 플레이어의 국가 선택 해제
     */
    public void removePlayerCountry(Player player) {
        Country country = playerCountries.remove(player.getUniqueId());
        if (country != null) {
            country.setOwner(null, null);
        }
    }
}
