package client.command;

import client.handlers.SessionHandler;
import client.netw.UDP;
import client.util.console.Cons;
import common.build.request.HelpReq;
import common.build.response.HelpRes;
import common.util.Commands;

import java.io.IOException;

public class Help extends Command {
    private final Cons console;
    private final UDP client;

    public Help(Cons console, UDP client) {
        super("help");
        this.console = console;
        this.client = client;
    }

    @Override
    public boolean apply(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        try {
            var response = (HelpRes) client.sendAndReceiveCommand(new HelpReq(SessionHandler.getCurrentUser()));
            console.print(response.helpMessage);
            return true;
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        }
        return false;
    }
}