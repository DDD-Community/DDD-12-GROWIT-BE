package com.growit.app.goal.domain.goal.dto;

public record UpdatePlanCommand(String goalId, String planId, String userId, String content) {}
