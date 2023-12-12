package app.pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.SimpleUser;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LikedContentPage extends Page {
    private List<Song> songs;
    private List<Playlist> playlists;

    public LikedContentPage(SimpleUser currentUser) {
        this.name = "likedPage";
        playlists = currentUser.getFollowedPlaylists();
        songs = currentUser.getLikedSongs();
    }

//    public void update(SimpleUser currentUser) {
//        // poate sa dea null
//        playlists = currentUser.getFollowedPlaylists();
//        songs = currentUser.getLikedSongs();
//    }
}
