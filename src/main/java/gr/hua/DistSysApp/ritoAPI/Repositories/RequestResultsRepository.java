package gr.hua.DistSysApp.ritoAPI.Repositories;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.RequestResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RequestResultsRepository extends JpaRepository<RequestResults,Integer> {

    @Query(value = "SELECT * FROM RequestResults WHERE request_id=?",nativeQuery = true)
    RequestResults findRequestByRequest_id(int request_id);
}
