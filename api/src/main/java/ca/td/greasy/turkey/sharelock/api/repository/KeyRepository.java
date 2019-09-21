package ca.td.greasy.turkey.sharelock.api.repository;

import ca.td.greasy.turkey.sharelock.api.model.Key;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface KeyRepository extends CrudRepository<Key, Long> {
    List<Key> getKeysByUserId(Long userId);
    
    List<Key> getKeysByLockId(Long lockId);
}
