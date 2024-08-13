package org.khtml.hexagonal.domain.material;

import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.domain.auth.JwtValidator;
import org.khtml.hexagonal.domain.user.User;
import org.khtml.hexagonal.global.support.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
@RestController
public class MaterialController {

    private final MaterialService materialService;
    private final JwtValidator jwtValidator;

    @PostMapping("/{building-id}/register")
    public ApiResponse<?> registerBuilding(
            @RequestHeader("Authorization") String token,
            @RequestParam("images") List<MultipartFile> multipartFiles,
            @PathVariable(name = "building-id") String buildingId
    ) throws IOException {
        User requestUser = jwtValidator.getUserFromToken(token);
        materialService.registerMaterials(buildingId, requestUser, multipartFiles);
        return ApiResponse.success();
    }

}
