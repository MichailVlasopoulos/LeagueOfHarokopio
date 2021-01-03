package gr.hua.DistSysApp.ritoAPI.Repositories;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}
