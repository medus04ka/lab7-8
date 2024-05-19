package common.build.request;

import common.model.HumanBeing;
import common.model.User;
import common.util.Commands;

/**
 * The type Add req.
 */
public class AddReq extends Request {
    /**
     * The Person.
     */
    public final HumanBeing person;

    /**
     * Instantiates a new Add req.
     *
     * @param humanBeing the human being
     */
    public AddReq(HumanBeing humanBeing, User user) {
         super(Commands.ADD, user);
         this.person = humanBeing;
        }
    }

