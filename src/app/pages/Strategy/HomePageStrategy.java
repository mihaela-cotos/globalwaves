package app.pages.Strategy;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.pages.*;

public class HomePageStrategy implements PrintPageStrategy {

    @Override
    public String print(Page page) {
        String pageLog = "Liked songs:\n\t[";
        for (Song song : ((HomePage)page).getSongs()) {
            pageLog += song.getName();
            pageLog += ", ";
        }

        pageLog = pageLog.substring(0, pageLog.length()-2);

        pageLog = pageLog.concat("]\\n\\nFollowed playlists:\\n\\t[");
        for (Playlist playlist : ((HomePage)page).getPlaylists()) {
            pageLog += playlist.getName();
            pageLog += ", ";
        }
        pageLog = pageLog.substring(0, pageLog.length()-2);
        pageLog += "]";
        return pageLog;
    }

}
