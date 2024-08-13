package org.khtml.hexagonal.domain.material;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    Boolean existsByName(String name);

}
