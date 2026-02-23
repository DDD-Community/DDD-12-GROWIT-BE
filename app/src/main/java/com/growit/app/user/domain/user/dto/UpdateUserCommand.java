package com.growit.app.user.domain.user.dto;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.SajuInfo;

public record UpdateUserCommand(User user, String name, String lastName, SajuInfo sajuInfo) {}
