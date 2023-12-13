package app.pages.Strategy;

import app.pages.Page;
import app.user.SimpleUser;
import app.user.User;

public interface PrintPageStrategy {
    public String print(User currentUser);
}
