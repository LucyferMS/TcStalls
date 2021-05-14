package pl.lucyferms.tcstalls.stall;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.lucyferms.tcstalls.TcStalls;
import pl.lucyferms.tcstalls.data.Storage;
import pl.lucyferms.tcstalls.essentials.ChatUtils;
import pl.lucyferms.tcstalls.essentials.ConfigUtils;
import pl.lucyferms.tcstalls.essentials.Debug;
import pl.lucyferms.tcstalls.essentials.Utils;
import pl.lucyferms.tcstalls.langPL.Messages;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Stalls {

    private static final HashMap<String, Stall> stalls = new HashMap<>();

    public static HashMap<String, Stall> getStalls(){
        return stalls;
    }

    public static boolean doesStallExists( String regionName ){
        return stalls.containsKey(regionName);
    }

    public static Stall getStall(String regionName){
        if( doesStallExists(regionName) ) {
            return stalls.getOrDefault(regionName, null);
        }
        return null;
    }

    public static boolean addStall(Stall stall){
        if( !doesStallExists(stall.getRegionName() )) {
            stalls.put(stall.getRegionName(), stall);
            return true;
        }
        return false;
    }

    public static boolean deleteStall( String regionName ){
        if( doesStallExists(regionName) ){
            stalls.remove(regionName);
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Objects.requireNonNull(Bukkit.getWorld("" + Storage.worldName))));
            ProtectedRegion region = regionManager.getRegion("" + regionName);
            region.getOwners().clear();
            region.getFlags().clear();
            return true;
        }
        return false;
    }

    public static String getStallOwnerName( String regionName ){
        return getStall(regionName).getOwner();
    }

    public static boolean isPlayerOwnerOfThisStall( Player player, String regionName ){
        Stall stall = Stalls.getStall(regionName);
        if( stall.getOwner().equalsIgnoreCase("" + Messages.toSale ) ){
            return false;
        }
        return stall.getOwner().equals(player.getName());
    }

    public static void abandonStall(String regionName){
        Stall stall = Stalls.getStall(regionName);
        stall.removeOwner();
        Debug.log("" + Messages.stallAbandoned.replace("%regionName%", regionName));

    }

    public static int getAmountOfPlayerMarkets(Player player){
        int amount = 0;

        for(String regionName : getStalls().keySet()){
            if(getStall(regionName) != null){
                if (getStall(regionName).getOwner().equalsIgnoreCase(player.getName())) {
                    amount++;
                }
            }
        }

        return amount;
    }

    public static void load(){
        stalls.clear();
        YamlConfiguration stallsConf = ConfigUtils.load("stalls.yml");
        if( !stallsConf.contains("stalls") ){
            Debug.sendError("" + Messages.noStalls);
            return;
        }

        assert stallsConf != null;
        for( String regionName : stallsConf.getConfigurationSection("stalls").getKeys(false) ) {

            String owner = "";
            Date buyDate;
            Date expiryDate;
            double price = 0.0;
            if( stallsConf.contains("stalls." + regionName + ".owner") ) {
                owner = stallsConf.getString("stalls." + regionName + ".owner");
            }
            else{
                Debug.sendError("Nie moge zaladowac ownera");
            }
            buyDate = Date.from(Instant.now());
            if( stallsConf.contains("stalls." + regionName + ".buyDate") ) {
                buyDate.setTime(stallsConf.getLong("stalls." + regionName + ".buyDate"));
            }
            else{
                buyDate = null;
            }

            expiryDate = Date.from(Instant.now());
            if( stallsConf.contains("stalls." + regionName + ".expiryDate") ) {
                expiryDate.setTime(stallsConf.getLong("stalls." + regionName + ".expiryDate"));
            }
            else{
                expiryDate = null;
            }

            if( stallsConf.contains("stalls." + regionName + ".price") ) {
                price = stallsConf.getDouble("stalls." + regionName + ".price");
            }

            stalls.put(regionName, new Stall(owner, buyDate, expiryDate, price, regionName) );
            if( owner.equalsIgnoreCase("" + Messages.toSale) ){
                getStall(regionName).removeOwner();
            }
        }
    }

    public static void save(){

        YamlConfiguration stallsConf = new YamlConfiguration();

        for( String regionName : getStalls().keySet() ){
            stallsConf.set("stalls." + regionName + ".owner", stalls.get(regionName).getOwner() );
            if( getStall(regionName).getBuyDate() != null ){
                stallsConf.set("stalls." + regionName + ".buyDate", stalls.get(regionName).getBuyDate().getTime() );
            }
            if( getStall(regionName).getExpiryDate() != null ) {
                stallsConf.set("stalls." + regionName + ".expiryDate", stalls.get(regionName).getExpiryDate().getTime());
            }
            stallsConf.set("stalls." + regionName + ".price", stalls.get(regionName).getPrice() );
            if( getStall(regionName).getOwner().equalsIgnoreCase("" + Messages.toSale) ){
                getStall(regionName).removeOwner();
            }
        }
        ConfigUtils.save(stallsConf, "stalls.yml");
    }


    public static void showStall( CommandSender sender, String regionName) {
        SimpleDateFormat format = new SimpleDateFormat("" + Messages.dateFormat);
        String buyString;
        String expiryString;
        if (!doesStallExists(regionName)) {
            ChatUtils.sendMessage(sender, "" + Messages.thisStallDoesntExists);
            return;
        }
        if (getStall(regionName).getBuyDate() != null) {
            buyString = format.format(Stalls.getStall(regionName).getBuyDate());
        } else {
            buyString = "" + Messages.notApplicable;
        }
        if (getStall(regionName).getExpiryDate() != null) {
            expiryString = format.format(Stalls.getStall(regionName).getExpiryDate());
        } else {
            expiryString = "" + Messages.notApplicable;
        }


        if (!Messages.separatorHeader.equalsIgnoreCase("none")) {
            sender.sendMessage("" + Messages.separatorHeader.replace("%regionName%", regionName));
        }
        if (!Messages.name.equalsIgnoreCase("none")) {
            sender.sendMessage("" + Messages.name.replace("%regionName%", regionName));
        }
        if (!Messages.owner.equalsIgnoreCase("none")) {
            sender.sendMessage("" + Messages.owner.replace("%owner%", Stalls.getStall(regionName).getOwner()));
        }
        if (!Messages.buyDate.equalsIgnoreCase("none")) {
            sender.sendMessage("" + Messages.buyDate.replace("%date%", buyString));
        }
        if (!Messages.expiryDate.equalsIgnoreCase("none")) {
            sender.sendMessage("" + Messages.expiryDate.replace("%date%", expiryString));
        }
        if (!Messages.price.equalsIgnoreCase("none")) {
            sender.sendMessage("" + Messages.price.replace("%price%", Stalls.getStall(regionName).getPrice().toString()));
        }
        if (!Messages.separatorFooter.equalsIgnoreCase("none")) {
            sender.sendMessage("" + Messages.separatorFooter.replace("%regionName%", regionName));
        }

    }

    public static void showStalls(String label, CommandSender sender, int site, String...rgName){
        SimpleDateFormat format = new SimpleDateFormat("" + Messages.dateFormat);
        if( stalls.size() == 0 ){
            ChatUtils.sendMessage(sender, "Nie posiadam zadnych straganow");
            return;
        }
        if( rgName.length == 0 ){
            Player p = (Player) sender;
            int page = 1;
            int maxpage = (int) Math.ceil(Stalls.getStalls().size() / 10.00);
            if( maxpage == 0 ){
                maxpage = 1;
            }
            int current = 0;
            page = Math.min(maxpage, site);

            p.sendMessage(Utils.color("" + Messages.listHeader));
            for (String regionName : Stalls.getStalls().keySet()) {


                StringBuilder listOneElementFormat = new StringBuilder(Utils.color("" + Messages.listOneElementFormat));



                Stall stall = Stalls.getStall(regionName);
                String owner = Utils.color(stall.getOwner());
                String buyString;
                String expiryString;
                if( stall.getBuyDate() == null ){
                    buyString = Utils.color("" + Messages.notApplicable);
                }
                else{
                    buyString = format.format(stall.getBuyDate());
                }
                if( stall.getExpiryDate() == null ){
                    expiryString = Utils.color("" + Messages.notApplicable);
                }
                else{
                    expiryString = format.format(stall.getExpiryDate());
                    listOneElementFormat.append(" ").append(Utils.color("" + Messages.timeRange));
                }


                if (current >= (page - 1) * 10 && current < page * 10) {
                    TextComponent cmdComponent = new TextComponent(Utils.color("&a" + listOneElementFormat.toString()
                            .replace("%regionName%", "" + regionName)
                            .replace("%owner%", "" + owner)
                            .replace("%buyDate%", "" + buyString)
                            .replace("%expiryDate%", "" + expiryString + " ")));
                    if( owner.equalsIgnoreCase("" + Messages.toSale) ){
                        cmdComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label + " zajmij " + regionName));
                        cmdComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color(Messages.claimStallHoverMessage.replace("%regionName%", "" + (regionName)))).create()));

                    }
                    else {
                        cmdComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " sprawdz " + regionName));
                        cmdComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color(Messages.showStallInfoHoverMessage.replace("%page%", "" + (page + 1)))).create()));
                    }
                    p.getPlayer().spigot().sendMessage(cmdComponent);
                }

                current++;
            }
            String listFooter = Messages.listFooter;

            BaseComponent[] components = TextComponent.fromLegacyText(Utils.color(listFooter.replace("%maxpage%", maxpage + "").replace("%page%", page + "")));

            for (BaseComponent component : components) {
                if (ChatColor.stripColor(component.toLegacyText()).contains(Messages.nextPage)) {
                    if (page < maxpage) {
                        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " lista " + (page + 1)));
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Messages.helpPageHoverMessage.replace("%page%", "" + (page + 1))).create()));
                    }
                    if( page == maxpage ){
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Messages.noNextPage.replace("%page%", "" + (page + 1))).create()));
                    }

                } else if (ChatColor.stripColor(component.toLegacyText()).contains(Messages.previousPage)) {
                    if (page > 1) {
                        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " lista " + (page - 1)));
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Messages.helpPageHoverMessage.replace("%page%", "" + (page - 1))).create()));
                    }
                    if( page == 1 ){
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Messages.noPreviousPage.replace("%page%", "" + (page + 1))).create()));
                    }
                }
            }
            p.getPlayer().spigot().sendMessage(components);

