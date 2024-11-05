package pl.rstepniewski.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.rstepniewski.demo.model.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
}
