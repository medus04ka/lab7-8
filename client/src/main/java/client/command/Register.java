package client.command;

import client.forms.RegisterForm;
import client.handlers.SessionHandler;
import client.netw.UDP;
import client.util.console.Cons;
import common.build.request.RegisterReq;
import common.build.response.RegisterRes;
import common.exceptions.API;
import common.exceptions.InvalidForm;
import common.exceptions.WrongAmountOfElements;

import java.io.Console;
import java.io.IOException;

public class Register extends Command {

    private final Cons console;
    private final UDP client;
    public Register(Cons console, UDP client) {
        super("register");
        this.console = console;
        this.client = client;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        try {
            if (!arguments[1].isEmpty()) throw new Exception();
            console.println("Создание пользователя:");

            var user = (new RegisterForm(console)).build();

            var response = (RegisterRes) client.sendAndReceiveCommand(new RegisterReq(user));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new API(response.getError());
            }

            SessionHandler.setCurrentUser(response.user);
            console.println("Пользователь " + response.user.getName() +
                    " с id=" + response.user.getId() + " успешно создан!");
            return true;

        } catch (WrongAmountOfElements exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        } catch (InvalidForm exception) {
            console.printError("Введенные данные не валидны! Пользователь на зарегистрирован");
        } catch(IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (API e) {
            console.printError(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}
