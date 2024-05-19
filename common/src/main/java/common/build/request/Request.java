package common.build.request;

import common.model.User;

import java.io.Serializable;
import java.util.Objects;

/**
 * The type Request.
 */
public abstract class Request implements Serializable {
    private final String name;

    private final User user;

    /**
     * Instantiates a new Request.
     *
     * @param name the name
     */
    public Request(String name, User user) {
        this.user = user;
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request response = (Request) o;
        return Objects.equals(name, response.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                '}';
    }

    public boolean isAuth() {
        return false;
    }
}