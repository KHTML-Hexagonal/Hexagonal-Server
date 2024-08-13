package org.khtml.hexagonal.domain.building.repository;

import org.khtml.hexagonal.domain.building.dto.RecommendBuilding;

import java.util.List;


public interface BuildingQueryDslRepository {

    List<RecommendBuilding> recommendBuilding();
}
