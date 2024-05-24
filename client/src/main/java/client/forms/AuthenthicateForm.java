package client.forms;

import client.util.Interrogator;
import client.util.console.Cons;
import common.model.User;

import java.util.NoSuchElementException;

public class AuthenthicateForm extends Form<User> {
    private final Cons console;

    public AuthenthicateForm(Cons console) {
        this.console = console;
    }

    @Override
    public User build() throws Exception {
        var user = new User(askName(), askPassword());
        if (!user.validate()) throw new Exception();
        return user;
    }

    /**
     * Запрашивает имя пользователя.
     *
     * @return Имя пользователя.
     */
    protected String askName() throws Exception {
        String name = "";
        var fileMode = Interrogator.fileMode();
        while (true) {
            try {
                console.println("Введите имя пользователя:");
                console.ps2();

                name = Interrogator.getUserScanner().nextLine().trim();
                if (fileMode) console.println(name);

                if (name.equals("") || name.length() >= 40) throw new Exception();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Имя пользователя не распознано!");
                if (fileMode) throw new Exception();
                break;
            } catch (Exception exception) {
                console.printError("Размер имени должен быть от 1 до 40 символов!");
                if (fileMode) throw new Exception();
                break;
            }
//            catch (IllegalStateException exception) {
//                console.printError("Непредвиденная ошибка!");
//                System.exit(0);
//            }
        }

        return name;
    }


    /**
     * Запрашивает пароль пользователя.
     *
     * @return Пароль пользователя.
     */
    protected String askPassword() throws Exception {
        String password = "";
        var fileMode = Interrogator.fileMode();
        while (true) {
            try {
                console.println("Введите пароль пользователя:");
                console.ps2();

                password = readPassword();
                if (fileMode) console.println(password);

                if (password.equals("")) throw new Exception();
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Пароль пользователя не распознан!");
                if (fileMode) throw new Exception();
                break;
            } catch (Exception exception) {
                console.printError("Пароль не должен быть пустым!");
                if (fileMode) throw new Exception();
                break;

            }
//            catch (IllegalStateException exception) {
//                console.printError("Непредвиденная ошибка!");
//                System.exit(0);
//            }
        }

        return password;
    }

    protected String readPassword() {
        if (System.console() == null) {
            return Interrogator.getUserScanner().nextLine().trim();
        }
        return new String(System.console().readPassword());
    }
}

