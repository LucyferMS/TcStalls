package pl.lucyferms.tcstalls.langPL;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.lucyferms.tcstalls.essentials.ConfigUtils;

public class Messages {

    public static String toSale = "Do wynajecia";
    public static String thisRegionDoesntExists = "Taki region nie istnieje";
    public static String thisStallDoesntExists = "Stragan o takiej nazwie nie istnieje";
    public static String thisRegionIsAlreadyInUsage = "Stragan na tym regionie juz istnieje";
    public static String occupiedGreetingOwner = "&a&l%owner%";
    public static String occupiedGreetingRegionName = "&2&l%regionName%";
    public static String notOccupiedGreetingOwner = "&b&l%owner%";
    public static String notOccupiedGreetingRegionName = "&3&l%regionName%";
    public static String stallCreated = "Dodano stragan o nazwie %regionName%";
    public static String stallDeleted = "Usunieto stragan %regionName%";
    public static String reloaded = "Przeladowano";
    public static String saved = "Zapisano";
    public static String commandOnlyForPlayers = "Komenda tylko dla graczy";
    public static String stallAbandoned = "Porzucono stragan %regionName%";
    public static String youAreNotOwnerOfThisStall = "Nie jestes wlascicielem tego straganu";
    public static String noStalls = "Brak straganow";
    public static String notApplicable = "nd.";
    public static String separatorHeader = "=======================";
    public static String separatorFooter = "=======================";
    public static String name = "Nazwa: %regionName%";
    public static String owner = "Wlasciciel: %owner%";
    public static String buyDate = "Data zakupu: %date%";
    public static String expiryDate = "Data wygasniecia: %date%";
    public static String price = "Cena: %price%";
    public static String listHeader = "==========Stragany==========";
    public static String showStallInfoHoverMessage = "&aNacisnij zeby zobaczyc wiecej informacji";
    public static String claimStallHoverMessage = "&aNacisnij zeby wynajac &2%regionName%";
    public static String listOneElementFormat = "&3%regionName% &7(%owner%)&f";
    public static String timeRange = "&a%buyDate% - %expiryDate%";
    public static String listFooter = "&a<< &2Strona %page% z %maxpage% &a>>";
    public static String previousPage = "<<";
    public static String nextPage = ">>";
    public static String helpPageHoverMessage = "Przejdź do strony %page%";
    public static String noNextPage = "To jest ostatnia strona";
    public static String noPreviousPage = "To jest pierwsza strona";
    public static String thisStallIsOccupied = "Ten stragan jest juz zajety";
    public static String noMoney = "Nie stac Cie na to stanowisko";
    public static String youReachedLimitOfStalls = "Osiegnieto limit straganow";
    public static String stallPurchased = "Zakupiono %regionName%";
    public static String dateFormat = "yyyy-MM-dd, HH:mm";
    public static String thisStaleDoesntHaveAnOwner = "Ten stragan nie jest zajety";
    public static String expiryTimeCantBeGreaterThan = "Data waznosci straganu nie moze przekraczac %days% dni";
    public static String currentExpiryDate = "Obecna data waznosci: %date%";
    public static String expiryDateExtendedTo = "Przedluzono date waznosci straganu do %date%";
    public static String infoAboutStalls = "Stragany wynajmujesz i przedluzasz o %buyDays% dni, lecz data waznosci nie moze siegac dalej niz %maxDays% dni\nJesli data waznosci straganu minie, traci on wlasciciela dzieki czemu inna osoba moze go wynajac razem z zawartoscia\nW straganach mozesz postawic sklepy skrzynkowe, wzor ich tworzenia znajdziesz pod /warp wzor";


    public static void loadMessages(){
        YamlConfiguration messages = ConfigUtils.load("lang-pl.yml");

        toSale = messages.getString("messages.toSale");
        thisRegionDoesntExists = messages.getString("messages.thisRegionDoesntExists");
        thisStallDoesntExists = messages.getString("messages.thisStallDoesntExists");
        thisRegionIsAlreadyInUsage = messages.getString("messages.thisRegionIsAlreadyInUsage");
        occupiedGreetingOwner = messages.getString("messages.occupiedGreetingOwner");
        occupiedGreetingRegionName = messages.getString("messages.occupiedGreetingRegionName");
        notOccupiedGreetingOwner = messages.getString("messages.notOccupiedGreetingOwner");
        notOccupiedGreetingRegionName = messages.getString("messages.notOccupiedGreetingRegionName");
        stallCreated = messages.getString("messages.stallCreated");
        stallDeleted = messages.getString("messages.stallDeleted");
        reloaded = messages.getString("messages.reloaded");
        saved = messages.getString("messages.saved");
        commandOnlyForPlayers = messages.getString("messages.commandOnlyForPlayers");
        stallAbandoned = messages.getString("messages.stallAbandoned");
        youAreNotOwnerOfThisStall = messages.getString("messages.youAreNotOwnerOfThisStall");
        noStalls = messages.getString("messages.noStalls");
        notApplicable = messages.getString("messages.notApplicable");
        separatorHeader = messages.getString("messages.separatorHeader");
        separatorFooter = messages.getString("messages.separatorFooter");

        name = messages.getString("messages.info.name");
        owner = messages.getString("messages.info.owner");
        buyDate = messages.getString("messages.info.buyDate");
        expiryDate = messages.getString("messages.info.expiryDate");
        price = messages.getString("messages.info.price");

        listHeader = messages.getString("messages.listHeader");
        showStallInfoHoverMessage = messages.getString("messages.showStallInfoHoverMessage");
        claimStallHoverMessage = messages.getString("messages.claimStallHoverMessage");
        listOneElementFormat = messages.getString("messages.listOneElementFormat");
        timeRange = messages.getString("messages.timeRange");
        listFooter = messages.getString("messages.listFooter");
        previousPage = messages.getString("messages.previousPage");
        nextPage = messages.getString("messages.nextPage");
        helpPageHoverMessage = messages.getString("messages.helpPageHoverMessage");
        noNextPage = messages.getString("messages.noNextPage");
        noPreviousPage = messages.getString("messages.noPreviousPage");
        thisStallIsOccupied = messages.getString("messages.thisStallIsOccupied");
        noMoney = messages.getString("messages.noMoney");
        youReachedLimitOfStalls = messages.getString("messages.youReachedLimitOfStalls");
        stallPurchased = messages.getString("messages.stallPurchased");
        dateFormat = messages.getString("messages.dateFormat");
        thisStaleDoesntHaveAnOwner = messages.getString("messages.thisStaleDoesntHaveAnOwner");
        expiryTimeCantBeGreaterThan = messages.getString("messages.expiryTimeCantBeGreaterThan");
        currentExpiryDate = messages.getString("messages.currentExpiryDate");
        expiryDateExtendedTo = messages.getString("messages.expiryDateExtendedTo");
        infoAboutStalls = messages.getString("messages.infoAboutStalls");



    }

}
