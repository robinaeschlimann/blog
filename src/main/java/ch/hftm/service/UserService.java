package ch.hftm.service;

import ch.hftm.model.User;
import ch.hftm.repository.BlogRepository;
import ch.hftm.repository.UserRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@Dependent
public class UserService
{
    @Inject
    UserRepository repository;

    @Inject
    Logger logger;

    public List<User> getUsers()
    {
        logger.info("Getting all users");
        return repository.listAll();
    }

    @Transactional
    public void addUser( User user )
    {
        logger.info("Adding user with username=" + user.getUsername());
        repository.persist(user);
    }
}
