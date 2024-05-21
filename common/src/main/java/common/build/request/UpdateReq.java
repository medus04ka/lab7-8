package common.build.request;

import common.model.HumanBeing;
import common.model.User;
import common.util.Commands;

public class UpdateReq extends Request {
    public final int id;
    public final HumanBeing updatedPerson;

    public UpdateReq(int id, HumanBeing updatedPerson, User user) {
        super(Commands.UPDATE, user);
        this.id = id;
        updatedPerson.setId(id);
        this.updatedPerson = updatedPerson;
    }
}
