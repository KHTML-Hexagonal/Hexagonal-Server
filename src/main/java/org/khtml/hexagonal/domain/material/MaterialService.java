package org.khtml.hexagonal.domain.material;

import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.domain.ai.application.GptManager;
import org.khtml.hexagonal.domain.building.*;
import org.khtml.hexagonal.domain.building.application.BlobManager;
import org.khtml.hexagonal.domain.building.dto.MaterialResult;
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
public class MaterialService {

    private final BuildingRepository buildingRepository;
    private final BuildingImageRepository buildingImageRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final MaterialRepository materialRepository;
    private final GptManager gptManager;
    private final BlobManager blobManager;

    @Transactional
    public void registerMaterials(String buildingId, User requestUser, List<MultipartFile> multipartFiles) throws IOException {
        List<String> urls = new ArrayList<>();
        Building building = buildingRepository.findBuildingByGisBuildingId(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));
        User user = userRepository.findById(requestUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        for (MultipartFile file : multipartFiles) {
            String url = blobManager.storeFile(file.getOriginalFilename(), file.getInputStream(), file.getSize());

            Image image = Image.builder().url(url).imageType(ImageType.MATERIAL).user(user).build();
            BuildingImage buildingImage = BuildingImage.builder().image(image).building(building).build();

            urls.add(url);

            imageRepository.save(image);
            buildingImageRepository.save(buildingImage);
        }

        MaterialResult materialResult = gptManager.analyzeMaterial(urls);
        String[] materials = materialResult.material().split(",");

        for (String material : materials) {
            if (materialRepository.existsByName(material)) {
                continue;
            }
            Material materialEntity = Material.builder().name(material.trim()).build();
            materialRepository.save(materialEntity);
        }

        building.updateMaterialUsage(materialResult.usage());
    }

    @Transactional
    public void updateMaterialDescription(String buildingId, Long userId, String description) {
        Building building = buildingRepository.findBuildingByGisBuildingId(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));

        if(!Objects.equals(building.getUser().getId(), userId)) {
            throw new IllegalArgumentException(ErrorType.DEFAULT_ERROR.getMessage());
        }

        building.setMaterialDescription(description);
    }

}
