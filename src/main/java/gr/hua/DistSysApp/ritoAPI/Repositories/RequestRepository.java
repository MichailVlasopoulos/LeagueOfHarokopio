package gr.hua.DistSysApp.ritoAPI.Repositories;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(value = "SELECT * FROM Request WHERE request_id=? AND user_id=?",nativeQuery = true)
    Request findRequestByRequest_idAndUserid(int requestId, int userId);
}
