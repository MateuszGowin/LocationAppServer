package pl.gowin.location_app_server.controllers;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.gowin.location_app_server.exceptions.BadRequestException;
import pl.gowin.location_app_server.exceptions.ResourceNotFoundException;
import pl.gowin.location_app_server.model.*;
import pl.gowin.location_app_server.security.JwtUtils;
import pl.gowin.location_app_server.repositories.RoleRepository;
import pl.gowin.location_app_server.repositories.UserRepository;
import pl.gowin.location_app_server.services.EmailService;
import pl.gowin.location_app_server.services.UserService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;
    private final UserService users;
    private final EmailService emailService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, UserService users,EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.users = users;
        this.emailService = emailService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles,
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.isUserEnabled()));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<HttpStatus> forgotPassword(@RequestParam String email) throws MessagingException {
        String token = UUID.randomUUID().toString();
        users.updateResetPasswordToken(token,email);
        String link = "http://localhost:4200/recover?email="+email+"&token="+token;
        emailService.sendRecoverPasswordToken(email,link);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<HttpStatus> resetPassword(@RequestBody ResetPassword resetPassword) {
        User user = users.findByEmail(resetPassword.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with email = " + resetPassword.getEmail()));
        if(user.getResetPasswordToken().equals(resetPassword.getToken())) {
            users.updatePassword(user,resetPassword.getPassword());
        } else {
            throw new BadRequestException("Invalid token = " + resetPassword.getToken());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
