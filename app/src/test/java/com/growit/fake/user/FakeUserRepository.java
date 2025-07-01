package com.growit.fake.user;

import com.growit.user.domain.user.User;
import com.growit.user.domain.user.UserRepository;
import com.growit.user.domain.user.vo.Email;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FakeUserRepository implements UserRepository {
  private final Map<String, User> store = new ConcurrentHashMap<>();

  @Override
  public Optional<User> findByEmail(Email email) {
    return store.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
  }

  @Override
  public Optional<User> findUserByuId(String id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public void saveUser(User user) {
    store.put(user.getId(), user);
  }
}
