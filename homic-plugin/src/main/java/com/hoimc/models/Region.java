package com.hoimc.models;

import org.bukkit.Location;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 지역 정보를 담는 데이터 모델
 * 각 지역은 자원 생산, 병력 배치, 건물 건설 등의 기능을 가짐
 */
public class Region {
    private final String id;
    private final String name;
    private final Location centerLocation;
    
    // 소유 정보
    private UUID ownerId;
    private String ownerCountryId;
    
    // 자원 생산
    private final Map<ResourceType, Integer> resourceProduction; // 틱당 생산량
    private final Map<ResourceType, Integer> resourceCapacity;   // 최대 저장량
    
    // 지역 속성
    private int population;
    private int infrastructure;
    private boolean contested; // 전투 중인지
    
    public Region(String id, String name, Location centerLocation) {
        this.id = id;
        this.name = name;
        this.centerLocation = centerLocation;
        this.resourceProduction = new HashMap<>();
        this.resourceCapacity = new HashMap<>();
        this.population = 0;
        this.infrastructure = 1;
        this.contested = false;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getCenterLocation() {
        return centerLocation;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getOwnerCountryId() {
        return ownerCountryId;
    }

    public Map<ResourceType, Integer> getResourceProduction() {
        return new HashMap<>(resourceProduction);
    }

    public int getResourceProduction(ResourceType type) {
        return resourceProduction.getOrDefault(type, 0);
    }

    public Map<ResourceType, Integer> getResourceCapacity() {
        return new HashMap<>(resourceCapacity);
    }

    public int getResourceCapacity(ResourceType type) {
        return resourceCapacity.getOrDefault(type, 1000);
    }

    public int getPopulation() {
        return population;
    }

    public int getInfrastructure() {
        return infrastructure;
    }

    public boolean isContested() {
        return contested;
    }

    public boolean hasOwner() {
        return ownerId != null;
    }

    // Setters
    public void setOwner(UUID ownerId, String ownerCountryId) {
        this.ownerId = ownerId;
        this.ownerCountryId = ownerCountryId;
    }

    public void removeOwner() {
        this.ownerId = null;
        this.ownerCountryId = null;
    }

    public void setResourceProduction(ResourceType type, int amount) {
        this.resourceProduction.put(type, amount);
    }

    public void setResourceCapacity(ResourceType type, int capacity) {
        this.resourceCapacity.put(type, capacity);
    }

    public void setPopulation(int population) {
        this.population = Math.max(0, population);
    }

    public void setInfrastructure(int infrastructure) {
        this.infrastructure = Math.max(1, Math.min(10, infrastructure));
    }

    public void setContested(boolean contested) {
        this.contested = contested;
    }

    // 인프라에 따른 생산량 보너스 계산
    public int getEffectiveProduction(ResourceType type) {
        int baseProduction = getResourceProduction(type);
        double infrastructureBonus = 1.0 + (infrastructure - 1) * 0.1; // 인프라 레벨당 +10%
        return (int) (baseProduction * infrastructureBonus);
    }

    // Builder 패턴
    public static class Builder {
        private final Region region;

        public Builder(String id, String name, Location centerLocation) {
            this.region = new Region(id, name, centerLocation);
        }

        public Builder setResourceProduction(ResourceType type, int amount) {
            region.setResourceProduction(type, amount);
            return this;
        }

        public Builder setResourceCapacity(ResourceType type, int capacity) {
            region.setResourceCapacity(type, capacity);
            return this;
        }

        public Builder setPopulation(int population) {
            region.setPopulation(population);
            return this;
        }

        public Builder setInfrastructure(int infrastructure) {
            region.setInfrastructure(infrastructure);
            return this;
        }

        public Builder setOwner(UUID ownerId, String ownerCountryId) {
            region.setOwner(ownerId, ownerCountryId);
            return this;
        }

        public Region build() {
            return region;
        }
    }
}