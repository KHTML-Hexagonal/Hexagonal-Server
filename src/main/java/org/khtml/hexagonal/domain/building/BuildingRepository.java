package org.khtml.hexagonal.domain.building;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {

    @EntityGraph(attributePaths = {"user"})
    Optional<Building> findBuildingByGisBuildingId(String gisBuildingId);

}
