/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Bryan_lzh
 */
public class LotterTask extends BukkitRunnable {

    private Lottery Lot;

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Lottery getLottery() {
        return this.Lot;
    }
}
