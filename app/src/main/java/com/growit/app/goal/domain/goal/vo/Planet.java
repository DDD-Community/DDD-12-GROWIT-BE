package com.growit.app.goal.domain.goal.vo;

import java.util.Objects;

public record Planet(String name, String imageDone, String imageProgress) {
    
    public Planet {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Planet name cannot be null or empty");
        }
        if (imageDone == null || imageDone.trim().isEmpty()) {
            throw new IllegalArgumentException("Planet done image cannot be null or empty");
        }
        if (imageProgress == null || imageProgress.trim().isEmpty()) {
            throw new IllegalArgumentException("Planet progress image cannot be null or empty");
        }
    }
    
    public static Planet of(String name, String imageDone, String imageProgress) {
        return new Planet(name, imageDone, imageProgress);
    }
    
    public String getImageByStatus(boolean isCompleted) {
        return isCompleted ? imageDone : imageProgress;
    }
}