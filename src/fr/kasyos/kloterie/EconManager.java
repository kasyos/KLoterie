package fr.kasyos.kloterie;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class EconManager 
{
	
	public static Plugin plugin = null;
    public static Economy economy = null;
    
    public static void load(Plugin p)
    {
    	plugin = p;
    }
    

    public static boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

}
