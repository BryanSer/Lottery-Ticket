/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.DumperOptions;

/**
 *
 * @author Bryan_lzh
 */
public abstract class Utils {

    public static String encodeBase64(String s) {
        try {
            byte[] b = Base64.encodeBase64(s.getBytes("UTF-8"));
            return new String(b);
        } catch (UnsupportedEncodingException ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l编码base64时出现问题"));
            Bukkit.getConsoleSender().sendMessage(ex.getMessage());
            return null;
        }
    }

    public static String decodeBase64(String s) {
        return new String(Base64.decodeBase64(s));
    }

    public static void registerLottery(Lottery l) {
        if (Data.LotteryMap.containsKey(l.getName())) {
            Data.LotteryMap.remove(l.getName());
        }
        Data.LotteryMap.put(l.getName(), l);
        FileConfiguration config = Data.LotteryTicket.getConfig();
        if (!config.contains("Lottery." + l.getName())) {
            config.set("Lottery." + l.getName() + ".Times", 0);
            config.set("Lottery." + l.getName() + ".Enable", true);
        }
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
        l.setEnable(config.getBoolean("Lottery." + l.getName() + ".Enable"));
        l.setTimes(config.getInt("Lottery." + l.getName() + ".Times"));
        Data.LotteryTicket.saveConfig();
    }

    public static String sendMessage(String s) {
        s = ChatColor.translateAlternateColorCodes('&', Data.Prefix + s);
        if (!Data.EnableBold) {
            s = s.replaceAll("§l", "");
        }
        return s;
    }

    public static String getDay() {
        return Calendar.getInstance().get(Calendar.YEAR) + "|" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "|" + Calendar.getInstance().get(Calendar.DATE);
    }

    public static boolean CheckItem(ItemStack is, String s) {
        //年+月+日|数字*数量|彩票类型*期数
        String args[] = s.split("\\|");
        List<String> Lore = is.getItemMeta().getLore();
        int i = 0;
        for (String str : Lore) {
            if(i==3){
                break;
            }
            if(i==0){
                str = str.replaceAll("\\+", "|");
            }
            String key = args[i].split(" ")[1];
            if(!key.equalsIgnoreCase(str)){
                return false;
            }
            i++;
        }
        return true;
    }
}
