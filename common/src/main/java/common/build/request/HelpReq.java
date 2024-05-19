package common.build.request;

import common.model.User;
import common.util.Commands;

/**
 * The type Help req.
 */
public class HelpReq extends Request {
    /**
     * Instantiates a new Help req.
     */
    public HelpReq(User user) {
        super(Commands.HELP, user);
    }
}