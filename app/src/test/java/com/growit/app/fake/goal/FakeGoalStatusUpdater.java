package com.growit.app.fake.goal;

import com.growit.app.goal.domain.goal.service.GoalStatusUpdater;

public class FakeGoalStatusUpdater implements GoalStatusUpdater {
  public boolean called = false;
  public String lastUid;

  @Override
  public void refreshByToDo(String uid) {
    this.called = true;
    this.lastUid = uid;
  }
}
