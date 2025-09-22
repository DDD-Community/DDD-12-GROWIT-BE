package com.growit.app.user.domain.userstats;

import java.time.LocalDate;

public record UserStats(String userId, LocalDate lastSeenDate, int streakLen) {

}
