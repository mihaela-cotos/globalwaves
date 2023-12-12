package app.user;

import app.audio.Collections.Podcast;
import app.pages.HostPage;
import app.pages.utils.Announcement;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Host extends User {
    private final ArrayList<Podcast> podcasts;
    private final ArrayList<Announcement> announcements;
    private HostPage hostPage;
    public Host(String username, int age, String city) {
        super(username, age, city);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
        hostPage = new HostPage(this);
        setUserType(Enums.UserType.HOST);
    }
}
