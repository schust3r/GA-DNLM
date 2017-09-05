package tec.psa.service;

import tec.psa.model.User;

public interface UserService {
  void save(User user);

  User findByUsername(String username);
}