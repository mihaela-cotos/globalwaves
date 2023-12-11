package app.pages;

import app.Admin;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.SimpleUser;
import app.user.User;
import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HomePage extends Page {
    private List<Song> songs;
    private List<Playlist> playlists;

    public void updateHomePage(CommandInput commandInput) {
        SimpleUser currentUser = (SimpleUser) Admin.getUser(commandInput.getUsername());
        // poate sa dea null
        playlists = currentUser.getTopLikedPlaylists();
        songs = currentUser.getTopLikedSongs();
    }
}
