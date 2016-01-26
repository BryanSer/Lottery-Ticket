/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

/**
 *
 * @author Bryan_lzh
 */
public interface Lottery {

    public String getName();

    public void setEnable(boolean b);

    public boolean isEnable();

    public int getDigit();


    public boolean isOK(int i);


    public long getInterval();
    
    public double getPrice();
    
    public void setTimes(int i);
    
    public int getTimes();
/**
 * @return 返回值将进行广播
 */
    public String Lottery();
}
