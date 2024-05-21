package common.model;

import common.util.Element;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс Хуман сущность.
 */
public class HumanBeing extends Element {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Boolean realHero; //Поле не может быть null
    private boolean hasToothpick;
    private Long impactSpeed; //Максимальное значение поля: 659
    private WeaponType weaponType; //Поле может быть null
    private Mood mood; //Поле не может быть null
    private int ownerId;

    public HumanBeing(
            int id,
            String name,
            Coordinates coordinates,
            LocalDate creationDate,
            Boolean realHero,
            boolean hasToothpick,
            Long impactSpeed,
            WeaponType weaponType,
            Mood mood,
            int ownerId) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.realHero = realHero;
        this.hasToothpick = hasToothpick;
        this.impactSpeed = impactSpeed;
        this.weaponType = weaponType;
        this.mood = mood;
        this.ownerId = ownerId;

    }

    public HumanBeing() {
    }

    /**
     * Валидирует правильность полей.
     *
     * @return true, если все поля корректны, иначе false.
     */
//    @Override
//    public int compareTo(HumanBeing o) {
//        return Double.compare(this.impactSpeed, o.getImpactSpeed());
//    }
    @Override
    public boolean validate() {
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (creationDate == null) return false;
        if (realHero == null) return false;
        if (impactSpeed > 659) return false;
        if (mood == null) return false;

        return true;
    }

    /**
     * Update.
     *
     * @param humanBeing the human being
     */
    public void update(HumanBeing humanBeing) {
        this.name = humanBeing.name;
        this.coordinates = humanBeing.coordinates;
        this.realHero = humanBeing.realHero;
        this.hasToothpick = humanBeing.hasToothpick;
        this.impactSpeed = humanBeing.impactSpeed;
        this.weaponType = humanBeing.weaponType;
        this.mood = humanBeing.mood;

    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Boolean isRealHero() {
        return realHero;
    }

    public boolean hasToothpick() {
        return hasToothpick;
    }

    public Long getImpactSpeed() {
        return impactSpeed;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public Mood getMood() {
        return mood;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HumanBeing that = (HumanBeing) o;
        return id == that.id && hasToothpick == that.hasToothpick &&
                Long.compare(that.impactSpeed, impactSpeed) == 0 &&
                Objects.equals(name, that.name) && Objects.equals(coordinates, that.coordinates) &&
                Objects.equals(creationDate, that.creationDate) && Objects.equals(realHero, that.realHero) &&
                weaponType == that.weaponType && mood == that.mood;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, realHero, hasToothpick, impactSpeed, weaponType, mood);
    }

    @Override
    public String toString() {
        return "HumanBeing #" + id +
                " (created on " + creationDate + ")" +
                "\n Имя убийцы: " + name +
                "\n Местоположение: " + coordinates +
                "\n А он точно убийца??: " + realHero +
                "\n Он Фредди Крюггер?: " + hasToothpick +
                "\n Скорость удара: " + impactSpeed +
                "\n Оружие: " + (weaponType != null ? weaponType : "None") +
                "\n Mood: " + mood;
    }

    @Override
    public int compareTo(Element element) {
        return this.id - element.getId();
    }

    public void setId(int id) {
        this.id = id;
    }

    public HumanBeing setName(String name) {
        this.name = name;
        return this;
    }

    public HumanBeing setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public HumanBeing setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Boolean getRealHero() {
        return realHero;
    }

    public HumanBeing setRealHero(Boolean realHero) {
        this.realHero = realHero;
        return this;
    }

    public boolean isHasToothpick() {
        return hasToothpick;
    }

    public HumanBeing setHasToothpick(boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
        return this;
    }

    public HumanBeing setImpactSpeed(Long impactSpeed) {
        this.impactSpeed = impactSpeed;
        return this;
    }

    public HumanBeing setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
        return this;
    }

    public HumanBeing setMood(Mood mood) {
        this.mood = mood;
        return this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public HumanBeing setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return this;
    }
}