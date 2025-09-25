package com.growit.app.retrospect.domain.retrospect.dto;

import com.growit.app.retrospect.domain.retrospect.vo.KPT;

public record UpdateRetrospectCommand(String id, String userId, KPT kpt) {}
