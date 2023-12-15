package app.pages.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Event {
    private String name;
    private String description;
    private String date;

    public Event(final String name, final String description, final String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }
}
