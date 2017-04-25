package tec.psa.service;

import tec.psa.model.User;

public interface UserService {
	public User findUserByUsername(String username);
	public void saveUser(User user);
}