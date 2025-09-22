package com.growit.app.retrospect.domain.retrospect.dto;

import com.growit.app.retrospect.domain.retrospect.vo.KPT;

public record CreateRetrospectCommand(String goalId, String planId, String userId, KPT kpt) {}
