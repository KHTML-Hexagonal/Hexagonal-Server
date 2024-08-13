package org.khtml.hexagonal.domain.ai.dto;

public class BuildingUpdate {

    private String structureReason;
    private String roofMaterial;
    private String roofCondition;
    private String wallMaterial;
    private String wallCondition;
    private String windowDoorMaterial;
    private String windowDoorCondition;
    private String overallCondition;
    private String conditionReason;
    private Integer crackScore;
    private Integer leakScore;
    private Integer corrosionScore;
    private Integer agingScore;
    private Integer totalScore;
    private String repairList;

    // Getters and Setters

    public String getStructureReason() {
        return structureReason;
    }

    public void setStructureReason(String structureReason) {
        this.structureReason = structureReason;
    }

    public String getRoofMaterial() {
        return roofMaterial;
    }

    public void setRoofMaterial(String roofMaterial) {
        this.roofMaterial = roofMaterial;
    }

    public String getRoofCondition() {
        return roofCondition;
    }

    public void setRoofCondition(String roofCondition) {
        this.roofCondition = roofCondition;
    }

    public String getWallMaterial() {
        return wallMaterial;
    }

    public void setWallMaterial(String wallMaterial) {
        this.wallMaterial = wallMaterial;
    }

    public String getWallCondition() {
        return wallCondition;
    }

    public void setWallCondition(String wallCondition) {
        this.wallCondition = wallCondition;
    }

    public String getWindowDoorMaterial() {
        return windowDoorMaterial;
    }

    public void setWindowDoorMaterial(String windowDoorMaterial) {
        this.windowDoorMaterial = windowDoorMaterial;
    }

    public String getWindowDoorCondition() {
        return windowDoorCondition;
    }

    public void setWindowDoorCondition(String windowDoorCondition) {
        this.windowDoorCondition = windowDoorCondition;
    }

    @Override
    public String toString() {
        return "BuildingUpdate{" +
                "structureReason='" + structureReason + '\'' +
                ", roofMaterial='" + roofMaterial + '\'' +
                ", roofCondition='" + roofCondition + '\'' +
                ", wallMaterial='" + wallMaterial + '\'' +
                ", wallCondition='" + wallCondition + '\'' +
                ", windowDoorMaterial='" + windowDoorMaterial + '\'' +
                ", windowDoorCondition='" + windowDoorCondition + '\'' +
                ", overallCondition='" + overallCondition + '\'' +
                ", conditionReason='" + conditionReason + '\'' +
                ", crackScore=" + crackScore +
                ", leakScore=" + leakScore +
                ", corrosionScore=" + corrosionScore +
                ", agingScore=" + agingScore +
                ", totalScore=" + totalScore +
                ", repairList='" + repairList + '\'' +
                '}';
    }

    public String getOverallCondition() {
        return overallCondition;
    }

    public void setOverallCondition(String overallCondition) {
        this.overallCondition = overallCondition;
    }

    public String getConditionReason() {
        return conditionReason;
    }

    public void setConditionReason(String conditionReason) {
        this.conditionReason = conditionReason;
    }

    public Integer getCrackScore() {
        return crackScore;
    }

    public void setCrackScore(Integer crackScore) {
        this.crackScore = crackScore;
    }

    public Integer getLeakScore() {
        return leakScore;
    }

    public void setLeakScore(Integer leakScore) {
        this.leakScore = leakScore;
    }

    public Integer getCorrosionScore() {
        return corrosionScore;
    }

    public void setCorrosionScore(Integer corrosionScore) {
        this.corrosionScore = corrosionScore;
    }

    public Integer getAgingScore() {
        return agingScore;
    }

    public void setAgingScore(Integer agingScore) {
        this.agingScore = agingScore;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public String getRepairList() {
        return repairList;
    }

    public void setRepairList(String repairList) {
        this.repairList = repairList;
    }
}

