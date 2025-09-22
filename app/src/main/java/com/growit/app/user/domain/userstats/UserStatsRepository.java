package com.growit.app.user.domain.userstats;


public interface UserStatsRepository {
    UserStats findByUserId(String userId);
}