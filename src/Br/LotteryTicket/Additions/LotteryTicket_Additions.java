/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket.Additions;

import Br.LotteryTicket.Additions.Lotteries.TwentyonePickFive;
import Br.LotteryTicket.Additions.Lotteries.Welfare1D;
import Br.LotteryTicket.Data;
import Br.LotteryTicket.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Bryan_lzh
 */
public class LotteryTicket_Additions extends JavaPlugin {

    private final double NeedVersion = 1.2d;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("LotteryTicket") == null) {
            getLogger().severe(String.format("[%s] - LotteryTicket未找到,自动卸载插件", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            this.setEnabled(false);
            return;
        }
        if (Double.valueOf(Data.LotteryTicket.getDescription().getVersion()).doubleValue() < this.NeedVersion) {
            getLogger().severe(String.format("[%s] - LotteryTicket版本过低,自动卸载插件", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            this.setEnabled(false);
            return;
        }
        Utils.registerLottery(new Welfare1D());
        Utils.registerLottery(new TwentyonePickFive());
    }

    @Override
    public void onDisable() {
    }
}
