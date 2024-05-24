package common.build.response;

public class NoSuchCommandRes extends Response {
    public NoSuchCommandRes(String name) {
        super(name, "??? дурачок залогинься");
    }
}