package ch.hftm.service;

import ch.hftm.control.dto.BlogDto;
import ch.hftm.model.User;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class BlogServiceTest {
    @Inject
    BlogService blogService;

    @Inject
    UserService userService;

    @Test
    void listingAndAddingBlogs() {
        // Arrange
        User user = User.builder().firstname("Robin").lastname("Aeschlimann").username("robinaeschlimann").build();
        userService.addUser(user);
        BlogDto blog = BlogDto.builder()
                .title("Testing Blog")
                .description("This is my testing blog")
                //.user(user)
                .build();
        int blogsBefore;
        List<BlogDto> blogs;

        // Act
        blogsBefore = blogService.getBlogs().size();
        blogService.addBlog(blog);
        blogs = blogService.getBlogs();

        // Assert
        assertEquals(blogsBefore + 1, blogs.size());
        //assertNotNull( blogs.get(blogs.size()-1).getUser() );
        assertEquals(blog, blogs.get(blogs.size() - 1));
    }
}