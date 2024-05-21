package common.util;

import java.io.Serializable;

public abstract class Element implements Comparable<Element>, Validator, Serializable {
    abstract public int getId();
}