package org.khtml.hexagonal.domain.building;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "building")
@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class Building {

    @Id
    private String gisBuildingId;

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

    @Column(name = "material_list")
    private String materialList;

    @Column(name = "condition_list")
    private String conditionList;
}
