package common.build.request;

import common.model.User;
import common.util.Commands;

public class RegisterReq extends Request {

    public RegisterReq(User user) {
        super(Commands.REGISTER, user);
    }

    @Override
    public boolean isAuth() {
        return true;
    }
}
