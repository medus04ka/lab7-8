package server.commands;

import common.build.request.*;
import common.build.response.*;
import server.repo.HumanBeingNativeBasedRepository;
import server.service.HumanBeingService;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции.
 *
 */
public class RemoveById extends Command {
    private final HumanBeingService service;

    /**
     * Instantiates a new Remove by id.
     *
     */
    public RemoveById(HumanBeingService service) {
        super("remove_by_id <ID>", "удалить элемент из коллекции по ID");
        this.service = service;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (RemoveByIdReq) request;

        try {
            if (!service.isExist(req.id)) {
                return new RemoveByIdRes("Продукта с таким ID в коллекции нет!");
            }

            service.removeById(req.getUser(), req.id);
            return new RemoveByIdRes(null);
        } catch (Exception e) {
            return new RemoveByIdRes(e.toString());
        }
    }
}