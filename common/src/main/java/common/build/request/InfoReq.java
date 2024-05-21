package common.build.request;

import common.model.User;
import common.util.Commands;

public class InfoReq extends Request {

    public InfoReq(User user) {
        super(Commands.INFO, user);
    }
}
