package gr.hua.DistSysApp.ritoAPI.Repositories;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete





import gr.hua.DistSysApp.ritoAPI.Models.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}