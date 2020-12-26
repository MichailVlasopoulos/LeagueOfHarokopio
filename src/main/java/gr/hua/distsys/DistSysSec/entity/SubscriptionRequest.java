package com.example.springbootmvc.entity;


import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Subscription_Requests")
public class SubscriptionRequest {

    @Column(name = "user_id")
    private int user_id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_request_id")
    private int subscription_request_id;

    @Column(name = "request_type")
    private String request_type;

    //TODO check date type
    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "paysafe_pin")
    private String paysafe_pin;

    //TODO check cascade type
    //FK from user
    @OneToOne(mappedBy = "Subscription_Requests",cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    private User user;

    //PK to SubscriptionRequestsResults
    @OneToOne(mappedBy = "subscription_request_id",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private SubscriptionRequestsResults subscriptionRequestsResults;

    public SubscriptionRequest() {
    }

    public SubscriptionRequest(int user_id, int subscription_request_id, String request_type, Date created_at, String paysafe_pin) {
        this.user_id = user_id;
        this.subscription_request_id = subscription_request_id;
        this.request_type = request_type;
        this.created_at = created_at;
        this.paysafe_pin = paysafe_pin;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date timestamp) {
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
                "user_id=" + user_id +
                ", subscription_request_id=" + subscription_request_id +
                ", request_type='" + request_type + '\'' +
                ", timestamp='" + created_at + '\'' +
                ", paysafe_pin='" + paysafe_pin + '\'' +
                '}';
    }
}
