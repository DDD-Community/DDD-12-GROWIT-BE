package com.growit.app.retrospect.domain.goalretrospect.service;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.todo.domain.ToDo;
import java.util.List;

public interface AIAnalysis {
  Analysis generate(Goal goal, List<ToDo> todos);
}
