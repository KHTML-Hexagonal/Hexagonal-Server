package org.khtml.hexagonal.domain.building.application;

import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.domain.building.ImageType;
import org.khtml.hexagonal.domain.building.dto.BuildingUpdate;
import org.khtml.hexagonal.domain.building.dto.RecommendBuilding;
import org.khtml.hexagonal.domain.building.entity.Building;
import org.khtml.hexagonal.domain.building.entity.BuildingImage;
import org.khtml.hexagonal.domain.building.entity.Image;
import org.khtml.hexagonal.domain.building.repository.BuildingImageRepository;
import org.khtml.hexagonal.domain.building.repository.BuildingRepository;
import org.khtml.hexagonal.domain.building.repository.ImageRepository;
import org.khtml.hexagonal.domain.user.User;
import org.khtml.hexagonal.domain.user.UserRepository;
import org.khtml.hexagonal.global.support.error.ErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final BuildingImageRepository buildingImageRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final BlobManager blobManager;


    @Transactional
    public Building createBuilding(Building building) {
        return buildingRepository.save(building);
    }

    public Building getBuilding(String buildingId) {
        return buildingRepository.findBuildingByGisBuildingId(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));
    }

    public List<String> getBuildingImages(String buildingId) {
        Building building = buildingRepository.findBuildingByGisBuildingId(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));

        List<BuildingImage> buildingImages = buildingImageRepository.findAllByBuilding(building);
        List<String> images = new ArrayList<>();
        for (BuildingImage buildingImage : buildingImages) {
            images.add(buildingImage.getImage().getUrl());
        }

        return images;
    }

    public void deleteBuilding(Long id) {
        buildingRepository.deleteById(id);
    }

    @Transactional
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

    @Transactional
    public List<String> registerBuilding(String buildingId, User requestUser, List<MultipartFile> multipartFiles) throws IOException {
        List<String> ret = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            String url = blobManager.storeFile(file.getOriginalFilename(), file.getInputStream(), file.getSize());

            User user = userRepository.findById(requestUser.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
            Building building = buildingRepository.findBuildingByGisBuildingId(buildingId)
                    .orElseThrow(() -> new IllegalArgumentException("Building not found"));

            building.updateUser(user);

            Image image = Image.builder()
                    .url(url)
                    .imageType(ImageType.BUILDING)
                    .user(user)
                    .build();

            BuildingImage buildingImage = BuildingImage.builder()
                    .building(building)
                    .image(image)
                    .build();

            imageRepository.save(image);
            buildingImageRepository.save(buildingImage);

            ret.add(url);
        }

        return ret;
    }

    @Transactional
    public void updateAnalyzedBuilding(String buildingId, BuildingUpdate buildingUpdate) {
        Building building = buildingRepository.findBuildingByGisBuildingId(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));

        building.updateAnalyzedData(buildingUpdate);
    }

    @Transactional
    public void updateBuildingDescription(String buildingId, Long userId, String description) {
        Building building = buildingRepository.findBuildingByGisBuildingId(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));

        if (!Objects.equals(building.getUser().getId(), userId)) {
            throw new IllegalArgumentException(ErrorType.DEFAULT_ERROR.getMessage());
        }

        building.setBuildingDescription(description);
    }

    public List<RecommendBuilding> recommendBuilding() {
        return buildingImageRepository.recommendBuilding();
    }

}
