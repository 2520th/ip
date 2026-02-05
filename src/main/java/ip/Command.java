package ip;

public class Command {
    private final String cmdType;
    private final String[] contents;

    public Command(String cmdType, String[] contents) {
        this.cmdType = cmdType;
        this.contents = contents;
    }

    public String getCmdType() {
        return cmdType;
    }

    public String[] getContents() {
        return contents;
    }
}
