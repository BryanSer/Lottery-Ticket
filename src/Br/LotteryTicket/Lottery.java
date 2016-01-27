/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * 实现该接口的类请在Utils.registerLottery();里注册
 * 为每个类提供的配置文件的位置在 Lottery.彩票的名字.* 下 请将自己的配置读取放在loadConfig()方法下
 * 请不要在注册类之前读取配置
 * @author Bryan_lzh
 */
public interface Lottery {

    /**
     *
     * @return 返回这个彩票类型的名字(不作为类型进行识别)
     */
    public String getName();
    
    /**
     *
     * @return 作为识别码 请不要带中文
     */
    public String getCode();
    /**
     *
     * @param b 设置是否启用这个彩票类型
     */
    public void setEnable(boolean b);

    /**
     *
     * @return 是否已启用
     */
    public boolean isEnable();

    /**
     *
     * @param i 传入的这个数字是否复合该类型的规则
     * @return 是否复合
     */
    public boolean isOK(int i);

    /**
     *
     * @return 返回开奖的间隔(1s=20tick)
     */
    public long getInterval();

    /**
     *
     * @return 返回费用
     */
    public double getPrice();

    /**
     *
     * @param i 设置彩票期数
     */
    public void setTimes(int i);

    /**
     *
     * @return 返回彩票期数
     */
    public int getTimes();

    /**
     * @return 返回值将进行广播
     */
    public String Lottery();
    

    /**
     * 读取配置
     * @param config 传入的配置文件
     */
    public void loadConfig(FileConfiguration config);
    
    /**
     * @return 返回当前的结果
     */
    public List<Result> getResults();
    
    /**
     * 
     * @param list 设置Result列表
     */
    public void setResults(List<Result> list);
    
    /**
     * 处理兑奖事件
     * @param p 请求兑奖的玩家
     * @param ticket 兑奖用的彩票
     * @param r 该期的彩票结果
     */
    public void Award(Player p,Ticket ticket,Result r);
    public String[] getCommandName();
}
