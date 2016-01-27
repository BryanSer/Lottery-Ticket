/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Br.LotteryTicket;

/**
 *
 * @author Bryan_lzh
 */
public class Result {

    private String Name;
    private int Times;
    private int Number;

    public Result(String name, int times, int number) {
        this.Name = name;
        this.Times = times;
        this.Number = number;
    }

    public String getName() {
        return this.Name;
    }

    public int getTimes() {
        return this.Times;
    }

    public int getNumber() {
        return this.Number;
    }
}
