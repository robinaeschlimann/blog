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

    public void searchBlogForTitle( String searchText ) {
        logger.info( "Searching blogs with title " + searchText );

        blogRepository.find( "SELECT b FROM blog b WHERE title LIKE " );
    }
}