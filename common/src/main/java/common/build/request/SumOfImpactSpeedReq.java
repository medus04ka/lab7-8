package common.build.request;

import common.model.User;
import common.util.Commands;

public class SumOfImpactSpeedReq extends Request {
    public SumOfImpactSpeedReq(User user) {
        super(Commands.SUM_OF_IMPACT_SPEED, user);
    }
}
