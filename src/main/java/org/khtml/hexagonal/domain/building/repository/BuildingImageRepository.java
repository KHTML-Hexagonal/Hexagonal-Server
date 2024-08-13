package org.khtml.hexagonal.domain.building.repository;

import org.khtml.hexagonal.domain.building.entity.Building;
import org.khtml.hexagonal.domain.building.entity.BuildingImage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuildingImageRepository extends JpaRepository<BuildingImage, Long> {

    @EntityGraph(attributePaths = {"image", "building"})
    List<BuildingImage> findAllByBuilding(Building building);

    @Query("SELECT bi FROM BuildingImage bi " +
            "JOIN Building b ON bi.building.gisBuildingId = b.gisBuildingId " +
            "JOIN Image i ON bi.image.id = i.id " +
            "WHERE b.buildingStatus = 'REGISTERED' ORDER BY b.totalScore DESC LIMIT 10")
    List<BuildingImage> recommendBuilding();

}
