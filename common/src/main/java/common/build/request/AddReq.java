package common.build.request;

import common.model.HumanBeing;
import common.model.User;
import common.util.Commands;

public class AddReq extends Request {

    public final HumanBeing person;

    public AddReq(HumanBeing humanBeing, User user) {
        super(Commands.ADD, user);
        this.person = humanBeing;
    }
}