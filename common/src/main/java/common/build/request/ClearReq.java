package common.build.request;

import common.model.User;
import common.util.Commands;

/**
 * The type Clear req.
 */
public class ClearReq extends Request {
    /**
     * Instantiates a new Clear req.
     */
    public ClearReq(User user) {
        super(Commands.CLEAR, user);
    }
}