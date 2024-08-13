package org.khtml.hexagonal.domain.building.repository;

import org.khtml.hexagonal.domain.building.entity.Building;
import org.khtml.hexagonal.domain.building.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByBuilding(Building building);
}
