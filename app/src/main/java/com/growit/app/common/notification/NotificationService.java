package com.growit.app.common.notification;

public interface NotificationService {
  void sendErrorNotification(String uri, String method, String errorType, String message);
}
