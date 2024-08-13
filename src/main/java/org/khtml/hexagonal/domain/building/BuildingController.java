package org.khtml.hexagonal.domain.building;

import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.domain.building.application.BuildingService;
import org.khtml.hexagonal.global.support.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/buildings")
@RequiredArgsConstructor
@RestController
public class BuildingController {

    private final BuildingService buildingService;

    @GetMapping("/{building-id}")
    public ApiResponse<BuildingDetailResponse> getBuildingDetail(
            @PathVariable(name = "building-id") String buildingId
    ) {
        return ApiResponse.success(BuildingDetailResponse.toResponse(buildingService.getBuilding(buildingId)));
    }

}
