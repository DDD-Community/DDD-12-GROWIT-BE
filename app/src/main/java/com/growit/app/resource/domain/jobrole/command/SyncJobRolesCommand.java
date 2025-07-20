package com.growit.app.resource.domain.jobrole.command;

import com.growit.app.resource.domain.jobrole.JobRole;
import java.util.List;

public record SyncJobRolesCommand(List<JobRole> jobRoles) {}
