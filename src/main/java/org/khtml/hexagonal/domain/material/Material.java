package org.khtml.hexagonal.domain.material;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.khtml.hexagonal.domain.building.entity.Building;
import org.khtml.hexagonal.domain.common.BaseEntity;

@Table(name = "material")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Material extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @Builder
    public Material(String name, Building building) {
        this.name = name;
        this.building = building;
    }

}
