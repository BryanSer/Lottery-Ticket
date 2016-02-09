/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Bryan_lzh
 */
public class Data {

    public static List<BukkitRunnable> BukkitRunnableList = new ArrayList<>();
    public static LotteryTicket LotteryTicket;
    public static HashMap<String, Lottery> LotteryMap = new HashMap<>();
    public static HashMap<String,String> SimpCommand = new HashMap<>();
    //Config:
    public static String Prefix;
    public static boolean EnableBold;

    public static Lottery Find(String Name) {
        if(!Data.SimpCommand.containsKey(Name)){
            return null;
        }
        return Data.LotteryMap.get(Data.SimpCommand.get(Name));
    }

    public static void LoadConfig() {
        FileConfiguration config = Data.LotteryTicket.getConfig();
        Data.Prefix = config.getString("Lottery.Plugin.Prefix");
        Data.EnableBold = Boolean.valueOf(config.getBoolean("Lottery.Plugin.EnableBold")).booleanValue();
    }
}
