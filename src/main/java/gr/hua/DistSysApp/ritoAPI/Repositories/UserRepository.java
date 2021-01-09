package gr.hua.DistSysApp.ritoAPI.Repositories;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete





import gr.hua.DistSysApp.ritoAPI.Models.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends JpaRepository<User, Integer> {


    User findByUsername(String username);

    User findById(int id);



    //TODO DELETE AFTER USE
    @Query(value = "SELECT * FROM User WHERE username=? ",nativeQuery = true)
    User findUser(String username);



}