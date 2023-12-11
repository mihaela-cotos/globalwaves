package app.user;

import app.pages.Page;
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
    private Page currentPage;

    public User(String username, int age, String city) {
        this.username = username;
        this.age = age;
        this.city = city;
    }

    public User() {
    }
}
