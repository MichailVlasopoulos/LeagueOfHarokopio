package gr.hua.DistSysApp.ritoAPI.Repositories;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.RequestResults;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.SubscriptionRequestsResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubscriptionRequestResultsRepository extends JpaRepository<SubscriptionRequestsResults, Long> {

    @Query(value = "SELECT * FROM subscription_requests_results WHERE subscription_request_id=?",nativeQuery = true)
    SubscriptionRequestsResults findSubscriptionRequestResultsByRequest_id(int subscription_request_id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE subscription_requests_results SET request_status=?1 WHERE subscription_request_id=?2",nativeQuery = true)
    void updateSubscriptionRequest(String request_status, int requestId);

    @Query(value = "SELECT * FROM subscription_requests_results WHERE request_status=?",nativeQuery = true)
    List<SubscriptionRequestsResults> findRequestsResultsByRequestStatus(String RequestStatus);
}
