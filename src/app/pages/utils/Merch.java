package app.pages.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Merch {
    private String name;
    private String description;
    private int price;

    public Merch(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
