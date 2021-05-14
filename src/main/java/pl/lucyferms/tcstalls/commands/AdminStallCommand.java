package pl.lucyferms.tcstalls.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.lucyferms.tcstalls.data.Storage;
import pl.lucyferms.tcstalls.essentials.ChatUtils;
import pl.lucyferms.tcstalls.essentials.Utils;
import pl.lucyferms.tcstalls.langPL.Messages;
import pl.lucyferms.tcstalls.stall.Stall;
import pl.lucyferms.tcstalls.stall.Stalls;

import java.util.Date;
import java.util.Objects;

public class AdminStallCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if( !sender.hasPermission("TcStalls.command.admin")){
            ChatUtils.sendMessage(sender, "&cBrak uprawnień");
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
            String regionName = args[1];
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(Objects.requireNonNull(Bukkit.getWorld("" + Storage.worldName))));
            if (!regionManager.hasRegion(regionName)) {
                ChatUtils.sendMessage(sender, "" + Messages.thisRegionDoesntExists);
                return true;
            }
            if (Stalls.getStalls().containsKey(regionName)) {
                ChatUtils.sendMessage(sender, "" + Messages.thisRegionIsAlreadyInUsage);
                return true;
            }
            if (Utils.isInteger(args[2])) {

                String owner = "" + Messages.toSale;

                Date buyDate = null;
                Date expiryDate = null;

                ProtectedRegion region = regionManager.getRegion("" + regionName);
                region.getFlags().clear();
                region.setFlag(Flags.GREET_TITLE, "" + ChatUtils.fixColor("" + Messages.notOccupiedGreetingOwner.replace("%owner%", owner) + "\n" + Messages.notOccupiedGreetingRegionName.replace("%regionName%", regionName)));
                region.setPriority(10);

                Double price = Double.parseDouble(args[2]);

                Stalls.addStall(new Stall(owner, buyDate, expiryDate, price, regionName));
                ChatUtils.sendMessage(sender, "" + Messages.stallCreated.replace("%regionName%", regionName));
                Stalls.save();
                return true;
            }
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("check")) {
            Stalls.checkStalls();
            ChatUtils.sendMessage(sender, "&a&lSprawdzam");
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("show")) {
            Stalls.showStalls(label, sender, 1);
            return true;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("show")) {
            Stalls.showStall(sender, args[1]);
            return true;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            if( Stalls.deleteStall(args[1]) ){
                ChatUtils.sendMessage(sender, "" + Messages.stallDeleted.replace("%regionName", args[1]) );
                Stalls.save();
            }
            else{
                ChatUtils.sendMessage(sender, "" + Messages.thisStallDoesntExists);
            }
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                Messages.loadMessages();
                Storage.loadConfig();
                Stalls.load();
                Stalls.checkStalls();
                ChatUtils.sendMessage(sender, "" + Messages.reloaded);
                return true;
            }
            else if (args[0].equalsIgnoreCase("save")) {
                Stalls.save();
                ChatUtils.sendMessage(sender, "" + Messages.saved);
                return true;
            }
        }
        ChatUtils.sendMessage(sender, "/atc create <region> <cena>");
        ChatUtils.sendMessage(sender, "/atc show [id]");
        ChatUtils.sendMessage(sender, "/atc reload");
        ChatUtils.sendMessage(sender, "/atc save");





        return true;
    }
}
