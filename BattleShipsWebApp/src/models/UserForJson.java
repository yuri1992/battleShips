package models;

import engine.model.multi.User;

/**
 * Created by amirshavit on 10/17/17.
 */
public class UserForJson {

    private int id;
    private String name;
    private String email;

    public UserForJson(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


}
