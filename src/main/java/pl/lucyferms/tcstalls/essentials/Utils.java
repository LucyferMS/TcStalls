package pl.lucyferms.tcstalls.essentials;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import pl.lucyferms.tcstalls.TcStalls;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils {

    /**
     * Checks if given arg is Integer
     * @param a String to check
     * @return Returns false if arg is integer
     */
    public static boolean isInteger(final String a) {
        try {
            Integer.parseInt(a);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given arg is Material
     * @param a String to check
     * @return Returns true if arg is material
     */
    public static boolean isMaterial(final String a){
        try {
            Material.valueOf(a);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Checks if given arg is Double
     * @param a String to check
     * @return Returns false if arg is double
     */
    public static boolean isDouble(final String a) {
        try {
            Double.parseDouble(a);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if given arg is Mob
     * @param mob_name String to check
     * @return Returns false if arg is Mob
     */
    public static boolean isMob(String mob_name){
        try{
            EntityType.valueOf(mob_name);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Checks if given arg is Enchantment Name
     * @param a String to check
     * @return Returns true if arg is Enchantment
     */
    public static boolean isEnchantment(String a){
        try{
            Enchantment.getByName(a);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public static boolean isVillagerProfession(String a){
        try{
            Villager.Profession.valueOf(a);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Clears full config
     * @param config_name Config name to clear
     */
    public static void clearConfig(String config_name){
        /*File file = new File(Main.getInstance().getDataFolder(), config_name);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);*/

        YamlConfiguration configuration = ConfigUtils.load(config_name);

        for(String key : configuration.getKeys(false)){
            configuration.set(key, null);
        }

        ConfigUtils.save(configuration, config_name);
    }

    /**
     * Changes miliseconds to hours
     * @param milis Miliseconds
     * @return Returns hours
     */
    public static int milisToHours(long milis){
        int time = (int) (((milis/1000)/60)/60);
        return time;
    }

    /**
     * Counts entities in specified chunk
     * @param entityType Types of entity to count
     * @param chunk Chunk where function will count entity
     * @return Return amount of entities
     */
    public static int getEntitiesInChunk(EntityType entityType, Chunk chunk){
        int sum = 0;
        for(Entity entity : chunk.getEntities()){
            if(entity.getType().equals(entityType)){
                sum++;
            }
        }
        return sum;
    }

    /**
     * Returns random number in range
     * @param min Lowest possible number
     * @param max Highest possible number
     * @return Returns random number
     */
    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static String locationToString(Location location){
        return "World: " + location.getWorld().getName() + " x: " + location.getX() + " y: " + location.getY() + " z: " + location.getZ();
    }

    public static boolean isPlayerInRegion(Player player, RegionManager manager, ProtectedRegion region, String world) {
        boolean playerIsIn = false;
        Location location = player.getLocation();
        assert manager != null;
        if (!world.equals(location.getWorld().getName())) {
            return false;
        }
        if (region.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ())) {
            playerIsIn = true;
        }
        return playerIsIn;
    }

    public static String color(String string) {
        return IridiumColorAPI.process(string);
    }

    public static List<String> color(List<String> strings) {
        return strings.stream().map(Utils::color).collect(Collectors.toList());
    }
}
