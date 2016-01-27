/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.DumperOptions;

/**
 *
 * @author Bryan_lzh
 */
public class Data {

    public static List<BukkitRunnable> BukkitRunnableList = new ArrayList<>();
    public static LotteryTicket LotteryTicket;
    public static HashMap<String, Lottery> LotteryMap = new HashMap<>();
    //Config:
    public static String Prefix;
    public static boolean EnableBold;

    public static void LoadConfig() {
        FileConfiguration config = Data.LotteryTicket.getConfig();
        DumperOptions yamlOptions = null;
        try {
            Field f = YamlConfiguration.class.getDeclaredField("yamlOptions");   //获取类YamlConfiguration里的匿名yamlOptions字段
            f.setAccessible(true);

            yamlOptions = new DumperOptions() {  //将yamlOptions字段替换为一个DumperOptions的匿名内部类，里面替换了setAllowUnicode方法让其永远无法设置为true
                private TimeZone timeZone = TimeZone.getDefault();

                @Override
                public void setAllowUnicode(boolean allowUnicode) {
                    super.setAllowUnicode(false);
                }

                @Override
                public void setLineBreak(DumperOptions.LineBreak lineBreak) {
                    super.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak());
                }
            };
            yamlOptions.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak());
            f.set(Data.LotteryTicket.getConfig(), yamlOptions); //把新的yamlOptions偷梁换柱回去
        } catch (ReflectiveOperationException ex) {
        }
        Data.Prefix = config.getString("Lottery.Plugin.Prefix");
        Data.EnableBold = Boolean.valueOf(config.getBoolean("Lottery.Plugin.EnableBold")).booleanValue();
    }
}
