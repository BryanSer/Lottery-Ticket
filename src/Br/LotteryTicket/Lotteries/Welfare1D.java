/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket.Additions.Lotteries;


import Br.LotteryTicket.Lottery;
import Br.LotteryTicket.LotteryTicket;
import Br.LotteryTicket.Result;
import Br.LotteryTicket.Ticket;
import Br.LotteryTicket.Utils;
import java.util.List;
import java.util.Random;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Bryan_lzh
 */
public class Welfare1D implements Lottery {

    private String Name = "福彩1D";
    private boolean Enable = false;
    private long Interval;
    private double Price = 5d;
    private int Times;
    private List<Result> Results;
    private String[] Commands = new String[]{"福彩1D", "福利彩票1D", "1D", "Welfare1D"};
    private double v;

    @Override
    public String getName() {
        return this.Name;
    }

    @Override
    public String getCode() {
        return "Welfare1D";
    }

    @Override
    public void setEnable(boolean b) {
        this.Enable = b;
    }

    @Override
    public boolean isEnable() {
        return this.Enable;
    }

    @Override
    public boolean isOK(int i) {
        if (i == 0 || i == 1) {
            return true;
        }
        return false;
    }

    @Override
    public long getInterval() {
        return this.Interval;
    }

    @Override
    public double getPrice() {
        return this.Price;
    }

    @Override
    public void setTimes(int i) {
        this.Times = i;
    }

    @Override
    public int getTimes() {
        return this.Times;
    }

    @Override
    public String Lottery() {
        Random ran = new Random();
        int i =0;
        if(ran.nextBoolean()){
            i=1;
        }
        this.Times++;
        this.Results.add(new Result(this.Name, this.Times, i));
        return "&e&l{福彩1D} &b&l第[" + this.Times + "] 期彩票已经开奖 结果为: &d&l[" + i + "]  &b&l请有购买的拿起彩票右键领取";
    }

    @Override
    public void loadConfig(FileConfiguration config) {
        if (!config.contains("Lottery.Welfare1D.Price")) {
            config.set("Lottery.Welfare3D.Pirce", this.Price);
        }
        if (!config.contains("Lottery.Welfare1D.开奖间隔_单位小时")) {
            config.set("Lottery.Welfare1D.开奖间隔_单位小时", 3.0d);
        }
        if (!config.contains("Lottery.Welfare1D.赢得奖励")) {
            config.set("Lottery.Welfare1D.赢得奖励", 8.0d);
        }
        this.v = config.getDouble("Lottery.Welfare1D.赢得奖励");
        this.Price = config.getDouble("Lottery.Welfare1D.Price");
        this.Interval = Long.valueOf((20 * 60 * 60 * config.getDouble(("Lottery.Welfare1D.开奖间隔_单位小时")) + "").split("\\.")[0]).longValue();
    }

    @Override
    public List<Result> getResults() {
        return this.Results;
    }

    @Override
    public void setResults(List<Result> list) {
        this.Results = list;
    }

    @Override
    public void Award(Player p, Ticket ticket, Result r) {
        if(ticket.Number == r.getNumber()){
            LotteryTicket.econ.depositPlayer(p, this.v*ticket.Amount);
            p.sendMessage(Utils.sendMessage("&e&l恭喜 你中奖了 获得: " + this.v * ticket.Amount + LotteryTicket.econ.currencyNamePlural()));
        }else {
            p.sendMessage(Utils.sendMessage("&d抱歉 你没有获得任何奖励"));
        }
    }

    @Override
    public String[] getCommandName() {
        return this.Commands;
    }

    @Override
    public boolean isOK(String s) {
        return false;
    }

    @Override
    public Type getType() {
        return Type.Int;
    }
}
