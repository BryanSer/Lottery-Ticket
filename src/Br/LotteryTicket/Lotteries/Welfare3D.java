/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket.Lotteries;

import Br.LotteryTicket.Lottery;


/**
 *
 * @author Bryan_lzh
 */
public class Welfare3D implements Lottery {
    private String Name = "福彩3D";
    private boolean Enable = false;
    private int Times;

    @Override
    public String getName() {
        return this.Name;
    }

    @Override
    public void setEnable(boolean b) {
        this.Enable = b;
    }

    @Override
    public boolean isEnable() {
        return this.Enable;
    }

    @Override
    public int getDigit() {
        return 3;
    }


    @Override
    public boolean isOK(int i) {
        return false;
    }


    @Override
    public long getInterval() {
        return 20L*60L*60L*6L;
    }

    @Override
    public String Lottery() {
        return null;
    }

    @Override
    public double getPrice() {
        return 5D;
    }

    @Override
    public void setTimes(int i) {
        this.Times = i;
    }

    @Override
    public int getTimes() {
        return this.Times;
    }
}
