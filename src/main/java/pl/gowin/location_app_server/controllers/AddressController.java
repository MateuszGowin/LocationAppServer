package pl.gowin.location_app_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gowin.location_app_server.exceptions.ResourceNotFoundException;
import pl.gowin.location_app_server.model.Address;
import pl.gowin.location_app_server.services.AddressService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {
    private final AddressService addresses;

    @Autowired
    public AddressController(AddressService addresses) {
        this.addresses = addresses;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> allAddresses = new ArrayList<>(addresses.findAll());
        if(allAddresses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allAddresses,HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Address> getAddress(@RequestParam Long id) {
        Address address = addresses.finById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Address with id = " + id));
        return new ResponseEntity<>(address,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        return new ResponseEntity<>(address,HttpStatus.CREATED);
    }

    @DeleteMapping("")
    public ResponseEntity<HttpStatus> deleteAddress(@RequestParam Long id) {
        Address address = addresses.finById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found Address with id = " +  id));
        addresses.deleteById(address.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("")
    public ResponseEntity<Address> updateAddress(@RequestParam Long id, @RequestBody Address address) {
        Address address_ = addresses.finById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Address with id = " + id));

        address_.setCity(address.getCity());
        address_.setNumber(address.getNumber());
        address_.setStreet(address.getStreet());

        return new ResponseEntity<>(addresses.save(address_),HttpStatus.OK);
    }
}
