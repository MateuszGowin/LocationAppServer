package pl.gowin.location_app_server;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.gowin.location_app_server.model.Role;
import pl.gowin.location_app_server.model.User;
import pl.gowin.location_app_server.repositories.RoleRepository;
import pl.gowin.location_app_server.repositories.UserRepository;

import java.util.Optional;

@Configuration
public class initDB {
    public initDB(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {

        Role roleAdmin = new Role();
        Role roleUser = new Role();
        roleAdmin.setName("ROLE_ADMIN");
        roleUser.setName("ROLE_USER");

//        roleRepository.save(roleAdmin);
//        roleRepository.save(roleUser);

        User user1 = new User();
        user1.setEmail("admin");
        user1.setPassword(passwordEncoder.encode("admin"));

        Optional<Role> role = roleRepository.findByName("ROLE_ADMIN");
        user1.setFirstName("Kuba");
        user1.setLastName("Kowalski");
        user1.addRole(role.get());

        User user2 = new User();
        user2.setEmail("user");
        user2.setPassword(passwordEncoder.encode("user"));
        Optional<Role> role2 = roleRepository.findByName("ROLE_USER");
        user2.setFirstName("Kuba");
        user2.setLastName("Kowalski");
        user2.addRole(role2.get());

//        userRepository.save(user1);
//        userRepository.save(user2);
    }
}
