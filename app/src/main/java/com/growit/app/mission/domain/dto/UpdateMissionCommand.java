package com.growit.app.mission.domain.dto;

public record UpdateMissionCommand(String id, String userId, boolean finished) {}
