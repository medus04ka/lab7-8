package client.command;

import client.forms.HumanBeingForm;
import client.handlers.SessionHandler;
import client.netw.UDP;
import client.util.console.Cons;
import common.build.request.UpdateReq;
import common.build.response.NoSuchCommandRes;
import common.build.response.NotLoggedInRes;
import common.exceptions.*;

import java.io.IOException;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 */
public class Update extends Command {
    private final Cons console;
    private final UDP client;

    public Update(Cons console, UDP client) {
        super("update ID {element}");
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
            if (arguments[1].isEmpty()) throw new WrongAmountOfElements();

            var id = Integer.parseInt(arguments[1]);

            console.println("* Введите данные обновленного продукта:");
            var updatedPerson = (new HumanBeingForm(console)).build();

            var response = client.sendAndReceiveCommand(new UpdateReq(id, updatedPerson, SessionHandler.getCurrentUser()));
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
                console.println("Продукт успешно обновлен.");
                return true;
            }

        } catch (WrongAmountOfElements exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        } catch (InvalidForm exception) {
            console.printError("Поля продукта не валидны! Продукт не создан!");
        } catch (NumberFormatException exception) {
            console.printError("ID должен быть представлен числом!");
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (API e) {
            console.printError(e.getMessage());
        } catch (IncorrectInputInScript ignored) {
        }
        return false;
    }
}