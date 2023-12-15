package app.pages.utils;

import lombok.Getter;

@Getter
public final class Announcement {
    private String name;
    private String description;

    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
