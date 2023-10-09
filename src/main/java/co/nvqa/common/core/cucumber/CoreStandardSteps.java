package co.nvqa.common.core.cucumber;

import co.nvqa.common.core.utils.CoreScenarioStorageKeys;
import co.nvqa.common.cucumber.StandardScenarioManager;
import co.nvqa.common.cucumber.glue.StandardSteps;

public abstract class CoreStandardSteps extends StandardSteps<StandardScenarioManager> implements
    CoreScenarioStorageKeys {

  @Override
  public void init() {
    // This method is empty by design.
  }
}
