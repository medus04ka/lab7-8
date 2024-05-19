package common.build.request;

import common.model.User;
import common.util.Commands;

/**
 * The type Show req.
 */
public class ShowReq extends Request {
    /**
     * Instantiates a new Show req.
     */
    public ShowReq(User user) {
        super(Commands.SHOW, user);
    }
}