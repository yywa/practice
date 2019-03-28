package command;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * 命令队列实现
 * TODO 如果还想继续细化，可以给每个命令实现类添加状态管理
 *
 * @author yyw
 * @version 1.0
 **/
public class CommandQueue {
    private ArrayList<Command> commands = Lists.newArrayList();

    public void add(Command command) {
        commands.add(command);
    }

    public void remove(Command command) {
        commands.remove(command);
    }

    public void execute() {
        commands.forEach(Command::execute);
    }
}
