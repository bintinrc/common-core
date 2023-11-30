package co.nvqa.common.core.model;

public class InboundSettings {

  private Double maxWeight;
  private Double weightTolerance;

  public InboundSettings() {
  }

  public Double getMaxWeight() {
    return maxWeight;
  }

  public void setMaxWeight(Double maxWeight) {
    this.maxWeight = maxWeight;
  }

  public Double getWeightTolerance() {
    return weightTolerance;
  }

  public void setWeightTolerance(Double weightTolerance) {
    this.weightTolerance = weightTolerance;
  }
}
