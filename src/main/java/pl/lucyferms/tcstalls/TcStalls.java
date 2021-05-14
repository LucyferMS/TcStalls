package pl.lucyferms.tcstalls;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import pl.lucyferms.tcstalls.commands.AdminStallCommand;
import pl.lucyferms.tcstalls.commands.StallCommand;
import pl.lucyferms.tcstalls.data.Storage;
import pl.lucyferms.tcstalls.essentials.Debug;
import pl.lucyferms.tcstalls.essentials.TimeChecker;
import pl.lucyferms.tcstalls.langPL.Messages;
import pl.lucyferms.tcstalls.stall.Stalls;

import java.util.logging.Logger;

public final class TcStalls extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private static TcStalls instance;



    public void onEnable() {
        instance = this;
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("stall").setExecutor(new StallCommand());
        getCommand("astall").setExecutor(new AdminStallCommand());
        Messages.loadMessages();
        Storage.loadConfig();
        Stalls.load();
        TimeChecker.timer();
        Stalls.checkStalls();
    }

    @Override
    public void onDisable() {
        Stalls.save();
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            Debug.log("Vault == null");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Debug.log("rsp == null");
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon(){
        return econ;
    }
    public static TcStalls getInstance(){
        return instance;
    }
}
