package fileio.input;

import app.audio.Files.Song;
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
    private FiltersInput filters; // pentru search
    private Integer itemNumber; // pentru select
    private Integer repeatMode; // pentru repeat
    private Integer playlistId; // pentru add/remove song
    private String playlistName; // pentru create playlist
    private Integer seed; // pentru shuffle
    private int age; // user
    private String city; // user
    private String name; // album
    private int releaseYear;
    private String description;
    private ArrayList<SongInput> songs;


    public CommandInput() {
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public void setFilters(FiltersInput filters) {
        this.filters = filters;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setRepeatMode(Integer repeatMode) {
        this.repeatMode = repeatMode;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    @Override
    public String toString() {
        return "CommandInput{" +
                "command='" + command + '\'' +
                ", username='" + username + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", filters=" + filters +
                ", itemNumber=" + itemNumber +
                ", repeatMode=" + repeatMode +
                ", playlistId=" + playlistId +
                ", playlistName='" + playlistName + '\'' +
                ", seed=" + seed +
                '}';
    }
}
