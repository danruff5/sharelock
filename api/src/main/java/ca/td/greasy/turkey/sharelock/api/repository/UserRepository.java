package ca.td.greasy.turkey.sharelock.api.repository;

import ca.td.greasy.turkey.sharelock.api.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
}
