package gr.hua.DistSysApp.ritoAPI.Repositories;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.SubscriptionRequestsResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SubscriptionRequestResultsRepository extends JpaRepository<SubscriptionRequestsResults, Long> {

    @Query(value = "SELECT * FROM Subscription_Requests_Results WHERE subscription_request_id=?",nativeQuery = true)
    SubscriptionRequestsResults findSubscriptionRequestResultsByRequest_id(int subscription_request_id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE Subscription_Requests_Results SET request_status=?1 WHERE subscription_request_id=?2",nativeQuery = true)
    void acceptSubscriptionRequest(String request_status, int requestId);

}
