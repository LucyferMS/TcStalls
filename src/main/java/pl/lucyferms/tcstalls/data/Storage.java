package pl.lucyferms.tcstalls.data;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.lucyferms.tcstalls.essentials.ConfigUtils;

import java.util.Date;

public class Storage {

    public static String worldName = "world";
    public static int buyDays = 7;
    public static int maxOccupationDays = 30;
    public static long buyTime = (1000L * 60 * 60 * 24 * buyDays);
    public static long maxOccupationTime = (1000L * 60 * 60 * 24 * maxOccupationDays);
    public static int maxStalls = 3;

    public static void loadConfig(){
        YamlConfiguration config = ConfigUtils.load("config.yml");

        assert config != null;
        worldName = config.getString("world");
        buyDays = config.getInt("buy-days");
        maxOccupationDays = config.getInt("max-occupation-days");
        buyTime = (1000L * 60 * 60 * 24 * buyDays);
        maxOccupationTime = (1000L * 60 * 60 * 24 * maxOccupationDays);
        maxStalls = config.getInt("max-stalls-per-player");
    }


}
