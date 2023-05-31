package ch.hftm.repository;

import ch.hftm.model.Blog;
import ch.hftm.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository  implements PanacheRepository<User> {
    public UserRepository(){}
}
