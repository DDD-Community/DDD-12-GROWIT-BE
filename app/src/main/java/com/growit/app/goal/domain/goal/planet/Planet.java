package com.growit.app.goal.domain.goal.planet;


public record Planet(Long id, String name, String imageDone, String imageProgress) {

    public static Planet of(Long id, String name, String imageDone, String imageProgress) {
        return new Planet(id, name, imageDone, imageProgress);
    }

    public String getImageByStatus(boolean isCompleted) {
        return isCompleted ? imageDone : imageProgress;
    }
}
