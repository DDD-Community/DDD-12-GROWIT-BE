package com.growit.user.domain.user.dto;

import com.growit.user.domain.jobrole.JobRole;
import com.growit.user.domain.user.User;

public record UserDto(User user, JobRole jobRole) {}
