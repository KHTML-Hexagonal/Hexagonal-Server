package org.khtml.hexagonal.domain.building.dto;

public class MaterialInfo {
    private String material;
    private String usage;

    public MaterialInfo(String material, String usage) {
        this.material = material;
        this.usage = usage;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "MaterialInfo{" +
                "material='" + material + '\'' +
                ", usage='" + usage + '\'' +
                '}';
    }
}
