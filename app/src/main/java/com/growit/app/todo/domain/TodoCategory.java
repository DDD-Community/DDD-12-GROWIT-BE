package com.growit.app.todo.domain;

public enum TodoCategory {
  URGENT, // 긴급O + 중요O (긴급)
  CONSISTENT, // 긴급X + 중요O (꾸준히)
  DEFERABLE, // 긴급O + 중요X (넘겨도)
  DELETABLE // 긴급X + 중요X (지워도)
}
