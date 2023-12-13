package app.user;

import app.utils.Enums;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class User {
    private String username;
    private int age;
    private String city;
    private Enums.UserType userType;
    private Enums.PageType pageName;
    public User(String username, int age, String city) {
        this.username = username;
        this.age = age;
        this.city = city;
    }

    public User() {
    }

    public boolean matchesName(String name) {
        return getUsername().toLowerCase().startsWith(name.toLowerCase());
    }
}
