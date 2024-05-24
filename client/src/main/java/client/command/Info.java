package client.command;

import client.handlers.SessionHandler;
import client.netw.UDP;
import client.util.console.Cons;
import common.build.request.InfoReq;
import common.build.response.InfoRes;
import common.build.response.NoSuchCommandRes;
import common.build.response.NotLoggedInRes;

import java.io.IOException;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Info extends Command {
    private final Cons console;
    private final UDP client;

    public Info(Cons console, UDP client) {
        super("info");
        this.console = console;
        this.client = client;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        try {
            var response = client.sendAndReceiveCommand(new InfoReq(SessionHandler.getCurrentUser()));

            if (response.getClass().equals(NotLoggedInRes.class)) {
                console.printError("Вы не залогинены, войдите");
            }
            if (response.getClass().equals(NoSuchCommandRes.class)) {
                console.printError("??? дурачок залогинься");
            }

            if (response.getClass().equals(getTargetClassCastOrErrorResponse(this.getClass()))) {
                assert response instanceof InfoRes;
                console.println(((InfoRes) response).infoMessage);
            }
            return true;
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
    @Override
    public boolean isNeedAuth() {
        return false;
    }
}