package app.pages.Strategy;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.pages.HomePage;
import app.pages.LikedContentPage;
import app.pages.Page;

import java.util.List;

public class LikedContentPageStrategy implements PrintPageStrategy {

    @Override
    public String print(Page page) {
        List<Song> songs = ((LikedContentPage)page).getSongs();
        List<Playlist> playlists = ((LikedContentPage)page).getPlaylists();
        String pageLog = "Liked Songs:\n\t[";
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
