package ch.hftm.service;

import ch.hftm.model.Blog;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class BlogServiceTest {
    @Inject
    BlogService blogService;

    @Test
    void listingAndAddingBlogs() {
        // Arrange
        Blog blog = new Blog("Testing Blog", "This is my testing blog");
        int blogsBefore;
        List<Blog> blogs;

        // Act
        blogsBefore = blogService.getBlogs().size();
        blogService.addBlog(blog);
        blogs = blogService.getBlogs();

        // Assert
        assertEquals(blogsBefore + 1, blogs.size());
        assertEquals(blog, blogs.get(blogs.size() - 1));
    }
}