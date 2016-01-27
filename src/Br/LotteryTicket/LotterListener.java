/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Bryan_lzh
 */
public class LotterListener implements Listener {

    @EventHandler
    public void UseTicket(PlayerInteractEvent evt) {
        if (!evt.hasItem()) {
            return;
        }
        if (evt.getItem().getType() != Material.PAPER) {
            return;
        }
        if (!evt.getItem().getItemMeta().getDisplayName().contains("§7§o§r")) {
            return;
        }
        String base64 = evt.getItem().getItemMeta().getLore().get(4);
        base64 = Utils.decodeBase64(base64);
        Ticket ticket = Utils.CheckItem(evt.getItem(), base64);
        if (!ticket.Passed) {
            evt.getPlayer().sendMessage(Utils.sendMessage("&c&l你的这张彩票没有通过校验..."));
        }
        Lottery Lot = Data.LotteryMap.get(ticket.Type);
        if (ticket.Times > Lot.getTimes()) {
            evt.getPlayer().sendMessage(Utils.sendMessage("&6&l抱歉 这张彩票还没开奖"));
            return;
        }
        for (Result r : Lot.getResults()) {
            if (r.getTimes() == ticket.Times) {
                Lot.Award(evt.getPlayer(), ticket, r);
                return;
            }
        }
        evt.getPlayer().sendMessage(Utils.sendMessage("&6&l未找到此彩票的信息 是否超出了兑奖时间?"));
    }
}
