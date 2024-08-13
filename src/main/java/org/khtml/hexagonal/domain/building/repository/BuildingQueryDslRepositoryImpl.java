package org.khtml.hexagonal.domain.building.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.domain.building.BuildingStatus;
import org.khtml.hexagonal.domain.building.dto.QRecommendBuilding;
import org.khtml.hexagonal.domain.building.dto.RecommendBuilding;

import java.beans.Expression;
import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.stringTemplate;
import static org.khtml.hexagonal.domain.building.entity.QBuilding.*;
import static org.khtml.hexagonal.domain.building.entity.QBuildingImage.*;
import static org.khtml.hexagonal.domain.building.entity.QImage.*;

@RequiredArgsConstructor
public class BuildingQueryDslRepositoryImpl implements BuildingQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RecommendBuilding> recommendBuilding() {
        return queryFactory
                .select(new QRecommendBuilding(
                        building.gisBuildingId,
                        image.url,
                        Expressions.stringTemplate("CONCAT({0}, ' ', {1})", building.legalDistrictName, building.landLotNumber),
                        building.repairList,
                        building.totalScore
                ))
                .from(buildingImage)
                .leftJoin(building).on(buildingImage.building.eq(building))
                .leftJoin(image).on(buildingImage.image.eq(image))
                .where(building.buildingStatus.eq(BuildingStatus.REGISTERED))
                .distinct()
                .orderBy(building.totalScore.desc())
                .limit(10)
                .fetch();
    }

}
