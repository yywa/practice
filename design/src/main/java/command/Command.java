package command;

/**
 * 命令接口
 *
 * @author yyw
 * @version 1.0
 **/
public interface Command {

    /**
     * 执行
     */
    void execute();

    /**
     * 取消操作
     */
    void unDo();
}
