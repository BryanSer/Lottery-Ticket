/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Bryan_lzh
 */
public class Ticket {
    private ItemStack Item;
    public String Day;
    public int Amount;
    public int Number;
    public String Result;
    
    public String LotteryName;
    public int Times;
    public boolean Passed;
    public Ticket(ItemStack is){
        this.Item = is;
    }
}
