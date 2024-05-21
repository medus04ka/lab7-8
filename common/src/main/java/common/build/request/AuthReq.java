package common.build.request;

import common.model.HumanBeing;
import common.model.User;
import common.util.Commands;

public class AuthReq extends Request {

    public AuthReq(User user) {
        super(Commands.AUTH, user);
    }

    @Override
    public boolean isAuth() {
        return true;
    }
}

