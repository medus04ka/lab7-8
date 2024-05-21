package common.model;

import java.io.Serializable;

/**
 * Ваш муд по жиЖни(на всю жизнь, лично мой отчислиться, но не судьба походу).
 */
public enum Mood implements Serializable {
    SADNESS,
    LONGING,
    GLOOM,
    CALM,
    RAGE;

    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var weaponType : values()) {
            nameList.append(weaponType.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}
