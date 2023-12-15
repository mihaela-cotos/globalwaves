package app.pages.Strategy;

import app.user.User;

/**
 * The PrintStrategy Interface.
 */
public interface PrintPageStrategy {
    /**
     * The method every page uses to execute a specific strategy.
     * @param currentUser current user
     * @return output message
     */
    String print(User currentUser);
}
