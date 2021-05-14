package pl.lucyferms.tcstalls.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pl.lucyferms.tcstalls.essentials.ChatUtils;
import pl.lucyferms.tcstalls.essentials.Debug;
import pl.lucyferms.tcstalls.essentials.Utils;
import pl.lucyferms.tcstalls.langPL.Messages;
import pl.lucyferms.tcstalls.stall.Stall;
import pl.lucyferms.tcstalls.stall.Stalls;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StallCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {




        if( args.length == 1 ){
            if( args[0].equalsIgnoreCase("lista")){
                Stalls.showStalls(label, sender, 1);
                return true;
            }
        }

        if(sender instanceof CommandExecutor){
            Debug.sendError("" + Messages.commandOnlyForPlayers);
            return true;
        }

        Player player = (Player) sender;

        if( args.length == 2 ){
            if( args[0].equalsIgnoreCase("lista")){
                if(Utils.isInteger(args[1])){
                    Stalls.showStalls(label, sender, Integer.parseInt(args[1]));
                }
                else{
                    Stalls.showStalls(label, sender, 1);
                }

                return true;
            }

            String regionName = args[1];
            if( !Stalls.doesStallExists(regionName)){
                ChatUtils.sendMessage(sender, "" + Messages.thisStallDoesntExists);
                return true;
            }
            if( args[0].equalsIgnoreCase("zajmij") ){
                Stalls.claimStall(player, regionName);
                return true;
            }
            if( args[0].equalsIgnoreCase("sprawdz")){
                Stalls.showStall(sender, regionName);
                return true;
            }
            if( args[0].equalsIgnoreCase("przedluz")){
                Stalls.extendClaimTime(player, regionName);
                return true;
            }
            if( args[0].equalsIgnoreCase("porzuc")){
                if(Stalls.isPlayerOwnerOfThisStall(player, regionName) ){
                    Stalls.abandonStall(regionName);
                    ChatUtils.sendMessage(player, "" + Messages.stallAbandoned.replace("%regionName%", regionName));
                    Stalls.save();
                }
                else{
                    ChatUtils.sendMessage(player, "" + Messages.youAreNotOwnerOfThisStall);
                }
                return true;
            }
//            if( args[0].equalsIgnoreCase("test") ){
//                if(Stalls.isPlayerOwnerOfThisStall(player, regionName) ){
//                    SimpleDateFormat format = new SimpleDateFormat("" + Messages.dateFormat);
//                    Stall stall = Stalls.getStall(regionName);
//                    Date date = Date.from(Instant.now());
//                    date.setMinutes(date.getMinutes() + 1);
//                    stall.setExpiryDate(date);
//                    ChatUtils.sendMessage(player, "Nowa data waznosci: " + format.format(date));
//                }
//                else{
//                    ChatUtils.sendMessage(player, "" + Messages.youAreNotOwnerOfThisStall);
//                }
//                return true;
//            }
        }
        ChatUtils.sendMessage(sender, "" + Messages.infoAboutStalls);
        sender.sendMessage("");
        ChatUtils.sendMessage(sender, "/" + label +" lista [strona]");
        ChatUtils.sendMessage(sender, "/" + label +" zajmij <nazwa straganu>");
        ChatUtils.sendMessage(sender, "/" + label +" sprawdz <nazwa straganu>");
        ChatUtils.sendMessage(sender, "/" + label +" porzuc <nazwa straganu>");
        ChatUtils.sendMessage(sender, "/" + label +" przedluz <nazwa straganu>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            ArrayList<String> list = new ArrayList<>();
            ArrayList<String> result = new ArrayList<>();

            list.add("lista");
            list.add("zajmij");
            list.add("sprawdz");
            list.add("porzuc");
            list.add("przedluz");
            for( String alias : list) {
                if (alias.toLowerCase().startsWith(args[0].toLowerCase()) ) {
                    result.add(alias);
                }
            }
            return result;
        }
        if( args.length == 2 && (!args[0].equalsIgnoreCase("lista")) ){
            ArrayList<String> result = new ArrayList<>();

            ArrayList<String> list = new ArrayList<>(Stalls.getStalls().keySet());
            for( String alias : list) {
                if (alias.toLowerCase().startsWith(args[1].toLowerCase()) ) {
                    result.add(alias);
                }
            }
            return result;
        }

        return null;
    }
}
