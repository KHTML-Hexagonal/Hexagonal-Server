package org.khtml.hexagonal.domain.building.application;

import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.domain.ai.dto.BuildingUpdate;
import org.khtml.hexagonal.domain.building.BlobManager;
import org.khtml.hexagonal.domain.building.Building;
import org.khtml.hexagonal.domain.building.BuildingDetailResponse;
import org.khtml.hexagonal.domain.building.BuildingRepository;
import org.khtml.hexagonal.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final BlobManager blobManager;

    public Building createBuilding(Building building) {
        return buildingRepository.save(building);
    }

    public Building getBuilding(String buildingId) {
        return buildingRepository.findBuildingByGisBuildingId(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));
    }

    public void deleteBuilding(Long id) {
        buildingRepository.deleteById(id);
    }

    public Building updateBuilding(String buildingId, BuildingUpdate buildingUpdate) {
        Building existingBuilding = getBuilding(buildingId);
        existingBuilding.setStructureReason(buildingUpdate.getStructureReason());
        existingBuilding.setRoofMaterial(buildingUpdate.getRoofMaterial());
        existingBuilding.setRoofCondition(buildingUpdate.getRoofCondition());
        existingBuilding.setWallMaterial(buildingUpdate.getWallMaterial());
        existingBuilding.setWallCondition(buildingUpdate.getWallCondition());
        existingBuilding.setWindowDoorMaterial(buildingUpdate.getWindowDoorMaterial());
        existingBuilding.setWindowDoorCondition(buildingUpdate.getWindowDoorCondition());
        existingBuilding.setOverallCondition(buildingUpdate.getOverallCondition());
        existingBuilding.setConditionReason(buildingUpdate.getConditionReason());
        existingBuilding.setCrackScore(buildingUpdate.getCrackScore());
        existingBuilding.setLeakScore(buildingUpdate.getLeakScore());
        existingBuilding.setCorrosionScore(buildingUpdate.getCorrosionScore());
        existingBuilding.setAgingScore(buildingUpdate.getAgingScore());
        existingBuilding.setTotalScore(buildingUpdate.getTotalScore());
        existingBuilding.setRepairList(buildingUpdate.getRepairList());

        return buildingRepository.save(existingBuilding);
    }

    public void registerBuilding(User requestUser, List<MultipartFile> multipartFiles) throws IOException {
        MultipartFile file = multipartFiles.getFirst();
        String url = blobManager.storeFile(file.getOriginalFilename(), file.getInputStream(), file.getSize());

    }

}
