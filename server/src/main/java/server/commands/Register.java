package server.commands;

import common.build.request.RegisterReq;
import common.build.request.Request;
import common.build.response.RegisterRes;
import common.build.response.Response;
import org.postgresql.util.PSQLException;
import server.managers.AuthManager;
import server.repo.auth.exceptions.UserNotExistException;

public class Register extends Command {
    private final AuthManager authManager;
    private final int MAX_USERNAME_LENGTH = 40;

    public Register(AuthManager authManager) {
        super("register", "зарегистрировать юзерочка");
        this.authManager = authManager;
    }

    @Override
    public Response apply(Request request) {
        var req = (RegisterReq) request;
        var user = req.getUser();
        if (user.getName().length() >= MAX_USERNAME_LENGTH) {
            return new RegisterRes(user, "Длина имени юзера должна быть < " + MAX_USERNAME_LENGTH);
        }

        try {
            var newUserId = authManager.registerUser(user.getName(), user.getPassword());

            if (newUserId <= 0) {
                return new RegisterRes(user, "Не удалось создать юзерка.");
            } else {
                return new RegisterRes(user.copy(newUserId), null);
            }
        } catch (PSQLException e) {
            var message = "Ошибка PostgreSQL: " + e.getMessage();
            if (e.getMessage().contains("duplicate key value violates unique constraint \"users_name_key\"")) {
                message = "Неуникальное имя юзерка! Попробуйте другое.";
            }
            return new RegisterRes(user, message);
        } catch (Exception e) {
            return new RegisterRes(user, e.toString());
        }
    }
}