//            for( String regionName : Stalls.getStalls().keySet() ){
//                showStall(sender, regionName);
//            }
        }
        else{
            String regionName = rgName[0];
            if( !doesStallExists(regionName) ){
                ChatUtils.sendMessage(sender, "" + Messages.thisStallDoesntExists);
                return;
            }
            showStall(sender, regionName);
        }
    }

    public static void claimStall( Player player, String regionName ){
        Stall stall = Stalls.getStall(regionName);

        if(stall == null){
            ChatUtils.sendMessage(player, "" + Messages.thisStallDoesntExists);
            return;
        }
        if( stall.isOccupied() ){
            ChatUtils.sendMessage(player, "" + Messages.thisStallIsOccupied);
            return;
        }
        if(!stall.canBuy(player)){
            ChatUtils.sendMessage(player, "" + Messages.noMoney);
            return;
        }
        if(getAmountOfPlayerMarkets(player) >= Storage.maxStalls){
            ChatUtils.sendMessage(player, "" + Messages.youReachedLimitOfStalls);
            return;
        }

        TcStalls.getEcon().withdrawPlayer(player, stall.getPrice());
        stall.buy(player.getName());

        stall.setBuyDate(Date.from(Instant.now()));
        Date expiryDate = Date.from(Instant.now());
        expiryDate.setTime(expiryDate.getTime() + (1000 * 60 * 60) + Storage.buyTime);
        expiryDate.setSeconds(0);
        expiryDate.setMinutes(0);
        stall.setExpiryDate(expiryDate);
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Objects.requireNonNull(Bukkit.getWorld("" + Storage.worldName))));
        ProtectedRegion region = regionManager.getRegion("" + regionName);

        region.getFlags().clear();
        region.setFlag(Flags.GREET_TITLE, "" + ChatUtils.fixColor("" + Messages.occupiedGreetingOwner.replace("%owner%", player.getName()) + "\n" + Messages.occupiedGreetingRegionName.replace("%regionName%", regionName)));
        region.getOwners().addPlayer(player.getUniqueId());
