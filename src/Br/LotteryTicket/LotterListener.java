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
        String base64 = evt.getItem().getItemMeta().getLore().get(5);
        base64 = Utils.decodeBase64(base64);
        //  if(!Utils.CheckItem(evt.getItem(), base64)){
        //         evt.getPlayer().sendMessage(Utils.sendMessage("&c&l你的彩票校验失败"));
        //      }
    }
}
