package common.build.response;

import common.model.User;
import common.util.Commands;

public class AuthRes extends Response {
    public final User user;

    public AuthRes(User user, String error) {
        super(Commands.AUTH, error);
        this.user = user;
    }
}
