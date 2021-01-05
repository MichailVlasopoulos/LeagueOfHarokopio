package gr.hua.DistSysApp.ritoAPI.Repositories;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface SubscriptionRequestRepository extends JpaRepository<Request, Long> {

    @Query(value = "SELECT * FROM Subscription_Requests WHERE subscription_request_id=? AND user_id=?",nativeQuery = true)
    Request findSubscriptionRequestByRequest_idAndUserid(int subscriptionRequestId, int userId);

    @Query(value = "SELECT * FROM Subscription_Requests WHERE subscription_request_id=?",nativeQuery = true)
    Request findSubscriptionRequestByRequest_id(int subscriptionRequestId);

    @Query(value = "SELECT * FROM Subscription_Requests WHERE created_at=?",nativeQuery = true)
    Request findSubscriptionRequestByTimestamp(Timestamp timestamp);

    @Query(value = "SELECT subscription_request_id FROM Subscription_Requests WHERE user_id=?1 AND request_type=?2",nativeQuery = true)
    int findSubscriptionRequestIDByUseridAndRequestType(int UserId, String RequestType);

    @Query(value = "SELECT subscription_request_id FROM Subscription_Requests WHERE user_id=?1 AND request_type=?2 ORDER BY created_at DESC LIMIT 1",nativeQuery = true)
    int findSubscriptionRequestIDByUseridAndRequestTypeOrdered(int UserId, String RequestType);

    @Query(value = "SELECT * FROM Subscription_Requests WHERE user_id=?1 AND request_type=?2",nativeQuery = true)
    Request findSubscriptionRequestByUseridAndRequestType(int UserId, String RequestType);
}
