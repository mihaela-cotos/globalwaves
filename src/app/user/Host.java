package app.user;

import app.audio.Collections.Podcast;
import app.pages.utils.Announcement;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Host extends User {
    private final ArrayList<Podcast> podcasts;
    private final ArrayList<Announcement> announcements;
    public Host(String username, int age, String city) {
        super(username, age, city);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
        setUserType(Enums.UserType.HOST);
        setPageName(Enums.PageType.HOST);
    }
}
