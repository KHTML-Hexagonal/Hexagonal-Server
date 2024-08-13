package org.khtml.hexagonal.domain.building.controller;

import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.domain.ai.application.GptManager;
import org.khtml.hexagonal.domain.building.dto.*;
import org.khtml.hexagonal.domain.auth.JwtValidator;
import org.khtml.hexagonal.domain.building.application.BuildingService;
import org.khtml.hexagonal.domain.building.entity.Building;
import org.khtml.hexagonal.domain.user.User;
import org.khtml.hexagonal.global.support.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/v1/buildings")
@RequiredArgsConstructor
@RestController
public class BuildingController {

    private final BuildingService buildingService;
    private final JwtValidator jwtValidator;
    private final GptManager gptManager;

    @GetMapping("/{building-id}")
    public ApiResponse<BuildingDetailResponse> getBuildingDetail(
            @PathVariable(name = "building-id") String buildingId
    ) {
        List<String> images = buildingService.getBuildingImages(buildingId);
        Building building = buildingService.getBuilding(buildingId);
        return ApiResponse.success(BuildingDetailResponse.toResponse(images, building));
    }

    @PostMapping("/{building-id}/register")
    public ApiResponse<?> registerBuilding(
            @RequestHeader("Authorization") String token,
            @RequestParam("images") List<MultipartFile> multipartFiles,
            @PathVariable(name = "building-id") String buildingId
    ) throws IOException {
        User requestUser = jwtValidator.getUserFromToken(token);
        List<String> urls = buildingService.registerBuilding(buildingId, requestUser, multipartFiles);

        BuildingUpdate buildingUpdate = gptManager.analyzeBuilding(urls);
        buildingService.updateAnalyzedBuilding(buildingId, buildingUpdate);

        return ApiResponse.success(buildingUpdate);
    }

    @PostMapping("/{building-id}/register/description")
    public ApiResponse<?> registerBuilding(
            @RequestHeader("Authorization") String token,
            @PathVariable(name = "building-id") String buildingId,
            @RequestBody BuildingDescriptionRequest buildingDescriptionRequest
            ) throws IOException {
        User requestUser = jwtValidator.getUserFromToken(token);
        buildingService.updateBuildingDescription(buildingId, requestUser.getId(), buildingDescriptionRequest.description());

        return ApiResponse.success();
    }

    @GetMapping("/recommend")
    public ApiResponse<?> recommendBuilding() {
        return ApiResponse.success(buildingService.recommendBuilding());
    }

}
