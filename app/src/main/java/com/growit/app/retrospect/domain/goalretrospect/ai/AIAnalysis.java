package com.growit.app.retrospect.domain.goalretrospect.ai;

import com.growit.app.retrospect.domain.goalretrospect.dto.AnalysisDto;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;

public interface AIAnalysis {
  Analysis generate(AnalysisDto request);
}
