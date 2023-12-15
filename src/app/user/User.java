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
    public User(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
    }

    public User() {
    }

    /**
     * Matches name boolean.
     *
     * @param name user's name
     * @return the boolean
     */
    public boolean matchesName(final String name) {
        return getUsername().toLowerCase().startsWith(name.toLowerCase());
    }
}
