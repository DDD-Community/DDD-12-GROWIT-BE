package com.growit.app.retrospect.domain.goalretrospect.dto;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.todo.domain.ToDo;
import java.util.List;

public record AnalysisDto(Goal goal, List<Retrospect> retrospects, List<ToDo> todos) {}
