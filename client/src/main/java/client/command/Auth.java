package client.command;

import client.forms.AuthenthicateForm;
import client.handlers.SessionHandler;
import client.netw.UDP;
import client.util.console.Cons;
import common.build.request.AuthReq;
import common.build.response.AuthRes;
import common.exceptions.API;
import common.exceptions.InvalidForm;
import common.exceptions.WrongAmountOfElements;

import java.io.IOException;

public class Auth extends Command {
    private final Cons console;
    private final UDP client;

    public Auth(Cons console, UDP client) {
        super("auth");
        this.console = console;
        this.client = client;
    }

    @Override
    public boolean apply(String[] arguments) {
        try {
            if (!arguments[1].isEmpty()) throw new Exception();
            console.println("Залогинивание пользователя:");

            var user = (new AuthenthicateForm(console)).build();

            var response = (AuthRes) client.sendAndReceiveCommand(new AuthReq(user));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new API(response.getError());
            }

            SessionHandler.setCurrentUser(response.user);
            console.println("Вы вошла " + response.user.getName() +
                    " с id=" + response.user.getId() + " !");
            return true;

        } catch (WrongAmountOfElements exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        } catch (InvalidForm exception) {
            console.printError("Введенные данные не валидны! Пользователь не вхожден");
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (API e) {
            console.printError(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean isNeedAuth() {
        return false;
    }


}
