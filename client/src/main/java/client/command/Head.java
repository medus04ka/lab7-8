package client.command;

import client.handlers.SessionHandler;
import client.netw.UDP;
import client.util.console.Cons;
import common.build.response.NotLoggedInRes;
import common.exceptions.*;
import common.build.request.*;
import common.build.response.*;

import java.io.IOException;

/**
 * Команда 'head'. Выводит первый элемент коллекции.
 */
public class Head extends Command {
    private final Cons console;
    private final UDP client;

    public Head(Cons console, UDP client) {
        super("head");
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
        try {
            if (!arguments[1].isEmpty()) throw new WrongAmountOfElements();

            var response = client.sendAndReceiveCommand(new HeadReq(SessionHandler.getCurrentUser()));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new API(response.getError());
            }

            if (response.getClass().equals(NotLoggedInRes.class)) {
                console.printError("Вы не залогинены, войдите");
            }
            if (response.getClass().equals(NoSuchCommandRes.class)) {
                console.printError("??? дурачок залогинься");

                if (response.getClass().equals(getTargetClassCastOrErrorResponse(this.getClass()))) {
                    if (((HeadRes) response).person == null) {
                        console.println("Коллекция пуста!");
                        return true;
                    }
                }
                console.println(((HeadRes) response).person);
                return true;
            }
        } catch (WrongAmountOfElements exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (API e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}