/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import java.io.File;
import java.util.HashMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Bryan_lzh
 */
public class Data {

    public static HashMap<String, BukkitTask> BukkitTaskList = new HashMap<>();
    public static Main LotteryTicket;
    public static HashMap<String, Lottery> LotteryMap = new HashMap<>();
    public static HashMap<String, String> SimpCommand = new HashMap<>();
    //Config:
    public static String Prefix;
    public static boolean EnableBold;

    public static Lottery Find(String Name) {
        if (!Data.SimpCommand.containsKey(Name)) {
            return null;
        }
        return Data.LotteryMap.get(Data.SimpCommand.get(Name));
    }

    public static void LoadConfig() {
        FileConfiguration config = Data.LotteryTicket.getConfig();
        Data.Prefix = config.getString("Lottery.Plugin.Prefix");
        Data.EnableBold = config.getBoolean("Lottery.Plugin.EnableBold");
    }

    public static void LoadScripts() {
        File folder = new File(Data.LotteryTicket.getDataFolder(), File.separator + "Scripts" + File.separator);
        for (File f : folder.listFiles()) {
            if(f.isFile()){
                ScriptsLottery sl = new ScriptsLottery(f);
                Utils.registerLottery(sl);
            }
        }
    }
}
