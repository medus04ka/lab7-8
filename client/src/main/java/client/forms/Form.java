package client.forms;

import common.exceptions.IncorrectInputInScript;
import common.exceptions.InvalidForm;

/**
 * Абстрактный класс формы для ввода пользовательских данных.
 *
 * @param <T> создаваемый объект
 */
public abstract class Form<T> {
    public abstract T build() throws Exception;
}