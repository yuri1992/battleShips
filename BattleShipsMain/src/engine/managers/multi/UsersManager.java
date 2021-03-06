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

    private static UsersManager instance;
    private final Set<User> activeUsers;
    private static int userIndex = 0;

    private UsersManager() {
        this.activeUsers = new HashSet<>();
    }

    public static UsersManager sharedInstance() {
        if (instance == null)
            instance = new UsersManager();
        return instance;
    }

    public User addUser(String username, String email, String password) throws UserNameTakenException {
        if (isUserNameTaken(username))
            throw new UserNameTakenException(username);

        User res = new User(userIndex, username, email, password);
        activeUsers.add(res);
        userIndex++;
        return res;
    }

    public void removeUser(int userId) throws UserNotFoundException {
        User u = getUser(userId);
        activeUsers.remove(u);
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(activeUsers);
    }

    public User getUser(int userId) throws UserNotFoundException {
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
