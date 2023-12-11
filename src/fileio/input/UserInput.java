package fileio.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class UserInput {
    private String username;
    private int age;
    private String city;

    public UserInput() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "UserInput{" +
                "username='" + username + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                '}';
    }
}
