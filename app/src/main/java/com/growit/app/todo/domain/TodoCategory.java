package com.growit.app.todo.domain;

public enum TodoCategory {
  NOW, // 긴급O + 중요O (지금/빨리 끝내기)
  STEADY, // 긴급X + 중요O (꾸준히/천천히 끝내기)
  SKIP, // 긴급O + 중요X (넘겨도)
  DELETE // 긴급X + 중요X (지워도)
}
