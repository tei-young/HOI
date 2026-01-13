package com.hoimc.models;

import org.bukkit.Material;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 국가 정보를 담는 데이터 모델
 */
public class Country {
    private final String id;
    private final String name;
    private final GovernmentType governmentType;
    private final Material flagMaterial;
    private final Map<String, Integer> initialResources;
    private final Map<String, Double> buffs;
    
    // 플레이어 정보
    private UUID ownerId;
    private String ownerName;

    public Country(String id, String name, GovernmentType governmentType, Material flagMaterial) {
        this.id = id;
        this.name = name;
        this.governmentType = governmentType;
        this.flagMaterial = flagMaterial;
        this.initialResources = new HashMap<>();
        this.buffs = new HashMap<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GovernmentType getGovernmentType() {
        return governmentType;
    }

    public Material getFlagMaterial() {
        return flagMaterial;
    }

    public Map<String, Integer> getInitialResources() {
        return new HashMap<>(initialResources);
    }

    public Map<String, Double> getBuffs() {
        return new HashMap<>(buffs);
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    // Setters
    public void setOwner(UUID ownerId, String ownerName) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    public void addInitialResource(String resourceType, int amount) {
        this.initialResources.put(resourceType, amount);
    }

    public void addBuff(String buffType, double value) {
        this.buffs.put(buffType, value);
    }

    // 빌더 패턴
    public static class Builder {
        private final Country country;

        public Builder(String id, String name, GovernmentType governmentType, Material flagMaterial) {
            this.country = new Country(id, name, governmentType, flagMaterial);
        }

        public Builder addResource(String type, int amount) {
            country.addInitialResource(type, amount);
            return this;
        }

        public Builder addBuff(String type, double value) {
            country.addBuff(type, value);
            return this;
        }

        public Country build() {
            return country;
        }
    }
}
