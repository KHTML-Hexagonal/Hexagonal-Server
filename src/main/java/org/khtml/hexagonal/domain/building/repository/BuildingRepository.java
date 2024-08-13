package org.khtml.hexagonal.domain.building.repository;

import org.khtml.hexagonal.domain.building.BuildingStatus;
import org.khtml.hexagonal.domain.building.entity.Building;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {

    @EntityGraph(attributePaths = {"user"})
    Optional<Building> findBuildingByGisBuildingId(String gisBuildingId);

    List<Building> findAllByBuildingStatus(BuildingStatus buildingStatus);

}
