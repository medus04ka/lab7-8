package common.build.request;

import common.model.User;
import common.util.Commands;

/**
 * The type Info req.
 */
public class InfoReq extends Request {
    /**
     * Instantiates a new Info req.
     */
    public InfoReq(User user) {
        super(Commands.INFO, user);
    }
}
