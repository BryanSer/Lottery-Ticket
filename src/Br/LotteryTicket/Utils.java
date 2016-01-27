/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Bryan_lzh
 */
public abstract class Utils {

    public static List<Result> toResult(List<String> list, String Name) {
        List<Result> l = new ArrayList<>();
        for (String s : list) {
            String str[] = s.split("\\|");
            l.add(new Result(Name, Integer.parseInt(str[0]), Integer.parseInt(str[1])));
        }
        return l;
    }

    public static List<String> toStringList(List<Result> list) {
        List<String> l = new ArrayList<>();
        for (Result r : list) {
            l.add(r.getTimes() + "|" + r.getNumber());
        }
        return l;
    }

    public static String encodeBase64(String s) {
        byte[] b = Base64.encodeBase64(s.getBytes());
        return new String(b);
    }

    public static String decodeBase64(String s) {
        return new String(Base64.decodeBase64(s));
    }

    public static void registerLottery(Lottery l) {
        if (Data.LotteryMap.containsKey(l.getCode())) {
            Data.LotteryMap.remove(l.getCode());
        }
        Data.LotteryMap.put(l.getCode(), l);
        for (String s : l.getCommandName()) {
            Data.SimpCommand.put(s, l.getCode());
        }
        FileConfiguration config = Data.LotteryTicket.getConfig();
        if (!config.contains("Lottery." + l.getCode())) {
            config.set("Lottery." + l.getCode() + ".Name", l.getName());
            config.set("Lottery." + l.getCode() + ".Times", 0);
            config.set("Lottery." + l.getCode() + ".Enable", false);

        }
        if (!config.contains("Lottery." + l.getCode() + ".Results")) {
            config.set("Lottery." + l.getCode() + ".Results", new ArrayList<String>());
        }
        l.setEnable(config.getBoolean("Lottery." + l.getCode() + ".Enable"));
        l.setTimes(config.getInt("Lottery." + l.getCode() + ".Times"));
        l.setResults(Utils.toResult(config.getStringList("Lottery." + l.getCode() + ".Results"), l.getCode()));
        l.loadConfig(config);
        Data.LotteryTicket.saveConfig();
        LotterTask LT = new LotterTask();
        LT.setLottery(l);
        Data.BukkitRunnableList.add(LT);
        LT.runTaskTimer(Data.LotteryTicket, l.getInterval() / 2l, l.getInterval());
    }

    public static String sendMessage(String s) {
        s = ChatColor.translateAlternateColorCodes('&', Data.Prefix + s);
        if (!Data.EnableBold) {
            s = s.replaceAll("§l", "");
            s = s.replaceAll("§L", "");
        }
        return s;
    }

    public static String getDay() {
        return Calendar.getInstance().get(Calendar.YEAR) + "|" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "|" + Calendar.getInstance().get(Calendar.DATE);
    }

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
                ticket.Number = Integer.parseInt(str.split("\\*")[0]);
            }
            if (i == 2) {
                ticket.Type = str.split("\\*")[0];
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
