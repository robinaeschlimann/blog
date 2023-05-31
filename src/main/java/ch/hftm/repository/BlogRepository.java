package ch.hftm.repository;

import java.util.ArrayList;
import java.util.List;

import ch.hftm.model.Blog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogRepository implements PanacheRepository<Blog> {

    public BlogRepository() {
    }

}
