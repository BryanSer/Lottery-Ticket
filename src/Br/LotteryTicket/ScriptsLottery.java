/*
 * 开发者:Bryan_lzh
 * QQ:390807154
 * 保留一切所有权
 * 若为Bukkit插件 请前往plugin.yml查看剩余协议
 */
package Br.LotteryTicket;

import Br.API.Scripts.ScriptLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Bryan_lzh
 * @version 1.0
 */
public class ScriptsLottery implements Lottery {

    private File ScriptFile;
    private Description Desc;
    private int Times;
    private List<Result> Results;
    private NashornScriptEngine Script;

    public static class Description {

        private String[] Usage;
        private String[] CommandName;
        private String Code = null;
        private double Price = -1;
        private int Interval = -1;
        private String Interval_Name;
        private boolean Enable = true;
        private Type Type;
        private String Name;

        public Description(File f) {
            List<String> usage = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
                String read = null;
                while ((read = br.readLine()) != null) {
                    if (read.startsWith("// @")) {
                        String s = read.replaceFirst("// @", "");
                        if (s.startsWith("Usage")) {
                            usage.add(s.replaceFirst("Usage ", ""));
                        } else if (s.startsWith("Code")) {
                            this.Code = s.replaceFirst("Code ", "");
                        } else if (s.startsWith("Type")) {
                            this.Type = Lottery.Type.valueOf(s.replaceFirst("Type ", ""));
                        } else if (s.startsWith("Name")) {
                            this.Name = s.replaceFirst("Name ", "");
                        } else if (s.startsWith("Command")) {
                            this.CommandName = s.replaceFirst("Command ", "").toLowerCase().split(",");
                        } else if (s.startsWith("Default ")) {
                            s = s.replaceFirst("Default ", "");
                            if (s.startsWith("Price")) {
                                this.Price = Double.parseDouble(s.replaceFirst("Price ", ""));
                            } else if (s.startsWith("Interval")) {
                                Interval_Name = s.replaceFirst("Interval ", "");
                                this.Interval = (int) (Br.API.Utils.getTimeLength(Interval_Name) / 50);
                            } else if (s.startsWith("Enable")) {
                                this.Enable = Boolean.parseBoolean(s.replaceFirst("Enable ", ""));
                            }
                        }
                    }
                }
                Usage = new String[usage.size()];
                this.Usage = usage.toArray(Usage);
            } catch (IOException e) {
            }
        }

        public String getInterval_Name() {
            return Interval_Name;
        }

        public String[] getCommandName() {
            return CommandName;
        }

        public String[] getUsage() {
            return Usage;
        }

        public String getCode() {
            return Code;
        }

        public Type getType() {
            return Type;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }

        public int getInterval() {
            return Interval;
        }

        public void setInterval(int Interval) {
            this.Interval = Interval;
        }

        public boolean isEnable() {
            return Enable;
        }

        public void setEnable(boolean Enable) {
            this.Enable = Enable;
        }

        public String getName() {
            return Name;
        }

    }

    public ScriptsLottery(File f) {
        this.ScriptFile = f;
        this.Desc = new Description(f);
        Script = ScriptLoader.evalAsUTF8(Data.LotteryTicket, f);
        this.invokeFunction("init");
    }

    private <V> V invokeFunction(String s, Object... obj) {
        try {
            return (V) this.Script.invokeFunction(s, obj);
        } catch (ScriptException ex) {
            Logger.getLogger(ScriptsLottery.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ScriptsLottery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private <V> V invokeFunction(String s) {
        return this.invokeFunction(s, new Object[]{});
    }

    @Override
    public String[] getUsage() {
        return this.Desc.getUsage();
    }

    @Override
    public String getName() {
        return this.Desc.getName();
    }

    @Override
    public String getCode() {
        return this.Desc.getCode();
    }

    @Override
    public void setEnable(boolean b) {
        this.Desc.setEnable(b);
    }

    @Override
    public boolean isEnable() {
        return this.Desc.isEnable();
    }

    @Override
    public boolean isOK(int i) {
        try {
            return this.invokeFunction("isValid", i);
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public boolean isOK(String s) {
        try {
            return this.invokeFunction("isValid", s);
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public long getInterval() {
        return this.Desc.getInterval();
    }

    @Override
    public double getPrice() {
        return this.Desc.getPrice();
    }

    @Override
    public void setTimes(int i) {
        this.Times = i;
    }

    @Override
    public int getTimes() {
        return this.Times;
    }

    @Override
    public String Lottery() {
        String s = this.invokeFunction("Lottery");
        switch (this.Desc.getType()) {
            case Int:
                int i = this.invokeFunction("getLastResult");
                this.Results.add(new Result(this.getName(), ++this.Times, i));
                s = s.replaceAll("%result%", String.valueOf(i));
                break;
            case String:
                String v = this.invokeFunction("getLastResult");
                this.Results.add(new Result(this.getName(), ++this.Times, s));
                s = s.replaceAll("%result%", v);
                break;
        }
        s = s.replaceAll("%times%", String.valueOf(this.Times));
        return s;
    }

    @Override
    public void loadConfig(FileConfiguration config) {
        if (!config.contains("Price")) {
            config.set("Price", this.Desc.getPrice());
        } else {
            this.Desc.setPrice(config.getDouble("Price"));
        }
        if (!config.contains("Interval")) {
            config.set("Interval", this.Desc.getInterval_Name());
        } else {
            this.Desc.setInterval((int) (Br.API.Utils.getTimeLength(config.getString("Interval")) / 50));
        }
        this.invokeFunction("loadConfig", config);
    }

    @Override
    public List<Result> getResults() {
        return this.Results;
    }

    @Override
    public void setResults(List<Result> list) {
        this.Results = list;
    }

    @Override
    public void Award(Player p, Ticket ticket, Result r) {
        this.invokeFunction("onAward", p, ticket, r);
    }

    @Override
    public String[] getCommandName() {
        return this.Desc.getCommandName();
    }

    @Override
    public Type getType() {
        return this.Desc.getType();
    }

}
