package com.growit.app.user.domain.user.dto;

import com.growit.app.user.domain.user.vo.Email;

public record SignInCommand(Email email, String password) {
}
