package server.commands;

import common.build.response.ClearRes;
import common.build.request.Request;
import common.build.response.Response;
import server.repo.HumanBeingNativeBasedRepository;
import server.service.HumanBeingService;

/**
 * Команда 'clear'. Очищает коллекцию.
 *
 */
public class Clear extends Command {
    private final HumanBeingService service;

    /**
     * Instantiates a new Clear.
     *
     * @param humanBeingNativeBasedRepository the human being repository
     */
    public Clear(HumanBeingService service) {
        super("clear", "очистить коллекцию");
        this.service = service;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            service.clear(request.getUser());
            return new ClearRes(null);
        } catch (Exception e) {
            return new ClearRes(e.toString());
        }
    }
}