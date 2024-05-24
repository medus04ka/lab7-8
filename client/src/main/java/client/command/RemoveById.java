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
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 */
public class RemoveById extends Command {
    private final Cons console;
    private final UDP client;

    public RemoveById(Cons console, UDP client) {
        super("remove_by_id <ID>");
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
            if (arguments[ 1 ].isEmpty()) throw new WrongAmountOfElements();
            var id = Integer.parseInt(arguments[ 1 ]);

            var response = client.sendAndReceiveCommand(new RemoveByIdReq(id, SessionHandler.getCurrentUser()));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new API(response.getError());
            }

            if (response.getClass().equals(NotLoggedInRes.class)) {
                console.printError("Вы не залогинены, войдите");
            }
            if (response.getClass().equals(NoSuchCommandRes.class)) {
                console.printError("??? дурачок залогинься");
            }
            if (response.getClass().equals(getTargetClassCastOrErrorResponse(this.getClass()))) {
                console.println("Продукт успешно удален.");
                return true;
            }
        } catch (WrongAmountOfElements exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        } catch (NumberFormatException exception) {
            console.printError("ID должен быть представлен числом!");
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (API e) {
            console.printError(e.getMessage());
        }
        return false;
    }
}