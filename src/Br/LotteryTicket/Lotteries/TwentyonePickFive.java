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
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Bryan_lzh
 */
public class TwentyonePickFive implements Lottery {
    
    private String Name = "21选5";
    private boolean Enable = false;
    private double Price = 5d;
    private int Times;
    private List<Result> Results;
    private double v1;
    private double v2;
    private double v3;
    private double v4;
    private double v5;
    private Long Interval;
    private String[] Commands = new String[]{"21选5", "21p5", "TwentyonePickFive"};
    
    @Override
    public String getName() {
        return this.Name;
    }
    
    @Override
    public String getCode() {
        return "TwentyonePickFive";
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
        return false;
    }
    
    @Override
    public boolean isOK(String s) {
        String str[] = s.split(",");
        if (str.length == 5) {
            for (String o : str) {
                try {
                    int i = Integer.parseInt(o);
                    if (i > 0 && i < 22) {
                        continue;
                    } else {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
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
        Random Random = new Random();
        int result[] = new int[5];
        int i = 0;
        A:
        while (true) {
            B:
            while (true) {
                result[i] = Random.nextInt(21);
                if (result[i] != 0) {
                    break B;
                }
            }
            i++;
            if (i >= 5) {
                break;
            }
        }
        this.Times++;
        this.Results.add(new Result(this.Name, this.Times, this.toStrings(result)));
        return "&6&l  {21选5} [" + this.Times + "] 期已开奖, 开奖结果为: " + this.toStrings(result);
    }
    
    @Override
    public void loadConfig(FileConfiguration config) {
        if (!config.contains("Lottery.TwentyonePickFive.Price")) {
            config.set("Lottery.TwentyonePickFive.Price", this.Price);
        }
        if (!config.contains("Lottery.TwentyonePickFive.开奖间隔")) {
            config.set("Lottery.TwentyonePickFive.开奖间隔", 6.0);
        }
        if (!config.contains("Lottery.TwentyonePickFive.中1个数字的奖励")) {
            config.set("Lottery.TwentyonePickFive.中1个数字的奖励", 8d);
            config.set("Lottery.TwentyonePickFive.中2个数字的奖励", 18d);
            config.set("Lottery.TwentyonePickFive.中3个数字的奖励", 30d);
            config.set("Lottery.TwentyonePickFive.中4个数字的奖励", 42d);
            config.set("Lottery.TwentyonePickFive.中5个数字的奖励", 60d);
        }
        this.Price = config.getDouble("Lottery.TwentyonePickFive.Price");
        this.Interval = Long.valueOf((20 * 60 * 60 * config.getDouble("Lottery.TwentyonePickFive.开奖间隔") + "").split("\\.")[0]).longValue();
        System.out.println(this.Interval);
        this.v1 = config.getDouble("Lottery.TwentyonePickFive.中1个数字的奖励");
        this.v2 = config.getDouble("Lottery.TwentyonePickFive.中2个数字的奖励");
        this.v3 = config.getDouble("Lottery.TwentyonePickFive.中3个数字的奖励");
        this.v4 = config.getDouble("Lottery.TwentyonePickFive.中4个数字的奖励");
        this.v5 = config.getDouble("Lottery.TwentyonePickFive.中5个数字的奖励");
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
        int t[] = this.toIntArray(ticket.Result);
        int re[] = this.toIntArray(r.getResult());
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
                LotteryTicket.econ.depositPlayer(p, this.v1 * ticket.Amount);
                p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了一个数字 获得: " + this.v1 * ticket.Amount + LotteryTicket.econ.currencyNamePlural()));
                break;
            case 2:
                LotteryTicket.econ.depositPlayer(p, this.v2 * ticket.Amount);
                p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了二个数字 获得: " + this.v2 * ticket.Amount + LotteryTicket.econ.currencyNamePlural()));
                break;
            case 3:
                LotteryTicket.econ.depositPlayer(p, this.v3 * ticket.Amount);
                p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了三个数字 获得: " + this.v3 * ticket.Amount + LotteryTicket.econ.currencyNamePlural()));
                break;
            case 4:
                LotteryTicket.econ.depositPlayer(p, this.v4 * ticket.Amount);
                p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了四个数字 获得: " + this.v4 * ticket.Amount + LotteryTicket.econ.currencyNamePlural()));
                break;
            case 5:
                LotteryTicket.econ.depositPlayer(p, this.v5 * ticket.Amount);
                p.sendMessage(Utils.sendMessage("&e&l恭喜 你中了&d&l五&e&l个数字 获得: " + this.v5 * ticket.Amount + LotteryTicket.econ.currencyNamePlural()));
                Bukkit.broadcastMessage(Utils.sendMessage("&6&l恭喜&d&l" + p.getName() + "&6在第[" + ticket.Times + "] 21选5 彩票中获得头奖, 奖金: " + this.v5 * ticket.Amount));
                break;
        }
    }
    
    @Override
    public String[] getCommandName() {
        return this.Commands;
    }
    
    @Override
    public Type getType() {
        return Type.String;
    }
    
    private int[] toIntArray(String s) {
        String str[] = s.split(",");
        int i[] = new int[s.length()];
        int o = 0;
        for (String m : str) {
            i[o] = Integer.parseInt(m);
            o++;
        }
        return i;
    }
    
    private String toStrings(int i[]) {
        String s = "";
        for (int o : i) {
            s += o + ",";
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }

    @Override
    public String[] getUsage() {
        return new String[]{
            "§6[选购的数字]: 5个大于0小于22的数字 用,分割",
            "§b例如: 1,9,20,19,5 | 6,6,8,19,21"
        };
    }
}