//        if( region.getMembers() != null) {
//            DefaultDomain owners = region.getOwners();
//
//            owners.addPlayer(player.getUniqueId());
//            region.setOwners(owners);
//        }


//        String command = "rg addowner " + stall.getRegionName()  + " -w " + Storage.worldName + " " + player.getName();
//        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
//        command = "rg addowner " + stall.getRegionName()  + " -w " + Storage.worldName + " " + player.getName();
        ChatUtils.sendMessage(player, "" + Messages.stallPurchased.replace("%regionName%", regionName));
        Stalls.save();
    }

    public static void extendClaimTime(Player player, String regionName ){
        SimpleDateFormat format = new SimpleDateFormat("" + Messages.dateFormat);
        Stall stall = getStall(regionName);
        if( stall.getOwner().equalsIgnoreCase("" + Messages.toSale)){
            ChatUtils.sendMessage(player, "" + Messages.thisStaleDoesntHaveAnOwner);
            return;
        }
        if(!player.getName().equalsIgnoreCase(stall.getOwner())){
            ChatUtils.sendMessage(player, "" + Messages.youAreNotOwnerOfThisStall);
            return;
        }
        if(!stall.canBuy(player)){
            ChatUtils.sendMessage(player, "" + Messages.noMoney);
            return;
        }

        TcStalls.getEcon().withdrawPlayer(player, stall.getPrice());

        Date buyDate = stall.getBuyDate();
        Date expiryDate = stall.getExpiryDate();
        String expiryString = format.format(expiryDate);

        if( (expiryDate.getTime() + (1000 * 60 * 60) + (1000L * 60 * 60 * 24 * Storage.buyDays)) - Date.from(Instant.now()).getTime() > Storage.maxOccupationTime ){
            ChatUtils.sendMessage(player, "" + Messages.expiryTimeCantBeGreaterThan.replace("%days%", "" + Storage.maxOccupationDays));
            ChatUtils.sendMessage(player, "" + Messages.currentExpiryDate.replace("%date%", expiryString));
            return;
        }
        expiryDate.setTime(expiryDate.getTime() + (1000 * 60 * 60) + (1000L * 60 * 60 * 24 * Storage.buyDays));
        expiryDate.setSeconds(0);
        expiryDate.setMinutes(0);


        stall.setExpiryDate(expiryDate);

        expiryString = format.format(Stalls.getStall(regionName).getExpiryDate());

        ChatUtils.sendMessage(player, "" + Messages.expiryDateExtendedTo.replace("%date%", expiryString ) );
        Stalls.save();
    }

    public static void checkStalls(){

        SimpleDateFormat format = new SimpleDateFormat("" + Messages.dateFormat);
        Debug.log("Sprawdzam stragany");
        for( String regionName : Stalls.getStalls().keySet()){
            Stall stall = Stalls.getStall(regionName);
            if( stall.getExpiryDate() == null ){
                continue;
            }

            Date currentDate = Date.from(Instant.now());
            if(currentDate.after(stall.getExpiryDate())){
                stall.removeOwner();
                Debug.log("Data waznosci " + regionName + " skonczyla sie");
                Debug.log("Usuwam...");
            }


        }
        Stalls.save();
    }


}
