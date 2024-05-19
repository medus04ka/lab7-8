package common.build.request;

import common.model.User;
import common.util.Commands;

/**
 * The type Sum of impact speed req.
 */
public class SumOfImpactSpeedReq extends Request{
    /**
     * Instantiates a new Sum of impact speed req.
     */
    public SumOfImpactSpeedReq(User user) {
            super(Commands.SUM_OF_IMPACT_SPEED, user);
        }
    }
