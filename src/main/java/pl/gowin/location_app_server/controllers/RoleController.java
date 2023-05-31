package pl.gowin.location_app_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gowin.location_app_server.exceptions.BadRequestException;
import pl.gowin.location_app_server.exceptions.ResourceNotFoundException;
import pl.gowin.location_app_server.model.Role;
import pl.gowin.location_app_server.services.RoleService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roles;

    @Autowired
    RoleController(RoleService roles) {
        this.roles = roles;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAll() {
        List<Role> allRoles = new ArrayList<>(roles.findAll());
        if(allRoles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allRoles, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        if(roles.findByName(role.getName()).isPresent())
            throw new BadRequestException("Role " + role.getName() + " already exists");
        return new ResponseEntity<>(roles.save(role),HttpStatus.CREATED);
    }

    @DeleteMapping("")
    public ResponseEntity<HttpStatus> deleteRole(@RequestParam Long id) {
        Role role = roles.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found Role with id = " + id));
        roles.deleteById(role.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
