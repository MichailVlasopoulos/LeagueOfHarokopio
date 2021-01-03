package gr.hua.DistSysApp.ritoAPI.Models.Entities;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "authorities")
public class Authorities implements Serializable {

    /*
    @Column(name = "username")
    private String Username;
     */

    @Column(name = "role",nullable = false,length = 32)
    private String Role;

    //FK from user

    @Id
    @OneToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "username",referencedColumnName = "username")
    private User user;

    //TODO CHECK REFERENCED COLUMN : nullable = false,unique = true,length = 50

    public Authorities(String role, User user) {
        Role = role;
        this.user = user;
    }

    public Authorities() {
    }

    /*
    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
     */


    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    @Override
    public String toString() {
        return "Authorities{" +
                "Role='" + Role + '\'' +
                ", user=" + user +
                '}';
    }
}
