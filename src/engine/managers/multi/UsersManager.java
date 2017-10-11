package engine.managers.multi;

import engine.exceptions.UserNameTakenException;
import engine.exceptions.UserNotFoundException;
import engine.model.multi.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by amirshavit on 10/11/17.
 */
public class UsersManager {

    private final Set<User> activeUsers;
    private static int userIndex = 0;

    public UsersManager() {
        this.activeUsers = new HashSet<>();
    }

    public void addUser(String username) throws UserNameTakenException {
        if (isUserNameTaken(username))
            throw new UserNameTakenException(username);

        activeUsers.add(new User(userIndex, username));
        userIndex++;
    }

    public void removeUser(int userId) throws UserNotFoundException {
        User u = getUserById(userId);
        activeUsers.remove(u);
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(activeUsers);
    }

    private User getUserById(int userId) throws UserNotFoundException {
        List<User> list = getUsers().stream().filter(u -> u.getId() == userId).collect(Collectors.toList());
        if (list.size() == 0)
            throw new UserNotFoundException(userId);
        return list.get(0);
    }

    private boolean isUserNameTaken(String username) {
        List<User> list = getUsers().stream().filter(u -> u.getName().equals(username)).collect(Collectors.toList());
        return list.size() > 0;
    }

}
