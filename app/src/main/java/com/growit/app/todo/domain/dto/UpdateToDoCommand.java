package com.growit.app.todo.domain.dto;

import java.time.LocalDate;

public record UpdateToDoCommand(String id, String userId, String content, LocalDate date) {}
