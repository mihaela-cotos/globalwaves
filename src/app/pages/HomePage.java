package app.pages;

import app.Admin;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.SimpleUser;
import app.user.User;
import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class HomePage extends Page {
    private List<Song> songs;
    private List<Playlist> playlists;

    public HomePage(SimpleUser currentUser) {
        this.name = "homePage";
        //update(currentUser);
        playlists = currentUser.getTopLikedPlaylists();
        songs = currentUser.getTopLikedSongs();
    }

    public void update(final SimpleUser currentUser) {
        // poate sa dea null
        playlists = currentUser.getTopLikedPlaylists();
        songs = currentUser.getTopLikedSongs();
    }
}
