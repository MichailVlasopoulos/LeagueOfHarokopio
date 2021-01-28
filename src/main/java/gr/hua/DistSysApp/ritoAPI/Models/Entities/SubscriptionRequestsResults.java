package gr.hua.DistSysApp.ritoAPI.Models.Entities;


import javax.persistence.*;


@Entity
@Table(name = "subscription_requests_results")
public class SubscriptionRequestsResults {

    @Id
    @Column(name = "subscription_request_id")
    private int subscription_request_id;

    @Column(name = "request_status",nullable = false,length = 16)
    private String request_status;


    //FK from Subscription_Requests
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @MapsId
    @JoinColumn(name = "subscription_request_id",columnDefinition = "INT(10) UNSIGNED")
    private SubscriptionRequest subscriptionRequest;

    public SubscriptionRequestsResults() {
    }

    public SubscriptionRequestsResults(int subscription_request_id, String request_status, SubscriptionRequest subscriptionRequest) {
        this.subscription_request_id = subscription_request_id;
        this.request_status = request_status;
        this.subscriptionRequest = subscriptionRequest;
    }

    public SubscriptionRequest getSubscriptionRequest() {
        return subscriptionRequest;
    }

    public void setSubscriptionRequest(SubscriptionRequest subscriptionRequest) {
        this.subscriptionRequest = subscriptionRequest;
    }

    public int getSubscription_request_id() {
        return subscription_request_id;
    }

    public void setSubscription_request_id(int request_id) {
        this.subscription_request_id = request_id;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }




    @Override
    public String toString() {
        return "SubscriptionRequestsResults{" +
                "request_id=" + subscription_request_id +
                ", request_status='" + request_status + '\'' +
                '}';
    }
}
