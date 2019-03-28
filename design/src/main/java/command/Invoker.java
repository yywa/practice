package command;

/**
 * 要求该命令执行这个请求
 *
 * @author yyw
 * @version 1.0
 **/
public class Invoker {

    Command command;

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }

    public void unDoCommand() {
        command.unDo();
    }
}
