/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import Br.API.Lores;
import Br.LotteryTicket.Lotteries.Welfare3D;
import Br.LotteryTicket.Lottery.Type;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;

/**
 *
 * @author Bryan_lzh
 */
public class LotteryTicket extends JavaPlugin {

    public static Economy econ = null;
    File dataFolder;  //也就是主类中getDataFolder()的返回值
    FileConfiguration config;  //替代getConfig()，读取配置文件时就操作它好了

    @Override
    public void onEnable() {
        Data.LotteryTicket = this;
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Vault未找到,自动卸载插件", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            this.setEnabled(false);
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("BrAPI") == null) {
            getLogger().severe(String.format("[%s] - BrAPI未找到,自动卸载插件", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            this.setEnabled(false);
            return;
        }
        try {
            this.dataFolder = this.getDataFolder();
            reloadCustomConfig();
        } catch (Throwable ex) {
            Logger.getLogger(LotteryTicket.class.getName()).log(Level.SEVERE, null, ex);
        }
        Data.LoadConfig();
        Bukkit.getPluginManager().registerEvents(new LotterListener(), this);
        Utils.registerLottery(new Welfare3D());
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        for (Lottery l : Data.LotteryMap.values()) {
            config.set("Lottery." + l.getCode() + ".Times", l.getTimes());
            config.set("Lottery." + l.getCode() + ".Enable", l.isEnable());
            config.set("Lottery." + l.getCode() + ".Results", Utils.toStringList(l.getResults()));
            System.out.println(l.getName() + " 已储存");
        }
        this.saveConfig();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("lt") || cmd.getName().equalsIgnoreCase("LotteryTicket") || cmd.getName().equalsIgnoreCase("彩票")) {
            if (args.length < 1) {
                sender.sendMessage(Utils.sendMessage("&c参数不足 请输入/" + cmd.getName() + " help 查看帮助"));
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(Utils.sendMessage("&e&l------------------------------------"));
                sender.sendMessage(Utils.sendMessage("&b&lLotteryTicket v" + this.getDescription().getVersion() + "作者: Bryan_lzh"));
                sender.sendMessage(Utils.sendMessage("&a /lt help 查看本帮助"));
                sender.sendMessage(Utils.sendMessage("&a /lt list 查看可用的彩票列表"));
                sender.sendMessage(Utils.sendMessage("&a /lt buy [彩票参数] [选购的数字] <数量> 购买一张彩票"));
                sender.sendMessage(Utils.sendMessage("&e&l------------------------------------"));
                return true;
            }
            if (args[0].equalsIgnoreCase("list")) {
                sender.sendMessage(Utils.sendMessage("&e&l------------------------------------"));
                sender.sendMessage(Utils.sendMessage("&b目前有的 彩票类型 | &a购买用的命令参数"));
                int o = 1;
                for (Lottery lot : Data.LotteryMap.values()) {
                    String stt = "&d&l[" + o + "] " + lot.getName() + "| 对应命令: ";
                    for (String sttt : lot.getCommandName()) {
                        stt += sttt + " ";
                    }
                    sender.sendMessage(Utils.sendMessage(stt));
                    o++;
                }
                sender.sendMessage(Utils.sendMessage("&e&l------------------------------------"));
                return true;
            }
            if (sender instanceof Player) {
                if (args[0].equalsIgnoreCase("buy")) {
                    if (!sender.hasPermission("LotteryTicket.buy")) {
                        sender.sendMessage(Utils.sendMessage("&c&l你没有购买的权限"));
                        return true;
                    }
                    //e.g./lt buy 福彩3D 数字 数量
                    if (args.length < 3) {
                        sender.sendMessage(Utils.sendMessage("&c参数不足 请输入/" + cmd.getName() + " help 查看帮助"));
                        return true;
                    }
                    String name = args[1];

                    Lottery Lot = Data.Find(name);
                    if (Lot == null) {
                        sender.sendMessage(Utils.sendMessage("&c&l插件没有找到此彩种"));
                        return true;
                    }
                    if (!Lot.isEnable()) {
                        sender.sendMessage(Utils.sendMessage("&c&l抱歉 这个彩票类型没有被启用"));
                        return true;
                    }
                    int amount = 1;
                    try {
                        amount = Integer.parseInt(args[3]);
                    } catch (Exception e) {
                    }
                    if (econ.getBalance(sender.getName()) < Lot.getPrice() * amount) {
                        sender.sendMessage(Utils.sendMessage("&c&l你没有足够的金钱"));
                        return true;
                    }
                    econ.withdrawPlayer(sender.getName(), Lot.getPrice() * amount);
                    if (Lot.getType() == Type.String) {
                        String Result;
                        Result = args[2];
                        if (!Lot.isOK(Result)) {
                            sender.sendMessage(Utils.sendMessage("&c&l选购的数字错误 请检查你的输入"));
                            return true;
                        }
                        ItemStack item = new ItemStack(Material.PAPER);
                        ItemMeta im = item.getItemMeta();
                        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + Lot.getName() + " &e彩票"));
                        item.setItemMeta(im);
                        String day = Utils.getDay();
                        item = Lores.addLore(item, "§b购买日期: " + day);
                        item = Lores.addLore(item, "§a购买数字*数量: " + Result + "*" + amount);
                        item = Lores.addLore(item, "§e彩票类型*期数: " + Lot.getCode() + "*" + (Lot.getTimes() + 1));
                        //年+月+日|数字*数量|彩票类型*期数
                        String base64 = Utils.encodeBase64(day.replaceAll("\\|", "+") + "|" + Result + "*" + amount + "|" + Lot.getCode() + "*" + (Lot.getTimes() + 1));
                        item = Lores.addLore(item, base64);
                        ((Player) sender).getInventory().addItem(item);
                        sender.sendMessage(Utils.sendMessage("&b&l你已成功购买了 " + Lot.getName() + " 的彩票,请保留好彩票 开奖后右键查看是否获奖"));
                    }
                    if (Lot.getType() == Type.Int) {
                        int number = 0;
                        try {
                            number = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            sender.sendMessage(Utils.sendMessage("&c&l参赛错误 请检查你的输入"));
                            return true;
                        }
                        if (!Lot.isOK(number)) {
                            sender.sendMessage(Utils.sendMessage("&c&l选购的数字错误 请检查你的输入"));
                            return true;
                        }
                        ItemStack item = new ItemStack(Material.PAPER);
                        ItemMeta im = item.getItemMeta();
                        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + Lot.getName() + " &e彩票"));
                        item.setItemMeta(im);
                        String day = Utils.getDay();
                        item = Lores.addLore(item, "§b购买日期: " + day);
                        item = Lores.addLore(item, "§a购买数字*数量: " + number + "*" + amount);
                        item = Lores.addLore(item, "§e彩票类型*期数: " + Lot.getCode() + "*" + (Lot.getTimes() + 1));
                        //年+月+日|数字*数量|彩票类型*期数
                        String base64 = Utils.encodeBase64(day.replaceAll("\\|", "+") + "|" + number + "*" + amount + "|" + Lot.getCode() + "*" + (Lot.getTimes() + 1));
                        item = Lores.addLore(item, base64);
                        ((Player) sender).getInventory().addItem(item);
                        sender.sendMessage(Utils.sendMessage("&b&l你已成功购买了 " + Lot.getName() + " 的彩票,请保留好彩票 开奖后右键查看是否获奖"));
                    }
                }
            } else {
                sender.sendMessage(Utils.sendMessage("&c&l请不要在后台执行这些命令"));
                return true;
            }
            return true;
        }
        return false;
    }

    @Override
    public FileConfiguration getConfig() {
        return this.config;
    }

    public void reloadCustomConfig() throws Throwable {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists()) {
            configFile.createNewFile();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        DumperOptions yamlOptions = null;
        try {
            Field f = YamlConfiguration.class.getDeclaredField("yamlOptions");
            f.setAccessible(true);

            yamlOptions = new DumperOptions() {
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
            f.set(config, yamlOptions);
            if (!config.contains("Lottery.Plugin")) {
                this.config.set("Lottery.Plugin.Prefix", "&6&l[彩票]");
                this.config.set("Lottery.Plugin.EnableBold", true);
            }
        } catch (ReflectiveOperationException ex) {
        }
    }
}