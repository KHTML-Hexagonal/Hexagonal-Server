package org.khtml.hexagonal.domain.building.repository;

import org.khtml.hexagonal.domain.building.entity.BuildingImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingImageRepository extends JpaRepository<BuildingImage, Long> {
}
