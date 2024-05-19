package common.build.request;

import common.model.User;
import common.util.Commands;

/**
 * The type Head req.
 */
public class HeadReq extends Request {
    /**
     * Instantiates a new Head req.
     */
    public HeadReq(User user) {
        super(Commands.HEAD, user);
    }
}