package app.pages.Strategy;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.SimpleUser;
import app.user.User;

import java.util.List;

public final class LikedContentPageStrategy implements PrintPageStrategy {

    @Override
    public String print(final User currentUser) {
        List<Song> songs = ((SimpleUser) currentUser).getLikedSongs();
        List<Playlist> playlists = ((SimpleUser) currentUser).getFollowedPlaylists();
        String pageLog = "Liked songs:\n\t[";
        for (int i = 0; i < songs.size(); i++) {
            pageLog += songs.get(i).getName();
            pageLog += " - ";
            pageLog += songs.get(i).getArtist();
            if (i != songs.size() - 1) {
                pageLog += ", ";
            }
        }

        pageLog += "]\n\nFollowed playlists:\n\t[";
        for (int i = 0; i < playlists.size(); i++) {
            pageLog += playlists.get(i).getName();
            pageLog += " - ";
            pageLog += playlists.get(i).getOwner();
            if (i != playlists.size() - 1) {
                pageLog += ", ";
            }
        }
        pageLog += "]";
        return pageLog;
    }
}
