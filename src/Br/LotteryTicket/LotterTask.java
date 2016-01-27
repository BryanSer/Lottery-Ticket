/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Bryan_lzh
 */
public class LotterTask extends BukkitRunnable {

    private Lottery Lot;

    @Override
    public void run() {
        Bukkit.broadcastMessage(Utils.sendMessage(Lot.Lottery()));
    }

    public Lottery getLottery() {
        return this.Lot;
    }

    public void setLottery(Lottery l) {
        this.Lot = l;
    }
}
