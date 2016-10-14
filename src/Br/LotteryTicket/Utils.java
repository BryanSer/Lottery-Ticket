/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import Br.LotteryTicket.Lottery.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Bryan_lzh
 */
public abstract class Utils {

    /**
     * 将配置文件中的List转换回Result的List
     *
     * @param list 配置文件中的List
     * @param Name 该彩票的名称
     * @param t 彩票的类型
     * @return Result的List
     */
    public static List<Result> toResult(List<String> list, String Name, Type t) {
        List<Result> l = new ArrayList<>();
        if (t == Type.Int) {
            for (String s : list) {
                String str[] = s.split("\\|");
                l.add(new Result(Name, Integer.parseInt(str[0]), Integer.parseInt(str[1])));
            }
        }
        if (t == Type.String) {
            for (String s : list) {
                String str[] = s.split("\\|");
                l.add(new Result(Name, Integer.parseInt(str[0]), str[1]));
            }
        }
        return l;
    }

    /**
     * 将Result的List转换为String的List来储存
     *
     * @param list Result的List
     * @param t 彩票的类型
     * @return String的List
     */
    public static List<String> toStringList(List<Result> list, Lottery.Type t) {
        List<String> l = new ArrayList<>();
        if (t == Type.Int) {
            for (Result r : list) {
                l.add(r.getTimes() + "|" + r.getNumber());
            }
        }
        if (t == Type.String) {
            for (Result r : list) {
                l.add(r.getTimes() + "|" + r.getResult());
            }
        }
        return l;
    }

    /**
     * base64加密
     *
     * @param s 要加密的字符串
     * @return 加密后的字符串
     */
    public static String encodeBase64(String s) {
        try {
            byte[] b = Base64.encodeBase64(s.getBytes());
            return new String(b);
        } catch (NoClassDefFoundError e) {
            String b = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(s.getBytes());
            return b;
        }
    }

    /**
     * base64解密
     *
     * @param s 要解密的字符串
     * @return 解密后的字符串
     */
    public static String decodeBase64(String s) {
        try {
            return new String(Base64.decodeBase64(s));
        } catch (NoClassDefFoundError e) {
            return new String(com.sun.org.apache.xerces.internal.impl.dv.util.Base64.decode(s));
        }
    }

    /**
     * 注册彩票. 如果没有在此注册彩票的数据将不会读取.
     *
     * @param l 要注册的彩票 {@link Lottery}
     */
    public static void registerLottery(Lottery l) {
        if (Data.LotteryMap.containsKey(l.getCode())) {
            Data.LotteryMap.remove(l.getCode());
        }
        Data.LotteryMap.put(l.getCode(), l);
        for (String s : l.getCommandName()) {
            Data.SimpCommand.put(s, l.getCode());
        }
        FileConfiguration config = Data.LotteryTicket.getConfig();
        if (!config.contains("Lottery." + l.getCode() + ".Enable")) {
            config.set("Lottery." + l.getCode() + ".Name", l.getName());
            config.set("Lottery." + l.getCode() + ".Times", 0);
            config.set("Lottery." + l.getCode() + ".Enable", true);
        }
        if (!config.contains("Lottery." + l.getCode() + ".Results")) {
            config.set("Lottery." + l.getCode() + ".Results", new ArrayList<String>());
        }
        l.setEnable(config.getBoolean("Lottery." + l.getCode() + ".Enable"));
        l.setTimes(config.getInt("Lottery." + l.getCode() + ".Times"));
        l.setResults(Utils.toResult(config.getStringList("Lottery." + l.getCode() + ".Results"), l.getCode(), l.getType()));
        l.loadConfig(config);
        Data.LotteryTicket.saveConfig();
        LotterTask LT = new LotterTask();
        LT.setLottery(l);
        BukkitTask runTaskTimer = LT.runTaskTimer(Data.LotteryTicket, l.getInterval() / 2l, l.getInterval());
        Data.BukkitTaskList.put(l.getCode(), runTaskTimer);
    }

    /**
     * 将一个字符串修改成发送给玩家的格式
     *
     * @param s 字符串
     * @return 发送给玩家的字符串
     */
    public static String sendMessage(String s) {
        s = ChatColor.translateAlternateColorCodes('&', Data.Prefix + s);
        if (!Data.EnableBold) {
            s = s.replaceAll("§l", "");
            s = s.replaceAll("§L", "");
        }
        return s;
    }

    /**
     * 将一个字符串组修改为发给玩家的格式
     *
     * @param str 字符串组
     * @return 发给玩家的字符串组
     */
    public static String[] sendMessage(String[] str) {
        int i = 0;
        for (String s : str) {
            str[i] = ChatColor.translateAlternateColorCodes('&', Data.Prefix + s);
            if (!Data.EnableBold) {
                str[i] = str[i].replaceAll("§l", "");
                str[i] = str[i].replaceAll("§L", "");
            }
            i++;
        }
        return str;
    }

    /**
     *
     * @return 返回今天的日期
     */
    public static String getDay() {
        return Calendar.getInstance().get(Calendar.YEAR) + "|" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "|" + Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 检查彩票数据
     *
     * @param is 需要检查的物品
     * @param s 警告base64解密后的数据
     * @return {@link Ticket}
     */
    public static Ticket CheckItem(ItemStack is, String s) {
        //年+月+日|数字*数量|彩票类型*期数
        String args[] = s.split("\\|");
        List<String> Lore = is.getItemMeta().getLore();
        Ticket ticket = new Ticket(is);
        int i = 0;
        for (String str : args) {
            if (i == 3) {
                break;
            }
            if (i == 0) {
                str = str.replaceAll("\\+", "|");
                ticket.Day = str;
            }
            if (i == 1) {
                ticket.Amount = Integer.parseInt(str.split("\\*")[1]);
                try {
                    ticket.Number = Integer.parseInt(str.split("\\*")[0]);
                } catch (Exception e) {
                    ticket.Result = str.split("\\*")[0];
                }
            }
            if (i == 2) {
                ticket.LotteryName = str.split("\\*")[0];
                ticket.Times = Integer.parseInt(str.split("\\*")[1]);
            }
            String key = Lore.get(i).split(" ")[1];
            if (!key.equalsIgnoreCase(str)) {
                ticket.Passed = false;
                return ticket;
            }
            i++;
        }
        ticket.Passed = true;
        return ticket;
    }

    /**
     * 将一个int的每一位转换成新的数组 自用函数
     *
     * @param i
     * @return
     */
    public static int[] toIntArray(int i) {
        String s = String.valueOf(i);
        char c[] = s.toCharArray();
        int r[] = new int[c.length];
        int o = 0;
        for (char ch : c) {
            r[o] = Integer.valueOf(ch + "");
            o++;
        }
        return r;
    }
}
