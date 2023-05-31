package pl.gowin.location_app_server.model;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private boolean isEnabled;

    public JwtResponse(String accessToken, Long id, String username, List<String> roles, String firstName, String lastName, boolean isEnabled) {
        this.token = accessToken;
        this.id = id;
        this.email = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.isEnabled = isEnabled;
    }


    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return email;
    }

    public void setUsername(String username) {
        this.email = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setisEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
