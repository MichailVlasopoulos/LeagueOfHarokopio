package gr.hua.DistSysApp.ritoAPI.Models.Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "summoner_name")
    private String summoner_name;

    @Column(name = "summoner_id")
    private String summoner_id;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserPassword userPassword;

    @OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "username",referencedColumnName = "username")
    private Authorities authorities;

    @OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private SubscriptionRequest subscriptionRequest;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    //@JoinColumn(name = "user_id", referencedColumnName = "user_id") //TODO do i need this?
    private List<Request> requests;



    public User() {
    }

    public User(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSummoner_name() {
        return summoner_name;
    }

    public void setSummoner_name(String summoner_name) {
        this.summoner_name = summoner_name;
    }

    public String getSummoner_id() {
        return summoner_id;
    }

    public void setSummoner_id(String summoner_id) {
        this.summoner_id = summoner_id;
    }
}
