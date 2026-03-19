package com.hoimc.managers;

import com.hoimc.HoiMCPlugin;
import com.hoimc.models.Country;
import com.hoimc.models.ResourceType;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * 플레이어/국가별 자원을 관리하는 Manager
 */
public class ResourceManager {
    private final HoiMCPlugin plugin;
    
    // 플레이어별 자원 보유량 (UUID -> ResourceType -> Amount)
    private final Map<UUID, Map<ResourceType, Integer>> playerResources;
    
    // 자원 저장 용량 (UUID -> ResourceType -> Capacity)
    private final Map<UUID, Map<ResourceType, Integer>> resourceCapacity;

    public ResourceManager(HoiMCPlugin plugin) {
        this.plugin = plugin;
        this.playerResources = new HashMap<>();
        this.resourceCapacity = new HashMap<>();
    }

    /**
     * 플레이어의 자원을 초기화 (국가 선택 시 호출)
     */
    public void initializePlayerResources(Player player, Country country) {
        UUID playerId = player.getUniqueId();
        
        // 자원 맵 초기화
        Map<ResourceType, Integer> resources = new HashMap<>();
        Map<ResourceType, Integer> capacity = new HashMap<>();
        
        // 국가 초기 자원 설정
        country.getInitialResources().forEach((resourceName, amount) -> {
            try {
                ResourceType type = ResourceType.valueOf(resourceName.toUpperCase());
                resources.put(type, amount);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Unknown resource type: " + resourceName);
            }
        });
        
        // 모든 자원 타입에 대해 기본값 설정
        for (ResourceType type : ResourceType.values()) {
            if (!resources.containsKey(type)) {
                resources.put(type, 0);
            }
            capacity.put(type, 10000); // 기본 용량
        }
        
        playerResources.put(playerId, resources);
        resourceCapacity.put(playerId, capacity);
        
        plugin.getLogger().info("Initialized resources for " + player.getName());
    }

    /**
     * 자원 추가
     */
    public boolean addResource(UUID playerId, ResourceType type, int amount) {
        if (amount <= 0) return false;
        
        Map<ResourceType, Integer> resources = playerResources.get(playerId);
        if (resources == null) return false;
        
        int current = resources.getOrDefault(type, 0);
        int capacity = getResourceCapacity(playerId, type);
        int newAmount = Math.min(current + amount, capacity);
        
        resources.put(type, newAmount);
        return true;
    }

    /**
     * 자원 제거
     */
    public boolean removeResource(UUID playerId, ResourceType type, int amount) {
        if (amount <= 0) return false;
        
        Map<ResourceType, Integer> resources = playerResources.get(playerId);
        if (resources == null) return false;
        
        int current = resources.getOrDefault(type, 0);
        if (current < amount) return false;
        
        resources.put(type, current - amount);
        return true;
    }

    /**
     * 자원 설정 (디버그용)
     */
    public void setResource(UUID playerId, ResourceType type, int amount) {
        Map<ResourceType, Integer> resources = playerResources.get(playerId);
        if (resources == null) {
            resources = new HashMap<>();
            playerResources.put(playerId, resources);
        }
        
        int capacity = getResourceCapacity(playerId, type);
        resources.put(type, Math.min(Math.max(0, amount), capacity));
    }

    /**
     * 자원 보유량 조회
     */
    public int getResource(UUID playerId, ResourceType type) {
        Map<ResourceType, Integer> resources = playerResources.get(playerId);
        if (resources == null) return 0;
        return resources.getOrDefault(type, 0);
    }

    /**
     * 모든 자원 조회
     */
    public Map<ResourceType, Integer> getAllResources(UUID playerId) {
        Map<ResourceType, Integer> resources = playerResources.get(playerId);
        if (resources == null) return new HashMap<>();
        return new HashMap<>(resources);
    }

    /**
     * 자원 보유 여부 확인
     */
    public boolean hasResource(UUID playerId, ResourceType type, int amount) {
        return getResource(playerId, type) >= amount;
    }

    /**
     * 여러 자원 보유 여부 확인
     */
    public boolean hasResources(UUID playerId, Map<ResourceType, Integer> required) {
        for (Map.Entry<ResourceType, Integer> entry : required.entrySet()) {
            if (!hasResource(playerId, entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 여러 자원 제거
     */
    public boolean removeResources(UUID playerId, Map<ResourceType, Integer> costs) {
        // 먼저 모든 자원을 가지고 있는지 확인
        if (!hasResources(playerId, costs)) {
            return false;
        }
        
        // 모든 자원 제거
        for (Map.Entry<ResourceType, Integer> entry : costs.entrySet()) {
            removeResource(playerId, entry.getKey(), entry.getValue());
        }
        
        return true;
    }

    /**
     * 자원 용량 조회
     */
    public int getResourceCapacity(UUID playerId, ResourceType type) {
        Map<ResourceType, Integer> capacity = resourceCapacity.get(playerId);
        if (capacity == null) return 10000;
        return capacity.getOrDefault(type, 10000);
    }

    /**
     * 자원 용량 설정
     */
    public void setResourceCapacity(UUID playerId, ResourceType type, int capacity) {
        Map<ResourceType, Integer> capacityMap = resourceCapacity.get(playerId);
        if (capacityMap == null) {
            capacityMap = new HashMap<>();
            resourceCapacity.put(playerId, capacityMap);
        }
        capacityMap.put(type, Math.max(0, capacity));
    }

    /**
     * 자원이 용량 제한에 도달했는지 확인
     */
    public boolean isResourceFull(UUID playerId, ResourceType type) {
        return getResource(playerId, type) >= getResourceCapacity(playerId, type);
    }

    /**
     * 플레이어 자원 데이터 제거
     */
    public void removePlayerData(UUID playerId) {
        playerResources.remove(playerId);
        resourceCapacity.remove(playerId);
    }

    /**
     * 모든 플레이어의 자원 데이터 조회 (관리자용)
     */
    public Map<UUID, Map<ResourceType, Integer>> getAllPlayerResources() {
        return new HashMap<>(playerResources);
    }
}