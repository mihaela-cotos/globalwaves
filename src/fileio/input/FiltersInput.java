package fileio.input;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class FiltersInput {
    private String name;
    private Integer duration;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear; // search song/episode -> releaseYear
    private String artist;
    private String owner; // search playlist & podcast
    private String followers; // search playlist -> followers
    private String description; // search album -> description

    public FiltersInput() {
    }

    @Override
    public String toString() {
        return "SongInput{"
                + "name='" + name + '\''
                + ", duration=" + duration
                + ", album='" + album + '\''
                + ", tags=" + tags
                + ", lyrics='" + lyrics + '\''
                + ", genre='" + genre + '\''
                + ", releaseYear='" + releaseYear + '\''
                + ", artist='" + artist + '\''
                + '}';
    }
}
