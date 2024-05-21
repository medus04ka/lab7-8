package server.commands;

import common.build.request.Request;
import common.build.response.InfoRes;
import common.build.response.Response;
import server.service.HumanBeingService;


/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Info extends Command {

    private final HumanBeingService service;

    public Info(HumanBeingService service) {
        super("info", "вывести информацию о коллекции");
        this.service = service;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var lastInitTime = service.getLastInitTime();
        var lastInitTimeString = (lastInitTime == null) ? "в данной сессии инициализации еще не происходило" :
                lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

        var lastSaveTime = service.getLastSaveTime();
        var lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии сохранения еще не происходило" :
                lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

        var message = "Сведения о коллекции:\n" +
                " Тип: " + service.getTypeOfCollection() + "\n" +
                " Количество элементов: " + service.get().size() + "\n";
        return new InfoRes(message, null);
    }
}