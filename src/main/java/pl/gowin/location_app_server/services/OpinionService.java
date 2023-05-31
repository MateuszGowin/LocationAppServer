package pl.gowin.location_app_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.gowin.location_app_server.model.Opinion;
import pl.gowin.location_app_server.model.Place;
import pl.gowin.location_app_server.repositories.OpinionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OpinionService {

    private final OpinionRepository opinionRepository;

    @Autowired
    public OpinionService(OpinionRepository opinionRepository) {
        this.opinionRepository = opinionRepository;
    }

    public List<Opinion> findAll() {
        return opinionRepository.findAll();
    }

    public Optional<Opinion> finById(Long id) {
        return opinionRepository.findById(id);
    }

    public Opinion save(Opinion address) {
        return opinionRepository.save(address);
    }

    public void deleteById(Long id) {
        opinionRepository.deleteById(id);
    }

    public List<Opinion> findAllByPlace(Place place) {
        return opinionRepository.findAllByPlace(place);
    }

    public Double findAvgRatingForPlace(Place place) {
        return opinionRepository.findAvgRatingForPlace(place);
    }
}
