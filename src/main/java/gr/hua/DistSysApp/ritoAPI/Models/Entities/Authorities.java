package gr.hua.DistSysApp.ritoAPI.Models.Entities;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "authorities")
public class Authorities implements Serializable {

    /*
    @Id
    @Column(name = "username")
    private String Username;
    */

    @Id
    @Column(name = "user_id")
    private int user_id;


    @Column(name = "role",nullable = false,length = 32)
    private String Role;

    //FK from user

    @OneToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @MapsId
    @JoinColumn(name = "user_id",columnDefinition = "INT(10) UNSIGNED")
    private User user;

    //TODO CHECK REFERENCED COLUMN : nullable = false,unique = true,length = 50


    public Authorities() {

    }

    public Authorities(int user_id, String role) {
        this.user_id = user_id;
        Role = role;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    @Override
    public String toString() {
        return "Authorities{" +
                "user_id=" + user_id +
                ", Role='" + Role + '\'' +
                ", user=" + user +
                '}';
    }
}
