package com.growit.app.retrospect.domain.goalretrospect.service;

import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.retrospect.infrastructure.engine.dto.AnalysisDto;

public interface AIAnalysis {
  Analysis generate(AnalysisDto request);
}
