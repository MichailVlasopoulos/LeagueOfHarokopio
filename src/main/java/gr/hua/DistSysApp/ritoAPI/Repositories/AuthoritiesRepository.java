package gr.hua.DistSysApp.ritoAPI.Repositories;

import gr.hua.DistSysApp.ritoAPI.Models.Entities.Authorities;
import gr.hua.DistSysApp.ritoAPI.Models.Entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authorities,Integer> {

    @Query(value = "SELECT * FROM Authorities WHERE role='ROLE_USER' OR role='ROLE_PREMIUM_USER' ",nativeQuery = true)
    List<Authorities> getAllUsers();

    @Query(value = "SELECT * FROM Authorities WHERE role='ROLE_ADMIN' OR role='ROLE_PREMIUM_ADMIN' OR role='ROLE_MAIN_ADMIN' ",nativeQuery = true)
    List<Authorities> getAllAdmins();

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE Authorities SET role='ROLE_PREMIUM_USER' WHERE user_id=?", nativeQuery = true)
    void goPremium(int user_id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE Authorities SET role='ROLE_USER' WHERE user_id=?", nativeQuery = true)
    void cancelPremium(int user_id);
}
