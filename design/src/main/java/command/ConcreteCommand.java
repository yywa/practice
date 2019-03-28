package command;


/**
 * 将一个接收者对象绑定于一个动作。
 * 调用接收者相应的操作，以实现Execute。
 *
 * @author yyw
 * @version 1.0
 **/
public class ConcreteCommand implements Command {
    Receiver receiver;

    public ConcreteCommand() {
    }

    public ConcreteCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.action();
    }

    @Override
    public void unDo() {
        receiver.unAction();
    }
}
