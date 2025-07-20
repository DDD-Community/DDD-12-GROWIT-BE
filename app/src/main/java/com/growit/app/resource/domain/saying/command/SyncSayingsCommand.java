package com.growit.app.resource.domain.saying.command;

import com.growit.app.resource.domain.saying.Saying;
import java.util.List;

public record SyncSayingsCommand(List<Saying> sayings) {}
