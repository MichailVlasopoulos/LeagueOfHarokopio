package gr.hua.DistSysApp.ritoAPI.Repositories;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.RequestResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RequestResultsRepository extends JpaRepository<RequestResults,Integer> {

    @Query(value = "SELECT * FROM Requests_Results WHERE request_id=?",nativeQuery = true)
    RequestResults findRequestResultsByRequest_id(int request_id);

    @Query(value = "SELECT * FROM Requests_Results WHERE request_id=?",nativeQuery = true)
    List <RequestResults> findRequestResultsListByRequest_id(int request_id);

    @Query(value = "SELECT * FROM Requests_Results WHERE request.user_id=?",nativeQuery = true)
    List<RequestResults> findRequestResultsByUserId(int user_id);

    @Query(value = "SELECT * FROM Requests_Results WHERE request_status=?",nativeQuery = true)
    List<RequestResults> findRequestsByRequestStatus(String RequestStatus);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE Requests_Results SET request_status=?1,results=?2 WHERE request_id=?3",nativeQuery = true)
    Integer updateRequest_Accept(String request_status, String results,int requestId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE Request_Results SET request_status=?1 WHERE request_id=?2",nativeQuery = true)
    Integer updateRequest_Deny(String request_status,int request_id);
}
