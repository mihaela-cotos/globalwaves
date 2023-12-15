package fileio.input;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class PodcastInput {
    private String name;
    private String owner;
    private ArrayList<EpisodeInput> episodes;

    public PodcastInput() {
    }
    @Override
    public String toString() {
        return "PodcastInput{"
                + "name='" + name + '\''
                + ", owner='" + owner + '\''
                + ", episodes=" + episodes
                + '}';
    }
}
