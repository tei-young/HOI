package com.hoimc.tasks;

import com.hoimc.HoiMCPlugin;
import com.hoimc.models.Country;
import com.hoimc.models.Region;
import com.hoimc.models.ResourceType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 주기적으로 자원을 자동 생산하는 Tick 시스템
 * 20 ticks = 1초, 기본적으로 5초마다 실행
 */
public class ResourceProductionTask extends BukkitRunnable {
    private final HoiMCPlugin plugin;
    private final int tickInterval; // 틱 단위 (20 = 1초)

    public ResourceProductionTask(HoiMCPlugin plugin, int secondInterval) {
        this.plugin = plugin;
        this.tickInterval = secondInterval * 20; // 초를 틱으로 변환
    }

    @Override
    public void run() {
        // 모든 온라인 플레이어에 대해 자원 생산
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID playerId = player.getUniqueId();
            
            // 국가를 선택한 플레이어만 처리
            Country country = plugin.getCountryManager().getPlayerCountry(player);
            if (country == null) continue;

            // 해당 플레이어가 소유한 모든 지역에서 자원 생산
            List<Region> ownedRegions = plugin.getRegionManager().getRegionsByPlayer(playerId);
            
            for (Region region : ownedRegions) {
                // 전투 중인 지역은 생산 50% 감소
                double productionMultiplier = region.isContested() ? 0.5 : 1.0;
                
                // 국가 버프 적용
                double countryBonus = country.getBuffs().getOrDefault("production_speed", 1.0);
                
                // 각 자원 타입별로 생산
                for (Map.Entry<ResourceType, Integer> production : region.getResourceProduction().entrySet()) {
                    ResourceType type = production.getKey();
                    int baseProduction = region.getEffectiveProduction(type); // 인프라 보너스 포함
                    
                    // 최종 생산량 계산
                    int finalProduction = (int) (baseProduction * productionMultiplier * countryBonus);
                    
                    if (finalProduction > 0) {
                        // 자원 추가 (용량 제한 적용)
                        boolean added = plugin.getResourceManager().addResource(playerId, type, finalProduction);
                        
                        // 용량이 가득 찼을 때 디버그 메시지 (선택사항)
                        if (!added && plugin.getResourceManager().isResourceFull(playerId, type)) {
                            // 용량 초과 시 아무것도 하지 않음 (또는 로그 기록)
                        }
                    }
                }
            }
        }
    }

    /**
     * Task 시작
     */
    public void start() {
        // 첫 실행 딜레이: 20 ticks (1초), 반복 주기: tickInterval
        this.runTaskTimer(plugin, 20L, tickInterval);
        plugin.getLogger().info("Resource production task started (interval: " + (tickInterval / 20) + " seconds)");
    }

    /**
     * Task 중지
     */
    public void stop() {
        this.cancel();
        plugin.getLogger().info("Resource production task stopped");
    }
}