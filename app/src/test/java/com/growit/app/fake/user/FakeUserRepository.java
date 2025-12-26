package com.growit.app.fake.user;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.vo.Email;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

  @Override
  public Page<User> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public java.util.List<User> findAllById(java.util.List<String> ids) {
    return store.values().stream()
        .filter(user -> ids.contains(user.getId()))
        .collect(java.util.stream.Collectors.toList());
  }
}
