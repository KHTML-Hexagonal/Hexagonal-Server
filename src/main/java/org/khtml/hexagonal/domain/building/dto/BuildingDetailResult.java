package org.khtml.hexagonal.domain.building.dto;

import org.khtml.hexagonal.domain.building.entity.Building;
import org.khtml.hexagonal.domain.building.entity.Image;

import java.util.List;

public record BuildingDetailResult(
        List<Image> images,
        Building building
) {
}
