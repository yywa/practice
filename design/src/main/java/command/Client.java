package command;

/**
 * 创建一个具体命令对象并设定它的接收者。
 *
 * @author yyw
 * @version 1.0
 **/
public class Client {

    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        Command command = new ConcreteCommand(receiver);
        Invoker invoker = new Invoker();
        invoker.setCommand(command);
        invoker.executeCommand();
        invoker.unDoCommand();
    }
}
