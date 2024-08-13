package org.khtml.hexagonal.domain.building.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.khtml.hexagonal.domain.building.BuildingStatus;
import org.khtml.hexagonal.domain.building.dto.BuildingUpdate;
import org.khtml.hexagonal.domain.user.User;

@Table(name = "building")
@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class Building {

    @Id
    private String gisBuildingId;

    @Enumerated(EnumType.STRING)
    @Column(name = "building_status")
    private BuildingStatus buildingStatus = BuildingStatus.NOT_REGISTERED;

    @Column(name = "legal_district_name")
    private String legalDistrictName;

    @Column(name = "land_lot_number")
    private String landLotNumber;

    @Column(name = "building_total_area")
    private double buildingTotalArea;

    @Column(name = "building_structure_type")
    private String buildingStructureType;

    @Column(name = "primary_use")
    private String primaryUse;

    @Column(name = "building_height")
    private double buildingHeight;

    @Column(name = "above_ground_floors")
    private Integer aboveGroundFloors;

    @Column(name = "below_ground_floors")
    private Integer belowGroundFloors;

    @Column(name = "permit_date")
    private String permitDate;

    @Column(name = "approval_date")
    private String approvalDate;

    @Column(name = "building_age")
    private Integer buildingAge;

    @Column(name = "data_reference_date")
    private String dataReferenceDate;

    @Column(name = "structure_reason")
    private String structureReason;

    @Column(name = "roof_material")
    private String roofMaterial;

    @Column(name = "roof_condition")
    private String roofCondition;

    @Column(name = "wall_material")
    private String wallMaterial;

    @Column(name = "wall_condition")
    private String wallCondition;

    @Column(name = "window_door_material")
    private String windowDoorMaterial;

    @Column(name = "window_door_condition")
    private String windowDoorCondition;

    @Column(name = "overall_condition")
    private String overallCondition;

    @Column(name = "condition_reason")
    private String conditionReason;

    @Column(name = "crack_score")
    private Integer crackScore;

    @Column(name = "leak_score")
    private Integer leakScore;

    @Column(name = "corrosion_score")
    private Integer corrosionScore;

    @Column(name = "aging_score")
    private Integer agingScore;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "repair_list")
    private String repairList;

    @Column(name = "building_description")
    private String buildingDescription;

    @Column(name = "material_description")
    private String materialDescription;

    @Column(name = "material_usage")
    private String materialUsage;

    @Column(name = "is_analyzed")
    private Boolean isAnalyzed = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateMaterialUsage(String materialUsage) {
        this.materialUsage = materialUsage;
    }

    public void updateAnalyzedData(BuildingUpdate buildingUpdate) {
        this.structureReason = buildingUpdate.getStructureReason();
        this.roofMaterial = buildingUpdate.getRoofMaterial();
        this.roofCondition = buildingUpdate.getRoofCondition();
        this.wallMaterial = buildingUpdate.getWallMaterial();
        this.wallCondition = buildingUpdate.getWallCondition();
        this.windowDoorMaterial = buildingUpdate.getWindowDoorMaterial();
        this.windowDoorCondition = buildingUpdate.getWindowDoorCondition();
        this.overallCondition = buildingUpdate.getOverallCondition();
        this.conditionReason = buildingUpdate.getConditionReason();
        this.crackScore = buildingUpdate.getCrackScore();
        this.leakScore = buildingUpdate.getLeakScore();
        this.corrosionScore = buildingUpdate.getCorrosionScore();
        this.agingScore = buildingUpdate.getAgingScore();
        this.totalScore = buildingUpdate.getTotalScore();
        this.repairList = buildingUpdate.getRepairList();
        this.isAnalyzed = true;
        this.buildingStatus = BuildingStatus.REGISTERED;
    }

}
