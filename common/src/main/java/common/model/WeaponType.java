package common.model;

import java.io.Serializable;

/**
 * То, чем обычно убивают.
 */
public enum WeaponType implements Serializable {
    AXE,
    SHOTGUN,
    RIFLE,
    KNIFE,
    MACHINE_GUN;

    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var weaponType : values()) {
            nameList.append(weaponType.name()).append(", ");
        }
        return nameList.substring(0, nameList.length() - 2);
    }
}
