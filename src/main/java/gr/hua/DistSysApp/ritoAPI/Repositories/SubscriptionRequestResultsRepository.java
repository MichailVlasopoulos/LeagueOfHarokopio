package gr.hua.DistSysApp.ritoAPI.Repositories;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.SubscriptionRequestsResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubscriptionRequestResultsRepository extends JpaRepository<SubscriptionRequestsResults, Long> {

    @Query(value = "SELECT * FROM Subscription_Requests_Results WHERE subscription_request_id=?",nativeQuery = true)
    SubscriptionRequestsResults findSubscriptionRequestResultsByRequest_id(int subscription_request_id);


}
