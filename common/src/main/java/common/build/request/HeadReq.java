package common.build.request;

import common.model.User;
import common.util.Commands;

public class HeadReq extends Request {

    public HeadReq(User user) {
        super(Commands.HEAD, user);
    }
}