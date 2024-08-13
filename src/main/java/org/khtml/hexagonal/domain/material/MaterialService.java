package org.khtml.hexagonal.domain.material;

import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.domain.building.*;
import org.khtml.hexagonal.domain.building.application.BlobManager;
import org.khtml.hexagonal.domain.building.entity.Building;
import org.khtml.hexagonal.domain.building.entity.BuildingImage;
import org.khtml.hexagonal.domain.building.entity.Image;
import org.khtml.hexagonal.domain.building.repository.BuildingImageRepository;
import org.khtml.hexagonal.domain.building.repository.BuildingRepository;
import org.khtml.hexagonal.domain.building.repository.ImageRepository;
import org.khtml.hexagonal.domain.user.User;
import org.khtml.hexagonal.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MaterialService {

    private final BuildingRepository buildingRepository;
    private final BuildingImageRepository buildingImageRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final MaterialRepository materialRepository;
    private final BlobManager blobManager;

    @Transactional
    public void registerMaterials(String buildingId, User requestUser, List<MultipartFile> multipartFiles) throws IOException {
        for(MultipartFile file : multipartFiles) {
            String url = blobManager.storeFile(file.getOriginalFilename(), file.getInputStream(), file.getSize());
            Building building = buildingRepository.findBuildingByGisBuildingId(buildingId)
                    .orElseThrow(() -> new IllegalArgumentException("Building not found"));
            User user = userRepository.findById(requestUser.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Image image = Image.builder().url(url).imageType(ImageType.MATERIAL).user(user).build();
            BuildingImage buildingImage = BuildingImage.builder().image(image).building(building).build();

            // **Material GPT 로직 수행 후 저장 로직 필요 **//
            imageRepository.save(image);
            buildingImageRepository.save(buildingImage);
        }
    }

}
