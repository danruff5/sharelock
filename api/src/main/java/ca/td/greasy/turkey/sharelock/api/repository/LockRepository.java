package ca.td.greasy.turkey.sharelock.api.repository;

import ca.td.greasy.turkey.sharelock.api.model.Lock;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LockRepository extends CrudRepository<Lock, Long> {
    List<Lock> findByOwnerId(Long userId);
}
