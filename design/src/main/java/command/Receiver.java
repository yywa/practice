package command;

/**
 * 如何实现与执行一个请求相关的操作
 *
 * @author yyw
 * @version 1.0
 **/
public class Receiver {

    /**
     * 执行动作
     */
    public void action() {
        System.out.println("正在执行一个命令");
    }

    /**
     * 撤销命令
     */
    public void unAction() {
        System.out.println("撤销一个命令");
    }
}
