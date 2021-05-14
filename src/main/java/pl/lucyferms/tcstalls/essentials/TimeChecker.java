package pl.lucyferms.tcstalls.essentials;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import pl.lucyferms.tcstalls.TcStalls;
import pl.lucyferms.tcstalls.langPL.Messages;
import pl.lucyferms.tcstalls.stall.Stalls;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class TimeChecker {




    public static void timer(){

        Date startDate = Date.from(Instant.now());
        int minutesToFullHour = 61-startDate.getMinutes();
        long ticksToFullHour = 20L * 60 * minutesToFullHour;
        new BukkitRunnable(){

            @Override
            public void run(){
                Stalls.checkStalls();
            }


        }.runTaskTimer(TcStalls.getInstance(), ticksToFullHour, (20 * 60 * 60) );


    }

}
