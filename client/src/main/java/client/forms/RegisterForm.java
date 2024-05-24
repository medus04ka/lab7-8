package client.forms;

import client.util.Interrogator;
import client.util.console.Cons;
import common.model.User;

public class RegisterForm extends AuthenthicateForm {
    private final Cons console;

    public RegisterForm(Cons console) {
        super(console);
        this.console = console;
    }

    @Override
    public User build() throws Exception {
        String name = askName();
        if (name.isBlank()) throw new Exception();
        String password = askPassword();
        if (password.isBlank()) throw new Exception();
        var user = new User(name, password);
        if (!user.validate()) throw new Exception();
        return user;
    }

    /**
     * Запрашивает пароль пользователя и проверяет его.
     *
     * @return Пароль пользователя.
     */
    @Override
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

                console.println("Повторите пароль пользователя:");
                console.ps2();

                var passwordRepeat = readPassword();
                if (fileMode) console.println(passwordRepeat);

                if (!password.equals(passwordRepeat)) throw new Exception();
                break;
            } catch (Exception exception) {
                console.printError("Пароль пользователя не распознан!");
                if (fileMode) throw new Exception();
                break;
            }
        }

        return password;
    }
}