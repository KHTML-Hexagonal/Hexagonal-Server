package org.khtml.hexagonal.domain.building;

public record BuildingDetailResponse(
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
    public static BuildingDetailResponse toResponse(Building building) {
        return new BuildingDetailResponse(
                building.getGisBuildingId(),
                building.getLegalDistrictName() + " " + building.getLandLotNumber(),
                building.getDescription(),
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
