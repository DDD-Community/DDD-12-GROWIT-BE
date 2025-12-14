package com.growit.app.goal.domain.anlaysis;

public record GoalAnalysis(int todoCompletedRate, String summary) {

    public GoalAnalysis {
        if (todoCompletedRate < 0 || todoCompletedRate > 100) {
            throw new IllegalArgumentException("Todo completed rate must be between 0 and 100");
        }
        if (summary == null || summary.trim().isEmpty()) {
            throw new IllegalArgumentException("Summary cannot be null or empty");
        }
    }

    public static GoalAnalysis of(int todoCompletedRate, String summary) {
        return new GoalAnalysis(todoCompletedRate, summary);
    }

    public boolean isWellPerformed() {
        return todoCompletedRate >= 80;
    }

    public String getPerformanceLevel() {
        if (todoCompletedRate >= 90) {
            return "EXCELLENT";
        } else if (todoCompletedRate >= 70) {
            return "GOOD";
        } else if (todoCompletedRate >= 50) {
            return "FAIR";
        } else {
            return "POOR";
        }
    }
}
