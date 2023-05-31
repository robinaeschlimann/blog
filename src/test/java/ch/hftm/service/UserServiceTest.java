package ch.hftm.service;

import ch.hftm.model.Blog;
import ch.hftm.model.User;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserServiceTest
{
    @Inject
    UserService service;

    @Test
    void listingAndAddingUsers()
    {
        // Arrange
        User user = User.builder().firstname("Robin").lastname("Aeschlimann").username("robinaeschlimann").build();
        int usersBefore;
        List<User> users;

        // Act
        usersBefore = service.getUsers().size();
        service.addUser(user);
        users = service.getUsers();

        // Assert
        assertEquals(usersBefore + 1, users.size());
        assertEquals(user, users.get(users.size() - 1));
    }
}