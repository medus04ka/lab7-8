package client.command;

import client.handlers.SessionHandler;
import client.netw.UDP;
import client.util.console.Cons;
import common.build.request.SumOfImpactSpeedReq;
import common.build.response.NoSuchCommandRes;
import common.build.response.NotLoggedInRes;
import common.build.response.Response;
import common.build.response.SumOfImpactSpeedRes;
import common.exceptions.*;

import java.io.IOException;

/**
 * Команда 'sum_of_impact_speed'. Сумма скорости удара.
 */
public class SumOfImpactSpeed extends Command {
    private final Cons console;
    private final UDP client;
    private Response response;

    public SumOfImpactSpeed(Cons console, UDP client) {
        super("sum_of_impactSpeed");
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
            var response = client.sendAndReceiveCommand(new SumOfImpactSpeedReq(SessionHandler.getCurrentUser()));
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
                console.println("Сумма скорости: " + ((SumOfImpactSpeedRes) response).sum);
                return true;
            }

        } catch (WrongAmountOfElements exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        } catch (API e) {
            console.printError(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}