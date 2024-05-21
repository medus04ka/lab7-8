package common.build.request;

import common.model.User;
import common.util.Commands;

public class ShowReq extends Request {

    public ShowReq(User user) {
        super(Commands.SHOW, user);
    }
}