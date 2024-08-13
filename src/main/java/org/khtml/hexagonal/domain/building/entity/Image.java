package org.khtml.hexagonal.domain.building.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.khtml.hexagonal.domain.building.ImageType;
import org.khtml.hexagonal.domain.common.BaseEntity;
import org.khtml.hexagonal.domain.user.User;

@Table(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Image extends BaseEntity {

    private String url;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Image(String url, ImageType imageType, User user) {
        this.url = url;
        this.imageType = imageType;
        this.user = user;
    }

}
