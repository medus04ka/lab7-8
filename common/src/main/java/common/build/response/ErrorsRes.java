package common.build.response;

public class ErrorsRes extends Response {
    public ErrorsRes(String error) {
        super("error", error);
    }
}