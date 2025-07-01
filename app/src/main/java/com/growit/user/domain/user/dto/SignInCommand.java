package com.growit.user.domain.user.dto;

import com.growit.user.domain.user.vo.Email;

public record SignInCommand(Email email, String password) {}
