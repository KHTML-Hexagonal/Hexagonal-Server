package org.khtml.hexagonal.domain.building.repository;

import org.khtml.hexagonal.domain.building.entity.Building;
import org.khtml.hexagonal.domain.building.entity.BuildingImage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuildingImageRepository extends JpaRepository<BuildingImage, Long>, BuildingQueryDslRepository {

    @EntityGraph(attributePaths = {"image", "building"})
    List<BuildingImage> findAllByBuilding(Building building);

}
