package com.hoimc.managers;

import com.hoimc.HoiMCPlugin;
import com.hoimc.models.Region;
import com.hoimc.models.ResourceType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 지역(Region) 데이터를 관리하는 Manager
 */
public class RegionManager {
    private final HoiMCPlugin plugin;
    private final Map<String, Region> regions;

    public RegionManager(HoiMCPlugin plugin) {
        this.plugin = plugin;
        this.regions = new HashMap<>();
        initializeRegions();
    }

    /**
     * 기본 지역들을 초기화
     */
    private void initializeRegions() {
        World world = Bukkit.getWorlds().get(0); // 메인 월드
        
        // 독일 영토
        Region berlin = new Region.Builder("berlin", "베를린", new Location(world, 0, 64, 0))
                .setResourceProduction(ResourceType.IRON, 10)
                .setResourceProduction(ResourceType.COAL, 8)
                .setResourceProduction(ResourceType.MANPOWER, 5)
                .setPopulation(5000000)
                .setInfrastructure(8)
                .build();
        
        Region ruhr = new Region.Builder("ruhr", "루르", new Location(world, -500, 64, 0))
                .setResourceProduction(ResourceType.IRON, 15)
                .setResourceProduction(ResourceType.COAL, 20)
                .setResourceProduction(ResourceType.STEEL, 10)
                .setPopulation(3000000)
                .setInfrastructure(7)
                .build();
        
        // 소련 영토
        Region moscow = new Region.Builder("moscow", "모스크바", new Location(world, 2000, 64, 0))
                .setResourceProduction(ResourceType.IRON, 12)
                .setResourceProduction(ResourceType.MANPOWER, 8)
                .setResourceProduction(ResourceType.POLITICAL_POWER, 3)
                .setPopulation(8000000)
                .setInfrastructure(6)
                .build();
        
        Region baku = new Region.Builder("baku", "바쿠", new Location(world, 2500, 64, 500))
                .setResourceProduction(ResourceType.OIL, 25)
                .setResourceProduction(ResourceType.RUBBER, 5)
                .setPopulation(1000000)
                .setInfrastructure(5)
                .build();
        
        // 미국 영토
        Region washington = new Region.Builder("washington", "워싱턴", new Location(world, -3000, 64, 0))
                .setResourceProduction(ResourceType.POLITICAL_POWER, 5)
                .setResourceProduction(ResourceType.MANPOWER, 4)
                .setResourceProduction(ResourceType.ELECTRONICS, 8)
                .setPopulation(4000000)
                .setInfrastructure(9)
                .build();
        
        Region texas = new Region.Builder("texas", "텍사스", new Location(world, -3500, 64, -500))
                .setResourceProduction(ResourceType.OIL, 30)
                .setResourceProduction(ResourceType.IRON, 10)
                .setResourceProduction(ResourceType.MANPOWER, 3)
                .setPopulation(2500000)
                .setInfrastructure(7)
                .build();
        
        // 영국 영토
        Region london = new Region.Builder("london", "런던", new Location(world, -1000, 64, 0))
                .setResourceProduction(ResourceType.POLITICAL_POWER, 4)
                .setResourceProduction(ResourceType.ELECTRONICS, 6)
                .setResourceProduction(ResourceType.MANPOWER, 4)
                .setPopulation(7000000)
                .setInfrastructure(9)
                .build();
        
        Region manchester = new Region.Builder("manchester", "맨체스터", new Location(world, -1100, 64, 100))
                .setResourceProduction(ResourceType.COAL, 15)
                .setResourceProduction(ResourceType.IRON, 8)
                .setResourceProduction(ResourceType.STEEL, 6)
                .setPopulation(2000000)
                .setInfrastructure(7)
                .build();
        
        // 일본 영토
        Region tokyo = new Region.Builder("tokyo", "도쿄", new Location(world, 5000, 64, 0))
                .setResourceProduction(ResourceType.ELECTRONICS, 10)
                .setResourceProduction(ResourceType.MANPOWER, 6)
                .setResourceProduction(ResourceType.POLITICAL_POWER, 3)
                .setPopulation(9000000)
                .setInfrastructure(8)
                .build();
        
        Region osaka = new Region.Builder("osaka", "오사카", new Location(world, 5200, 64, 200))
                .setResourceProduction(ResourceType.STEEL, 8)
                .setResourceProduction(ResourceType.IRON, 6)
                .setResourceProduction(ResourceType.MANPOWER, 4)
                .setPopulation(3500000)
                .setInfrastructure(7)
                .build();
        
        // 한국 영토
        Region seoul = new Region.Builder("seoul", "서울", new Location(world, 4000, 64, 500))
                .setResourceProduction(ResourceType.WHEAT, 12)
                .setResourceProduction(ResourceType.MANPOWER, 5)
                .setResourceProduction(ResourceType.ELECTRONICS, 4)
                .setPopulation(3000000)
                .setInfrastructure(6)
                .build();
        
        Region busan = new Region.Builder("busan", "부산", new Location(world, 4100, 64, 700))
                .setResourceProduction(ResourceType.IRON, 8)
                .setResourceProduction(ResourceType.WHEAT, 8)
                .setResourceProduction(ResourceType.MANPOWER, 3)
                .setPopulation(1500000)
                .setInfrastructure(5)
                .build();
        
        // 지역 등록
        registerRegion(berlin);
        registerRegion(ruhr);
        registerRegion(moscow);
        registerRegion(baku);
        registerRegion(washington);
        registerRegion(texas);
        registerRegion(london);
        registerRegion(manchester);
        registerRegion(tokyo);
        registerRegion(osaka);
        registerRegion(seoul);
        registerRegion(busan);
        
        plugin.getLogger().info("Initialized " + regions.size() + " regions");
    }

