package app.audio.Collections;

import app.audio.Files.Episode;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class PodcastOutput {
    private final ArrayList<String> episodes;
    private final String name;

    public PodcastOutput(Podcast podcast) {
        this.name = podcast.getName();
        this.episodes = new ArrayList<>();
        for (int i = 0; i < podcast.getEpisodes().size(); i++) {
            episodes.add(podcast.getEpisodes().get(i).getName());
        }
    }
}
