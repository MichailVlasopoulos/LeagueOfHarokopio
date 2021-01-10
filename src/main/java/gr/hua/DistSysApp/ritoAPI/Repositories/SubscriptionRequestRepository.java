package gr.hua.DistSysApp.ritoAPI.Repositories;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.SubscriptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public interface SubscriptionRequestRepository extends JpaRepository<SubscriptionRequest, Long> {

    @Query(value = "SELECT * FROM Subscription_Requests WHERE subscription_request_id=? AND user_id=?",nativeQuery = true)
    SubscriptionRequest findSubscriptionRequestByRequest_idAndUserid(int subscriptionRequestId, int userId);

    @Query(value = "SELECT * FROM Subscription_Requests WHERE subscription_request_id=?",nativeQuery = true)
    SubscriptionRequest findSubscriptionRequestByRequest_id(int subscriptionRequestId);

    @Query(value = "SELECT * FROM Subscription_Requests WHERE created_at=?",nativeQuery = true)
    SubscriptionRequest findSubscriptionRequestByTimestamp(Timestamp timestamp);

    @Query(value = "SELECT subscription_request_id FROM Subscription_Requests WHERE user_id=?1 AND request_type=?2",nativeQuery = true)
    int findSubscriptionRequestIDByUseridAndRequestType(int UserId, String RequestType);

    @Query(value = "SELECT subscription_request_id FROM Subscription_Requests WHERE user_id=?1 AND request_type=?2 ORDER BY created_at DESC LIMIT 1",nativeQuery = true)
    int findSubscriptionRequestIDByUseridAndRequestTypeOrdered(int UserId, String RequestType);

    @Query(value = "SELECT * FROM Subscription_Requests WHERE user_id=?1 AND request_type=?2",nativeQuery = true)
    SubscriptionRequest findSubscriptionRequestByUseridAndRequestType(int UserId, String RequestType);

    @Query(value = "SELECT * FROM Subscription_Requests WHERE user_id=?1 AND request_type=?2 ORDER BY created_at DESC LIMIT 1",nativeQuery = true)
    SubscriptionRequest findSubscriptionRequestByUseridAndRequestTypeOrdered(int UserId, String RequestType);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE Subscription_Requests SET request_status=?1 WHERE request_id=?2",nativeQuery = true)
    SubscriptionRequest acceptSubscriptionRequest(String request_status, int requestId);
}
