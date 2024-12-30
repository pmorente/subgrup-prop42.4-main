package edu.upc.prop.clusterxx;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestUsers {

    private Users users;

    @Before
    public void setUp() throws Exception {
        // Initialize the Users instance with predefined data
        List<Object[]> predefinedUsers = new ArrayList<>();
        predefinedUsers.add(new Object[]{"user1", "/path/to/user1"});
        predefinedUsers.add(new Object[]{"user2", "/path/to/user2"});
        users = new Users(predefinedUsers);
    }

    @Test
    public void testLoadUsers() {
        // Verify the initial set of users is loaded correctly
        List<String> userList = users.usersAsString();
        assertTrue(userList.contains("user1"));
        assertTrue(userList.contains("user2"));
        assertEquals("The number of users should match the expected value", 2, userList.size());
    }

    @Test
    public void testCreateUser() {
        // Add a new user and verify it exists in the database
        String newUsername = "user3";
        String newFolderPath = "/path/to/user3";
        users.createUser(newUsername, newFolderPath);

        List<String> userList = users.usersAsString();
        assertTrue(userList.contains(newUsername));
        assertEquals("The number of users should match the expected value", 3, userList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserAlreadyExists() {
        // Attempt to create a user that already exists
        String existingUsername = "user1";
        String folderPath = "/path/to/user1";
        users.createUser(existingUsername, folderPath); // Should throw an exception
    }

    @Test
    public void testDeleteUser() {
        // Delete an existing user and verify it is removed
        String usernameToDelete = "user1";
        users.deleteUser(usernameToDelete);

        List<String> userList = users.usersAsString();
        assertFalse(userList.contains(usernameToDelete));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteUserNotExists() {
        // Attempt to delete a non-existing user
        String nonExistentUser = "user3";
        users.deleteUser(nonExistentUser); // Should throw an exception
    }

    @Test
    public void testLogInAndGetLoggedInUser() {
        // Log in as an existing user and verify the logged-in user
        String usernameToLogIn = "user2";
        users.logIn(usernameToLogIn);
        assertEquals("Logged-in user should match the username", usernameToLogIn, users.getLogInUser());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLogInUserNotExists() {
        // Attempt to log in as a non-existing user
        String nonExistentUser = "user3";
        users.logIn(nonExistentUser); // Should throw an exception
    }

    @Test
    public void testGetRouteLoggedIn() {
        // Log in as a user and verify their folder path
        String usernameToLogIn = "user2";
        users.logIn(usernameToLogIn);
        String folderPath = users.getRouteLoggedIn();
        assertEquals("The folder path should match the expected value", "/path/to/user2", folderPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRouteLoggedInNoUser() {
        // Attempt to get the folder path when no user is logged in
        users.getRouteLoggedIn(); // Should throw an exception
    }

    @Test
    public void testUsersAsString() {
        // Verify the list of usernames is correct
        List<String> userList = users.usersAsString();
        assertTrue(userList.contains("user1"));
        assertTrue(userList.contains("user2"));
        assertEquals("The number of users should match the expected value", 2, userList.size());
    }

    @Test
    public void testUserAndFolderPathAsString() {
        // Verify the list of usernames and folder paths is correct
        List<String> userAndFolderList = users.UserAndFolderPathAsString();
        assertTrue(userAndFolderList.contains("user1,/path/to/user1"));
        assertTrue(userAndFolderList.contains("user2,/path/to/user2"));
        assertEquals("The number of users and folder paths should match the expected value", 2, userAndFolderList.size());
    }
}
