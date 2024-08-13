package org.khtml.hexagonal.domain.building.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImageRequest {

    private String role;
    private List<Content> content;

    @Getter
    @Setter
    public static class ImageUrl {
        private String url;
    }

    @Getter
    @Setter
    public static class Content {
        private String type;
        private ImageUrl image_url;

        public String getImageUrlOrNull() {
            return (image_url != null) ? image_url.getUrl() : null;
        }
    }
}
