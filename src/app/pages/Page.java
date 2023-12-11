package app.pages;

import app.audio.Files.Song;
import app.pages.Strategy.*;

public abstract class Page {
    ChangePageStrategy change;
    PrintPageStrategy print;
}
