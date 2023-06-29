package ch.hftm.service;

import java.util.List;
import java.util.stream.Collectors;

import ch.hftm.model.Blog;
import ch.hftm.repository.BlogRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class BlogService {
    @Inject
    BlogRepository blogRepository;

    @Inject
    Logger logger;

    public List<Blog> getBlogs() {
        var blogs = blogRepository.listAll();
        logger.info("Returning " + blogs.size() + " blogs");
        return blogs;
    }

    @Transactional
    public void addBlog(Blog blog) {
        logger.info("Adding blog " + blog.getTitle());
        blogRepository.persist(blog);
    }

    @Transactional
    public void addBlogs( List<Blog> blogs ) {
        logger.info( "Adding blogs: " + blogs.stream()
                .map( Blog::getTitle )
                .collect(Collectors.joining(", ") ) );

        blogRepository.persist( blogs.stream() );
    }

    @Transactional
    public void updateBlog( Blog blog )
    {
        logger.info( "Updating blog with id: " + blog.getId() );
        var dbBlog = blogRepository.findById( blog.getId() );

        dbBlog.setTitle( blog.getTitle() );
        dbBlog.setDescription( blog.getDescription() );
        blogRepository.persist( dbBlog );
    }

    public List<Blog> findBlogs( String searchString )
    {
        logger.info( "Searching for blogs with search string: " + searchString );

        return List.of();
    }
}