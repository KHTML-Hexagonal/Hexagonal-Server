package org.khtml.hexagonal.domain.building.dto;

import org.khtml.hexagonal.domain.building.entity.Building;

import java.util.List;

public record RecommendBuildingResult(
        List<RecommendBuilding> recommendBuildings
) {
    public record RecommendBuilding(
            String imageUrl,
            String address,
            String repairList
    ) {
    }
}
