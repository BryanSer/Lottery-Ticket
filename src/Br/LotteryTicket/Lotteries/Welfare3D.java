/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket.Lotteries;

import Br.LotteryTicket.Lottery;
import Br.LotteryTicket.LotteryTicket;
import Br.LotteryTicket.Result;
import Br.LotteryTicket.Ticket;
import Br.LotteryTicket.Utils;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Bryan_lzh
 */
public class Welfare3D implements Lottery {

    private String Name = "福彩3D";
    private boolean Enable = false;
    private int Times;
    private double Pirce = 5d;
    private List<Result> Results;
    private Long Interval;

    @Override
    public String getName() {
        return this.Name;
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
        if (i > 999) {
            return false;
        }
        if (i < 100) {
            return false;
        }
        return true;
    }

    @Override
    public long getInterval() {
        return this.Interval;// * 6L;
    }

    @Override
    public String Lottery() {
        Random random = new Random();
        int i = 0;
        while (true) {
            int var = random.nextInt(9);
            if (var != 0) {
                i += var * 100;
                break;
            }
        }
        i += random.nextInt(9) * 10;
        i += random.nextInt(9);
        this.Times++;
        this.Results.add(new Result(this.Name, this.Times, i));
        return "&e&l{福彩3D} 第[" + this.Times + "] 期彩票已经开奖 结果为: " + i + "  &b请有购买的拿起彩票右键领取";
    }

    @Override
    public double getPrice() {
        return this.Pirce;
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
    public void loadConfig(FileConfiguration config) {
        if (!config.contains("Lottery.Welfare3D.Pirce")) {
            config.set("Lottery.Welfare3D.Pirce", this.Pirce);
        }
        if (!config.contains("Lottery.Welfare3D.Interval_单位小时")) {
            config.set("Lottery.Welfare3D.Interval_单位小时", 6);
        }
        this.Pirce = config.getDouble("Lottery.Welfare3D.Pirce");
        this.Interval = 20L*60L*60L*Long.valueOf(config.getInt("Lottery.Welfare3D.Interval_单位小时")).longValue();
        Bukkit.broadcastMessage(""+this.Interval);
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
        int t[] = Utils.toIntArray(ticket.Number);
        int re[] = Utils.toIntArray(r.getNumber());
        int i = 0;
        int a = 0;
        for (int e : re) {
            if (t[i] == e) {
                a++;
            }
            i++;
            if (i > 2) {
                break;
            }
        }
        switch (a) {
            case 0:
                p.sendMessage(Utils.sendMessage("&6&l可惜 你没有获得任何奖励QAQ"));
                break;
            case 1:
                LotteryTicket.econ.depositPlayer(p, 5d * ticket.Amount);
                p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了一个数字 获得: " + 5d * ticket.Amount + LotteryTicket.econ.currencyNamePlural()));
                break;
            case 2:
                LotteryTicket.econ.depositPlayer(p, 10d * ticket.Amount);
                p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了二个数字 获得: " + 10d * ticket.Amount + LotteryTicket.econ.currencyNamePlural()));
                break;
            case 3:
                LotteryTicket.econ.depositPlayer(p, 20d * ticket.Amount);
                p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了三个数字 获得: " + 20d * ticket.Amount + LotteryTicket.econ.currencyNamePlural()));
                break;
        }
    }

    @Override
    public String getCode() {
        return "Welfare3D";
    }
}
