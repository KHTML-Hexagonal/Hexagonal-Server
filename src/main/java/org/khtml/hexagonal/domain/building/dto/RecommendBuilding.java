package org.khtml.hexagonal.domain.building.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

@Data
public class RecommendBuilding {

    private String buildingId;
    private String imageUrl;
    private String address;
    private String repairList;

    @Builder
    @QueryProjection
    public RecommendBuilding(String buildingId, String imageUrl, String address, String repairList) {
        this.buildingId = buildingId;
        this.imageUrl = imageUrl;
        this.address = address;
        this.repairList = repairList;
    }
}
