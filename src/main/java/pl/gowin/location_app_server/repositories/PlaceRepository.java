package pl.gowin.location_app_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.gowin.location_app_server.model.Place;
import pl.gowin.location_app_server.model.Type;

import java.math.BigDecimal;
import java.util.List;

public interface PlaceRepository extends JpaRepository <Place,Long> {


    List<Place> findByType(Type type);

    @Query(value="select *,(6371000 * Acos(Cos(Radians(?1)) * Cos(Radians(latitude)) * Cos(Radians(longitude) - Radians(?2)) + Sin (Radians(?1)) * Sin(Radians(latitude)))" +
            ") AS distance_m from PLACE " +
            " having distance_m < ?3 order by distance_m",nativeQuery = true)
    List<Place> findNearbyPlaces(BigDecimal latitude, BigDecimal longitude, int radius);

    @Query(value="select *,(6371000 * Acos(Cos(Radians(?1)) * Cos(Radians(latitude)) * Cos(Radians(longitude) - Radians(?2)) + Sin (Radians(?1)) * Sin(Radians(latitude)))" +
            ") AS distance_m from PLACE " +
            " WHERE type IN ?4 " +
            " having distance_m < ?3 order by distance_m",nativeQuery = true)
    List<Place> findNearbyPlaces(BigDecimal latitude, BigDecimal longitude, int radius,List<String> types);
    }
