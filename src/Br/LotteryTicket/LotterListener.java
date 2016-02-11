/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;


import Br.LotteryTicket.Lottery.Type;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Bryan_lzh
 */
public class LotterListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent evt) {
        if (evt.getPlayer().isOp()) {
            if (LotteryTicket.NeedUpdata) {
                evt.getPlayer().sendMessage(new String[]{
                            Utils.sendMessage("&e&lLotteryTicket彩票插件已有新版本"),
                            Utils.sendMessage("&e&l请联系你们的技术人员或服主更新插件"),
                            Utils.sendMessage("&6&l地址: http://www.mcbbs.net/thread-547272-1-1.html")
                        });
            }
        }
    }

    @EventHandler
    public void UseTicket(PlayerInteractEvent evt) {
        if (!evt.hasItem()) {
            return;
        }
        if (evt.getItem().getType() != Material.PAPER) {
            return;
        }
        if (!evt.getItem().getItemMeta().hasDisplayName()) {
            return;
        }
        if (!evt.getItem().getItemMeta().getDisplayName().contains("§e彩票")) {
            return;
        }
        if (evt.getItem().getItemMeta().getDisplayName().contains("§d已兑奖")) {
            return;
        }
        String base64 = evt.getItem().getItemMeta().getLore().get(3);
        base64 = Utils.decodeBase64(base64);
        Ticket ticket = Utils.CheckItem(evt.getItem(), base64);
        if (!ticket.Passed) {
            evt.getPlayer().sendMessage(Utils.sendMessage("&c&l你的这张彩票没有通过校验..."));
        }
        Lottery Lot = Data.LotteryMap.get(ticket.LotteryName);
        if (ticket.Times > Lot.getTimes()) {
            evt.getPlayer().sendMessage(Utils.sendMessage("&e&l抱歉 这张彩票还没开奖"));
            return;
        }
        for (Result r : Lot.getResults()) {
            if (r.getTimes() == ticket.Times) {
                evt.getPlayer().sendMessage(new String[]{
                            "§6你购买的数字: " + ((Lot.getType() == Type.String) ? ticket.Result : ticket.Number),
                            "§e开奖的结果:   " + ((Lot.getType() == Type.String) ? r.getResult() : r.getNumber())
                        });
                Lot.Award(evt.getPlayer(), ticket, r);
                ItemStack item = evt.getItem();
                evt.getPlayer().getInventory().remove(item);
                ItemMeta im = item.getItemMeta();
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&m" + im.getDisplayName().split("§6")[1] + " &r&d已兑奖"));
                item.setItemMeta(im);
                item.setAmount(1);
                item = Br.API.Lores.addLore(item, "&d&l已兑奖");
                evt.getPlayer().getInventory().addItem(item);
                return;
            }
        }

        evt.getPlayer()
                .sendMessage(Utils.sendMessage("&6&l未找到此彩票的信息 是否超出了兑奖时间?"));
    }
}
