package pl.gowin.location_app_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.gowin.location_app_server.model.Opinion;
import pl.gowin.location_app_server.model.Place;

import java.math.BigDecimal;
import java.util.List;

public interface OpinionRepository extends JpaRepository <Opinion,Long> {

    List<Opinion> findAllByPlace(Place place);

    @Query ("select avg(e.rate) from Opinion e where e.place = :place")
    Double findAvgRatingForPlace(@Param("place") Place place);

}
