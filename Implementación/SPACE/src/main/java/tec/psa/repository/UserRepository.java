package tec.psa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tec.psa.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, String> {
	 User findByUsername(String username);
}