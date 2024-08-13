package org.khtml.hexagonal.domain.building.repository;

import org.khtml.hexagonal.domain.building.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
