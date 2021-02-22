package lk.cj.testapp.repository;

import lk.cj.testapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByUsername(String username);
}
