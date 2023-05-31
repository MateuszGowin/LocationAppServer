package pl.gowin.location_app_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gowin.location_app_server.exceptions.BadRequestException;
import pl.gowin.location_app_server.exceptions.ResourceNotFoundException;
import pl.gowin.location_app_server.model.Address;
import pl.gowin.location_app_server.model.Opinion;
import pl.gowin.location_app_server.model.Place;
import pl.gowin.location_app_server.model.Type;
import pl.gowin.location_app_server.services.AddressService;
import pl.gowin.location_app_server.services.OpinionService;
import pl.gowin.location_app_server.services.PlaceService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/place")
public class PlaceController {

    private final PlaceService places;
    private final AddressService addresses;
    private final OpinionService opinions;

    @Autowired
    PlaceController(PlaceService places, AddressService addresses,OpinionService opinions){
        this.places = places;
        this.addresses = addresses;
        this.opinions = opinions;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Place>> getAllPlaces(){
        List<Place> allPlaces = new ArrayList<>(places.findAll());
        if(allPlaces.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allPlaces, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Place> getPlace(@RequestParam Long placeId){
        Place place = places.findById(placeId).
                orElseThrow(()-> new ResourceNotFoundException("Not found Place with id = " + placeId));
        return new ResponseEntity<>(place,HttpStatus.OK);
    }

    @GetMapping("/allByType/{type}")
    public ResponseEntity<List<Place>> getAllPlacesByType(@PathVariable("type") Type type){
        List<Place> allPlaceByType = new ArrayList<>(places.findByType(type));
        if(allPlaceByType.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allPlaceByType,HttpStatus.OK);
    }

    @GetMapping("/allNearbyPlaces/{latitude}/{longitude}/{radius}")
    public ResponseEntity<List<Place>> getAllNearbyPlaces(@PathVariable("latitude") BigDecimal latitude,@PathVariable("longitude") BigDecimal longitude,@PathVariable("radius") int radius){
        List<Place> allNearbyPlaces = new ArrayList<>(places.findNearbyPlace(latitude,longitude,radius));
        if(allNearbyPlaces.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allNearbyPlaces,HttpStatus.OK);
    }

    @GetMapping("/allNearbyPlaces/{latitude}/{longitude}/{radius}/{types}")
    public ResponseEntity<List<Place>> getAllNearbyPlaces(@PathVariable("latitude") BigDecimal latitude,@PathVariable("longitude") BigDecimal longitude,@PathVariable("radius") int radius,@PathVariable("types") List<Type> types){
        List<Place> allNearbyPlaces = new ArrayList<>(places.findNearbyPlaces(latitude,longitude,radius,types));
        if(allNearbyPlaces.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allNearbyPlaces,HttpStatus.OK);
    }

    @GetMapping("/{placeId}/opinions")
    public ResponseEntity<List<Opinion>> getAllOpinionsForPlace(@PathVariable("placeId") Long placeId) {
        List<Opinion> allOpinionsForPlace= opinions.findAllByPlace(places.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Place with id = " + placeId)));
        return new ResponseEntity<>(allOpinionsForPlace,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Place> createPlace(@RequestBody Place place){
        if(addresses.findByStreetAndNumberAndCity(place.getAddress().getStreet(),place.getAddress().getNumber(),place.getAddress().getCity()).isEmpty()){
            places.save(place);
        } else {
            throw new BadRequestException("A Place with this " + place.getAddress().toString() + " already exists");
        }
        return new ResponseEntity<>(place,HttpStatus.CREATED);
    }

    @DeleteMapping("")
    public ResponseEntity<HttpStatus> deletePlace(@RequestParam Long id){
        Place place = places.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found Place with id = " + id));
        if(place.getOpinions().isEmpty())
            places.deleteById(place.getId());
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("")
    public ResponseEntity<Place> updatePlace(@RequestParam Long id,@RequestBody Place place){
        Place place_ = places.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Place with id = " + id));

        place_.setName(place.getName());
        place_.setType(place.getType());
        place_.setLatitude(place.getLatitude());
        place_.setLongitude(place.getLongitude());
        place_.setIs_accepted(true);

        Address address = place_.getAddress();
        address.setStreet(place.getAddress().getStreet());
        address.setCity(place.getAddress().getCity());
        address.setNumber(place.getAddress().getNumber());

        addresses.save(address);
        place_.setAddress(address);
        return new ResponseEntity<>(places.save(place_),HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<Place> acceptPlace(@RequestParam Long id) {
        Place place = places.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Place with id = " + id));
        return new ResponseEntity<>(places.acceptPlace(place),HttpStatus.OK);
    }
}
