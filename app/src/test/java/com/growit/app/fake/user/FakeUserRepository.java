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
  public Optional<User> findExistingUser(String provider, String providerId) {
    return Optional.empty();
  }

  @Override
  public boolean existsByUserIdAndProvider(String userId, String provider) {
    // 테스트 환경에서는 간단하게 false 리턴
    return false;
  }

  @Override
  public boolean hasAnyOAuthAccount(String userId) {
    // 테스트 환경에서는 간단하게 false 리턴 (순수 이메일 가입자로 가정)
    return false;
  }
}
