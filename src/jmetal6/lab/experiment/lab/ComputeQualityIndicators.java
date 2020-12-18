package jmetal6.lab.experiment.lab;

import java.util.List;

import jmetal6.qualityindicator.QualityIndicator;

public class ComputeQualityIndicators {
  private final ExperimentSettings<?, ?> experimentSettings ;
  private final List<QualityIndicator<?, ?>> qualityIndicators ;

  public ComputeQualityIndicators(ExperimentSettings<?, ?> experimentSettings, List<QualityIndicator<?, ?>> qualityIndicators) {
    this.experimentSettings = experimentSettings;
    this.qualityIndicators = qualityIndicators;
  }

  public void run() {

  }
}
