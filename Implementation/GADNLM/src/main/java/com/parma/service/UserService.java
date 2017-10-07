package com.parma.service;

import com.parma.model.User;

public interface UserService {

  void save(User user);

  User findByUsername(String username);

}
