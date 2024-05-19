package client.handlers;

import common.model.User;

public class SessionHandler {
    public static User currentUser = null;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        SessionHandler.currentUser = currentUser;
    }
}
