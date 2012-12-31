package fr.kasyos.kloterie;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;



public class MainKloterie extends JavaPlugin
{
	
	ArrayList<Player> loterie = new ArrayList<Player>(); 
	public boolean loterieHasStarted; 
	public boolean loterienotstarted; 
	
	public Logger log;
	
	public CommandeKloterie kloterie;
	
	public TimeListener timeListener;
	
	
    @Override
	public void onEnable()
    {
    	timeListener = new TimeListener(this);
    	EconManager.load(this);
    	EconManager.setupEconomy();
    	
    	this.getConfig().options().copyDefaults(true);
    	this.saveConfig();
    	
    	this.kloterie = new CommandeKloterie(this);
    	
    	
    	this.log = this.getLogger();
    	
    	getCommand("kloterie").setExecutor(kloterie);
    	
    	
        this.getServer().getPluginManager().registerEvents(new SignsListener(this), this);
        this.getServer().getPluginManager().registerEvents(timeListener, this);
        
        
        timeListener.onDayGo();
    	
    	
    	
    	String infos = this.getDescription().getName() + 
    			" version "+ this.getDescription().getVersion() + 
    			" ecrit par " + this.getDescription().getAuthors().toString() +
    			" en cours d'activation !";
    	
    	log.info(infos);
    	log.warning("Vault doit être installer sur votre serveur !");

    	
    	if (this.getConfig().getBoolean("MessagedeDepart.Activer"))
    	{
    		String msg = this.getConfig().getString("MessagedeDepart.Message");
    		log.info(msg);
    	}
    }
    
    
    @Override
    public void onDisable()
    {
    	if (this.getConfig().getBoolean("MessageArret.Activer"))
    	{
    		String msg = this.getConfig().getString("MessageArret.Message");
    		log.info(msg);
    	}
    }
    
    public void broadcastToLoterie(String msg){
    	for(int i = 0;i<this.loterie.size();i++){
    		this.loterie.get(i).sendMessage(msg);
    	}
    }
    
    
}
