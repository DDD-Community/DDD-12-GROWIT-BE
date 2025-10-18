package com.growit.app.user.domain.userstats.service;

import com.growit.app.user.domain.userstats.UserStats;
import java.util.List;
import java.util.Set;

public interface ActiveUserQuery {

  Set<String> getActiveUserIds(List<UserStats> userStatsList, int diffDay);
}
