package app.pages.Strategy;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.pages.*;
import app.user.SimpleUser;
import app.user.User;

import java.util.List;

public class HomePageStrategy implements PrintPageStrategy {

    @Override
    public String print(User currentUser) {
        List<Playlist> playlists = ((SimpleUser)currentUser).getTopLikedPlaylists();
        List<Song> songs = ((SimpleUser)currentUser).getTopLikedSongs();
        String pageLog = "Liked songs:\n\t[";

        System.out.println("size" + songs.size());
        for (int i = 0; i < songs.size(); i++) {
            pageLog += songs.get(i).getName();
            if (i != songs.size() - 1) {
                pageLog += ", ";
            }
        }

        pageLog += "]\n\nFollowed playlists:\n\t[";
        for (int i = 0; i < playlists.size(); i++) {
            pageLog += playlists.get(i).getName();
            if (i != playlists.size() - 1) {
                pageLog += ", ";
            }
        }
        pageLog += "]";
        return pageLog;
    }

}
