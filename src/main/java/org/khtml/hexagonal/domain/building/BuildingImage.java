package org.khtml.hexagonal.domain.building;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.khtml.hexagonal.domain.common.BaseEntity;

@Table(name = "building_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BuildingImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

}
