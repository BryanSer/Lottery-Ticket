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

    public String getDay() {
        return Day;
    }

    public int getAmount() {
        return Amount;
    }

    public int getNumber() {
        return Number;
    }

    public String getResult() {
        return Result;
    }

    public String getLotteryName() {
        return LotteryName;
    }

    public int getTimes() {
        return Times;
    }

    public boolean isPassed() {
        return Passed;
    }
    
    
}
