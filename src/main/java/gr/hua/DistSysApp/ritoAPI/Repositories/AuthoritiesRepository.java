package gr.hua.DistSysApp.ritoAPI.Repositories;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Authorities;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authorities,Integer> {

    @Query(value = "SELECT * FROM Authorities WHERE role=ROLE_User OR role=ROLE_PremiumUser ",nativeQuery = true)
    List<Authorities> getAllUsers();

    @Query(value = "SELECT * FROM Authorities WHERE role=ROLE_UserAdmin OR role=ROLE_PremiumAdmin OR role=ROLE_MainAdmin ",nativeQuery = true)
    List<Authorities> getAllAdmins();
}
