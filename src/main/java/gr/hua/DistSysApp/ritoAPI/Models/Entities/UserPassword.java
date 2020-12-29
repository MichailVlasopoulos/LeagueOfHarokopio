package gr.hua.DistSysApp.ritoAPI.Models.Entities;


import javax.persistence.*;

@Entity
@Table(name = "user_Password")
public class UserPassword {


    @Id
    @Column(name = "user_id")
    private int user_id;

    @Column(name = "password_hash")
    private String password_hash;

    //FK from user
    @OneToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @MapsId
    //@JoinColumn(name = "user_id")
    private User user;

    public UserPassword(int user_id, String password_hash) {
        this.user_id = user_id;
        this.password_hash = password_hash;
    }

    public UserPassword() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    @Override
    public String toString() {
        return "UserPassword{" +
                "user_id=" + user_id +
                ", password_hash='" + password_hash + '\'' +
                '}';
    }
}
