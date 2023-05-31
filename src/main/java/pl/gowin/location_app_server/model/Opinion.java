package pl.gowin.location_app_server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Opinion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rate;
    private String comment;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy")
    private Date  upload_date;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;
    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Opinion() {
    }

    public Opinion(Long id, int rate, String comment, Date upload_date, Place place, User user) {
        this.id = id;
        this.rate = rate;
        this.comment = comment;
        this.upload_date = upload_date;
        this.place = place;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(Date upload_date) {
        this.upload_date = upload_date;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
