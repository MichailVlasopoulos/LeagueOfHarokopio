package gr.hua.DistSysApp.ritoAPI.Models.Entities;


import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "subscription_requests")
public class SubscriptionRequest {

    /*
    @Column(name = "user_id")
    private int user_id;
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_request_id",columnDefinition = "INT(10) UNSIGNED")
    private int subscription_request_id;

    @Column(name = "request_type",nullable = false,length = 64)
    private String request_type;

    //TODO check date type
    @Column(name = "created_at")
    private Timestamp created_at;

    @Column(name = "paysafe_pin",nullable = false,length = 64)
    private String paysafe_pin;

    //TODO check cascade type
    //FK from user
    @OneToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "user_id",nullable = false,referencedColumnName = "user_id")
    private User user;

    //PK to SubscriptionRequestsResults
    @OneToOne(mappedBy = "subscriptionRequest",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private SubscriptionRequestsResults subscriptionRequestsResults;

    public SubscriptionRequest() {
    }

    public SubscriptionRequest(int subscription_request_id, String request_type, Timestamp created_at, String paysafe_pin) {
        this.subscription_request_id = subscription_request_id;
        this.request_type = request_type;
        this.created_at = created_at;
        this.paysafe_pin = paysafe_pin;
    }

    public int getSubscription_request_id() {
        return subscription_request_id;
    }

    public void setSubscription_request_id(int subscription_request_id) {
        this.subscription_request_id = subscription_request_id;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp timestamp) {
        this.created_at = timestamp;
    }

    public String getPaysafe_pin() {
        return paysafe_pin;
    }

    public void setPaysafe_pin(String paysafe_pin) {
        this.paysafe_pin = paysafe_pin;
    }

    @Override
    public String toString() {
        return "SubscriptionRequest{" +
                ", subscription_request_id=" + subscription_request_id +
                ", request_type='" + request_type + '\'' +
                ", timestamp='" + created_at + '\'' +
                ", paysafe_pin='" + paysafe_pin + '\'' +
                '}';
    }
}
