package gr.hua.DistSysApp.ritoAPI.Repositories;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete





import gr.hua.DistSysApp.ritoAPI.Models.Entities.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Integer> {


}