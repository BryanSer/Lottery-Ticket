/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import Br.API.Lores;
import Br.LotteryTicket.Lotteries.Welfare3D;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Bryan_lzh
 */
public class LotteryTicket extends JavaPlugin {

    public static Economy econ = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(Utils.getDay());
    }

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
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
            this.saveDefaultConfig();
        }
        Utils.registerLottery(new Welfare3D());
    }

    @Override
    public void onDisable() {
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
                //TODO 显示帮助
                return true;
            }
            if (sender instanceof Player) {
                if (args[0].equalsIgnoreCase("buy")) {
                    //e.g./lt buy 福彩3D 数字 数量
                    if (args.length < 3) {
                        sender.sendMessage(Utils.sendMessage("&c参数不足 请输入/" + cmd.getName() + " help 查看帮助"));
                        return true;
                    }
                    String name = args[1];
                    if (!Data.LotteryMap.containsKey(name)) {
                        sender.sendMessage(Utils.sendMessage("&c&l插件没有找到此彩种"));
                        return true;
                    }
                    Lottery Lot = Data.LotteryMap.get(name);
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
                    int number = 0;
                    try {
                        number = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        sender.sendMessage(Utils.sendMessage("&c&l参赛错误 请检查你的输入"));
                        return true;
                    }
                    ItemStack item = new ItemStack(Material.PAPER);
                    ItemMeta im = item.getItemMeta();
                    im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&o&r&6" + Lot.getName() + " 彩票"));
                    item.setItemMeta(im);
                    String day = Utils.getDay();
                    item = Lores.addLore(item, "§b购买日期: " + day);
                    item = Lores.addLore(item, "§a购买数字*数量: " + number + "*" + amount);
                    item = Lores.addLore(item, "§e彩票类型*期数: " + Lot.getName()+"*"+(Lot.getTimes() + 1));
                    //年+月+日|数字*数量|彩票类型*期数
                    String base64 = Utils.encodeBase64(day.replaceAll("\\|", "+") + "|" + number + "*" + amount + "|" + Lot.getName()+"*"+(Lot.getTimes() + 1));
                    item = Lores.addLore(item, base64);
                    ((Player) sender).getInventory().addItem(item);
                    sender.sendMessage(Utils.sendMessage("&b&l你已成功购买了 " + Lot.getName() + " 的彩票,请保留好彩票 开奖后右键查看是否获奖"));
                }
            } else {
                sender.sendMessage(Utils.sendMessage("&c&l请不要在后台执行这些命令"));
                return true;
            }
            return true;
        }
        return false;
    }
}