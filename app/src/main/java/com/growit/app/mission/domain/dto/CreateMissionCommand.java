package com.growit.app.mission.domain.dto;

public record CreateMissionCommand(String userId, boolean finished, String content) {}
