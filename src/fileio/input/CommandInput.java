package fileio.input;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class CommandInput {
    private String command;
    private String username;
    private Integer timestamp;
    private String type; // song / playlist / podcast
    private FiltersInput filters; // for search
    private Integer itemNumber; // for select
    private Integer repeatMode; // for repeat
    private Integer playlistId; // for add/remove song
    private String playlistName; // for create playlist
    private Integer seed; // for shuffle
    private int age; // user
    private String city; // user
    private String name; // album
    private int releaseYear;
    private String description;
    private ArrayList<SongInput> songs;
    private ArrayList<EpisodeInput> episodes;
    private String date;
    private int price;
    private String nextPage;


    public CommandInput() {
    }

    @Override
    public String toString() {
        return "CommandInput{"
                + "command='" + command + '\''
                + ", username='" + username + '\''
                + ", timestamp=" + timestamp
                + ", type='" + type + '\''
                + ", filters=" + filters
                + ", itemNumber=" + itemNumber
                + ", repeatMode=" + repeatMode
                + ", playlistId=" + playlistId
                + ", playlistName='" + playlistName + '\''
                + ", seed=" + seed
                + '}';
    }
}
