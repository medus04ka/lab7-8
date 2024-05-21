package common.build.request;

import common.model.User;
import common.util.Commands;

public class ClearReq extends Request {
    public ClearReq(User user) {
        super(Commands.CLEAR, user);
    }
}