package gr.hua.DistSysApp.ritoAPI.Models.Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",columnDefinition = "INT(10) UNSIGNED")
    private int id;

    @Column(name = "username",nullable = false,unique = true,length = 50)
    private String username;

    @Column(name = "first_name",nullable = false,length = 30)
    private String firstName;

    @Column(name = "last_name",nullable = false, length = 30)
    private String lastName;


    @Column(name = "email",nullable = false,unique = true,length = 50)
    private String email;

    @Column(name = "summoner_name",length = 50)
    private String summoner_name;

    @Column(name = "summoner_id",length = 128)
    private String summoner_id;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserPassword userPassword;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Authorities authorities;

    /*
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username",referencedColumnName = "username")
    private Authorities authorities;
     */


    /*
    @OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private SubscriptionRequest subscriptionRequest;
     */


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id") //TODO do i need this?
    private List<Request> requests;



    public User() {
    }

    public User(int id, String username, String firstName, String lastName, String email, String summoner_name, String summoner_id, UserPassword userPassword, List<Request> requests) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.summoner_name = summoner_name;
        this.summoner_id = summoner_id;
        this.userPassword = userPassword;
        this.requests = requests;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public UserPassword getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(UserPassword userPassword) {
        this.userPassword = userPassword;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public Authorities getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Authorities authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", summoner_name='" + summoner_name + '\'' +
                ", summoner_id='" + summoner_id + '\'' +
                ", userPassword=" + userPassword +
                ", requests=" + requests +
                '}';
    }
}
