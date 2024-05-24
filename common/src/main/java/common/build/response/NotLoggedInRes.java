package common.build.response;

public class NotLoggedInRes extends Response {

    public NotLoggedInRes(String name) {
        super(name, "u are not logged in");
    }
}