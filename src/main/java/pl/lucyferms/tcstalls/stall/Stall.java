package pl.lucyferms.tcstalls.stall;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.lucyferms.tcstalls.TcStalls;
import pl.lucyferms.tcstalls.data.Storage;
import pl.lucyferms.tcstalls.essentials.ChatUtils;
import pl.lucyferms.tcstalls.langPL.Messages;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class Stall {

    private String owner;
    private Date buyDate;
    private Date expiryDate;
    private Double price;
    private String regionName;


    public Stall(String owner, Date buyDate, Date expiryDate, Double price, String regionName) {
        this.owner = owner;
        this.buyDate = buyDate;
        this.expiryDate = expiryDate;
        this.price = price;
        this.regionName = regionName;
    }
    public boolean isOccupied(){
        return !owner.equalsIgnoreCase("" + Messages.toSale);
    }

    public boolean canBuy(Player player){
        return TcStalls.getEcon().getBalance(player) >= price;
    }

    public void buy(String player){
        this.owner = player;
    }

    public void removeOwner(){
        this.owner = "" + Messages.toSale;
        this.buyDate = null;
        this.expiryDate = null;
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Objects.requireNonNull(Bukkit.getWorld("" + Storage.worldName))));
        ProtectedRegion region = regionManager.getRegion("" + this.regionName);
        region.getOwners().clear();
        region.getFlags().clear();
        region.setFlag(Flags.GREET_TITLE, "" + ChatUtils.fixColor("" + Messages.notOccupiedGreetingOwner.replace("%owner%", this.owner) + "\n" + Messages.notOccupiedGreetingRegionName.replace("%regionName%", this.regionName)));
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
