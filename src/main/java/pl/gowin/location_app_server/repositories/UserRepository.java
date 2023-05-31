package pl.gowin.location_app_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gowin.location_app_server.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,Long> {
    Optional<User> findByEmail(String email);
}
