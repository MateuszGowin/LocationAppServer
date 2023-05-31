package pl.gowin.location_app_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.gowin.location_app_server.model.Place;
import pl.gowin.location_app_server.model.Type;
import pl.gowin.location_app_server.repositories.PlaceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    @Autowired
    PlaceService(PlaceRepository placeRepository){
        this.placeRepository = placeRepository;
    }

    public List<Place> findAll() {
        return placeRepository.findAll();
    }

    public Optional<Place> findById(Long id) {
        return placeRepository.findById(id);
    }

    public Place save(Place place) {
        place.setIs_accepted(false);
        return placeRepository.save(place);
    }

    public void deleteById(Long id) {
        placeRepository.deleteById(id);
    }

    public Place acceptPlace(Place place) {
        place.setIs_accepted(true);
        return placeRepository.save(place);
    }
    public List<Place> findByType(Type type){
        return placeRepository.findByType(type);
    }

    public List<Place> findNearbyPlace(BigDecimal latitude, BigDecimal longitude, int radius){
        return placeRepository.findNearbyPlaces(latitude,longitude,radius);
    }
    public List<Place> findNearbyPlaces(BigDecimal latitude, BigDecimal longitude, int radius, List<Type> types){
        return placeRepository.findNearbyPlaces(latitude,longitude,radius, types.stream().map(Objects::toString).collect(Collectors.toList()));
    }

}
