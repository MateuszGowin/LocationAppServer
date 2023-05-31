package pl.gowin.location_app_server.controllers;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gowin.location_app_server.exceptions.BadRequestException;
import pl.gowin.location_app_server.exceptions.ResourceNotFoundException;
import pl.gowin.location_app_server.model.User;
import pl.gowin.location_app_server.model.VerifyUserRequest;
import pl.gowin.location_app_server.services.EmailService;
import pl.gowin.location_app_server.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService users;
    private final EmailService emailService;


    @Autowired
    UserController(UserService users, EmailService emailService) {
        this.users = users;
        this.emailService = emailService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = new ArrayList<>(users.findAll());
        if(allUsers.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<User> getUser(@RequestParam Long id) {
        User user = users.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + id));
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUser(@RequestParam String email) {
        User user = users.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with email = " + email));
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<User> addUser(@RequestBody User user) throws MessagingException {
        if(users.findByEmail(user.getEmail()).isPresent())
            throw new BadRequestException("This email: '" + user.getEmail() + "' is already used");
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        String link = "http://localhost:4200/verify?email="+user.getEmail()+"&token="+user.getVerificationToken();
        emailService.sendVerifyToken(user.getEmail(), link);
        return new ResponseEntity<>(users.save(user), HttpStatus.CREATED);
    }

    @DeleteMapping("")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam Long id) {
        User user = users.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + id));
        users.deleteById(user.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/verify")
    public ResponseEntity<HttpStatus> verifyUser(@RequestBody VerifyUserRequest verifyUserRequest) {
        User user = users.findByEmail(verifyUserRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with email = " + verifyUserRequest.getEmail()));
        if(user.getVerificationToken().equals(verifyUserRequest.getToken())) {
            user.setVerificationToken(null);
            user.setEnabled(true);
            users.enableUser(user);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
