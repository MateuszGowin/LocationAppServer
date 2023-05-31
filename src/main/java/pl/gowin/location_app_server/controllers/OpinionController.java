package pl.gowin.location_app_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gowin.location_app_server.exceptions.ResourceNotFoundException;
import pl.gowin.location_app_server.model.Opinion;
import pl.gowin.location_app_server.services.OpinionService;
import pl.gowin.location_app_server.services.PlaceService;
import pl.gowin.location_app_server.services.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/opinion")
public class OpinionController {

    private final OpinionService opinions;
    private final UserService users;
    private final PlaceService places;

    @Autowired
    public OpinionController(OpinionService opinions, UserService users, PlaceService places) {
        this.opinions = opinions;
        this.users = users;
        this.places = places;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Opinion>> getAllOpinions() {
        List<Opinion> allOpinions = new ArrayList<>(opinions.findAll());
        if(allOpinions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allOpinions,HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Opinion> getOpinion(@RequestParam Long id) {
        Opinion opinion = opinions.finById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Opinion with id = " + id));
        return new ResponseEntity<>(opinion,HttpStatus.OK);
    }

    @GetMapping("/allOpinionsForPlace")
    public ResponseEntity<List<Opinion>> getAllOpinionsForPlace(@RequestParam Long placeId) {
        List<Opinion> allOpinionsForPlace= opinions.findAllByPlace(places.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Place with id = " + placeId)));
        return new ResponseEntity<>(allOpinionsForPlace,HttpStatus.OK);
    }

    @GetMapping("/findAvgRatingForPlace")
    public ResponseEntity<Double> getAvgRatingForPlace(@RequestParam Long placeId) {
        Double avg = opinions.findAvgRatingForPlace(places.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Place with id = " + placeId)));
        return new ResponseEntity<>(avg,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Opinion> addOpinion(@RequestParam Long placeId, @RequestParam Long userId, @RequestBody Opinion opinion) {
        opinion.setUser(users.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user with id = " + userId)));

        opinion.setPlace(places.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found place with id = " + placeId)));

        return new ResponseEntity<>(opinions.save(opinion),HttpStatus.CREATED);
    }

    @DeleteMapping("")
    public ResponseEntity<HttpStatus> deleteOpinion(@RequestParam Long id) {
        Opinion opinion = opinions.finById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found Opinion with id = " + id));
        opinions.deleteById(opinion.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
