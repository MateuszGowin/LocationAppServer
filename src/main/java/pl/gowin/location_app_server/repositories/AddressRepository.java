package pl.gowin.location_app_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gowin.location_app_server.model.Address;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {
    Optional<Address> findByStreetAndNumberAndCity(String street, int number, String city);
}