    /**
     * 지역을 시스템에 등록
     */
    public void registerRegion(Region region) {
        regions.put(region.getId(), region);
    }

    /**
     * ID로 지역 가져오기
     */
    public Region getRegion(String regionId) {
        return regions.get(regionId);
    }

    /**
     * 모든 지역 목록
     */
    public Collection<Region> getAllRegions() {
        return regions.values();
    }

    /**
     * 특정 국가가 소유한 지역들
     */
    public List<Region> getRegionsByCountry(String countryId) {
        return regions.values().stream()
                .filter(region -> countryId.equals(region.getOwnerCountryId()))
                .collect(Collectors.toList());
    }

    /**
     * 특정 플레이어가 소유한 지역들
     */
    public List<Region> getRegionsByPlayer(UUID playerId) {
        return regions.values().stream()
                .filter(region -> playerId.equals(region.getOwnerId()))
                .collect(Collectors.toList());
    }

    /**
     * 소유자가 없는 중립 지역들
     */
    public List<Region> getNeutralRegions() {
        return regions.values().stream()
                .filter(region -> !region.hasOwner())
                .collect(Collectors.toList());
    }

    /**
     * 지역 소유권 설정
     */
    public void setRegionOwner(String regionId, UUID ownerId, String countryId) {
        Region region = getRegion(regionId);
        if (region != null) {
            region.setOwner(ownerId, countryId);
            plugin.getLogger().info("Region " + region.getName() + " claimed by " + countryId);
        }
    }

    /**
     * 지역 소유권 제거
     */
    public void removeRegionOwner(String regionId) {
        Region region = getRegion(regionId);
        if (region != null) {
            region.removeOwner();
            plugin.getLogger().info("Region " + region.getName() + " is now neutral");
        }
    }

    /**
     * 특정 위치에서 가장 가까운 지역 찾기
     */
    public Region getNearestRegion(Location location) {
        Region nearest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Region region : regions.values()) {
            double distance = region.getCenterLocation().distance(location);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = region;
            }
        }
        
        return nearest;
    }

    /**
     * 특정 반경 내의 지역들 찾기
     */
    public List<Region> getRegionsInRadius(Location center, double radius) {
        return regions.values().stream()
                .filter(region -> region.getCenterLocation().distance(center) <= radius)
                .collect(Collectors.toList());
    }

    /**
     * 지역의 인프라 업그레이드
     */
    public boolean upgradeInfrastructure(String regionId) {
        Region region = getRegion(regionId);
        if (region != null && region.getInfrastructure() < 10) {
            region.setInfrastructure(region.getInfrastructure() + 1);
            return true;
        }
        return false;
    }

    /**
     * 지역 정보 출력 (디버그용)
     */
    public String getRegionInfo(String regionId) {
        Region region = getRegion(regionId);
        if (region == null) return "Region not found";
        
        StringBuilder info = new StringBuilder();
        info.append("=== ").append(region.getName()).append(" ===\n");
        info.append("ID: ").append(region.getId()).append("\n");
        info.append("Owner: ").append(region.hasOwner() ? region.getOwnerCountryId() : "None").append("\n");
        info.append("Population: ").append(region.getPopulation()).append("\n");
        info.append("Infrastructure: ").append(region.getInfrastructure()).append("/10\n");
        info.append("Production:\n");
        
        region.getResourceProduction().forEach((type, amount) -> {
            int effective = region.getEffectiveProduction(type);
            info.append("  - ").append(type.getDisplayName())
                .append(": ").append(amount).append(" (").append(effective).append(")\n");
        });
        
        return info.toString();
    }
}