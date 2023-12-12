package app.pages.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Event {
    private String name;
    private String description;
    private String date;

    public Event(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }
}
