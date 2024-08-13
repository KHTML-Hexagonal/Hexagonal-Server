package org.khtml.hexagonal.domain.building.dto;

import org.khtml.hexagonal.domain.building.entity.Building;

import java.util.List;

public record BuildingDetailResponse(
        List<String> images,
        String buildingId,
        String address,
        String description,
        String phoneNumber,
        Integer crackScore,
        Integer leakScore,
        Integer corrosionScore,
        Integer agingScore,
        Integer totalScore,
        String repairList,
        String roofMaterial,
        String buildingStructureType,
        String wallMaterial,
        String windowDoorMaterial
) {
    public static BuildingDetailResponse toResponse(List<String> images, Building building) {
        return new BuildingDetailResponse(
                images,
                building.getGisBuildingId(),
                building.getLegalDistrictName() + " " + building.getLandLotNumber(),
                building.getBuildingDescription(),
                building.getUser() == null ? null : building.getUser().getPhoneNumber(),
                building.getCrackScore(),
                building.getLeakScore(),
                building.getCorrosionScore(),
                building.getAgingScore(),
                building.getTotalScore(),
                building.getRepairList(),
                building.getRoofMaterial(),
                building.getBuildingStructureType(),
                building.getWallMaterial(),
                building.getWindowDoorMaterial()
        );
    }
}
