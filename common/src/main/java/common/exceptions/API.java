package common.exceptions;

/**
 * Выбрасывается, если в ответе сервера ошибка
 */
public class API extends Exception {
    public API(String message) {
        super(message);
    }
}