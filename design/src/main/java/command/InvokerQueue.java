package command;

/**
 * 进行批处理的Invoker
 *
 * @author yyw
 * @version 1.0
 **/
public class InvokerQueue {
    private CommandQueue commandQueue;

    public InvokerQueue(CommandQueue commandQueue) {
        this.commandQueue = commandQueue;
    }

    public CommandQueue getCommandQueue() {
        return commandQueue;
    }

    public void setCommandQueue(CommandQueue commandQueue) {
        this.commandQueue = commandQueue;
    }

    public void executeCommand() {
        commandQueue.execute();
    }

    /**
     * TODO 可以固定删除最后一个指令，或者删除传入的指令。此处删除的是传入的指令
     */
    public void removeCommand(Command command) {
        commandQueue.remove(command);
    }
}
