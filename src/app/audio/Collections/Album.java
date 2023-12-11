package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Album extends AudioCollection {
    private int timestamp;
    private final String description;
    private final Integer releaseYear;
    private final ArrayList<Song> songs;

    public Album(String name, String owner, ArrayList<Song> songs, int timestamp,
                                            String description, Integer releaseYear) {
        super(name, owner);
        this.songs = songs;
        this.timestamp = timestamp;
        this.description = description;
        this.releaseYear = releaseYear;
    }


    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(int index) {
        return songs.get(index);
    }

    public boolean containsSong(Song song) {
        int count = 0;
        for (Song iterSong : songs) {
            if (iterSong.getName().equals(song.getName())) {
                count++;
            }
        }
        return count > 1;
    }
}
