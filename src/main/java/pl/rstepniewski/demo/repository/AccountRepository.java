package pl.rstepniewski.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pl.rstepniewski.demo.model.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    @Query("{ '_id': ?0 }")
    Optional<Account> findByIdWithLock(String accountId, LockModeType lockMode);
}
