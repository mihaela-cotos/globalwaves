package app.pages;

import app.audio.Files.Song;
import app.pages.Strategy.*;
import app.user.User;
import lombok.Getter;

@Getter
public abstract class Page {
    String name;
    ChangePageStrategy change;
    PrintPageStrategy print;
}
