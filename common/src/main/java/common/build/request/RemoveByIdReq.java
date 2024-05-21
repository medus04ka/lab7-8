package common.build.request;

import common.model.User;
import common.util.Commands;

public class RemoveByIdReq extends Request {
    public final int id;

    public RemoveByIdReq(int id, User user) {
        super(Commands.REMOVE_BY_ID, user);
        this.id = id;
    }
}
