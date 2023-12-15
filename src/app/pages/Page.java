package app.pages;

import app.pages.Strategy.ArtistPageStrategy;
import app.pages.Strategy.HomePageStrategy;
import app.pages.Strategy.LikedContentPageStrategy;
import app.pages.Strategy.HostPageStrategy;
import app.pages.Strategy.PrintPageStrategy;
import app.utils.Enums;
import lombok.Getter;

@Getter
public final class Page {
    private Enums.PageType pageType;

    public Page(final Enums.PageType page) {
        this.pageType = page;
    }

    /**
     * Choose the right strategy for print.
     * @return print page strategy
     */
    public PrintPageStrategy getPrintMethod() {
        PrintPageStrategy printStrategy;

        switch (this.pageType) {
            case HOME:
                printStrategy = new HomePageStrategy();
                break;
            case LIKED:
                printStrategy = new LikedContentPageStrategy();
                break;
            case ARTIST:
                printStrategy = new ArtistPageStrategy();
                break;
            case HOST:
                printStrategy = new HostPageStrategy();
                break;
            default:
                printStrategy = null;
        }

        return printStrategy;
    }
}
