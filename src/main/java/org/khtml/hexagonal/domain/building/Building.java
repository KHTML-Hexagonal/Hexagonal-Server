package org.khtml.hexagonal.domain.building;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "building")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
public class Building {

    @Id
    private String gisBuildingId;

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

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "description")
    private String description;

}
