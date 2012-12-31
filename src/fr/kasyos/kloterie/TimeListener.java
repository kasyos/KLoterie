package fr.kasyos.kloterie;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class TimeListener implements Listener
{

	private MainKloterie plugin;

	public TimeListener(MainKloterie plugin)
	{
		this.plugin = plugin;
	}

	public void onDayGo()
	{			     
	
		if(plugin.getConfig().getBoolean("ActiverLoterie"))
		{
		
		plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable(){

			@Override
			public void run()
			{
				new Thread(){
					public void run(){
						
						int prixticket = plugin.getConfig().getInt("PrixTicket");

						plugin.getServer().broadcastMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.YELLOW + "La Loterie commence ! Pour acheter un ticket : " + ChatColor.AQUA + "/kloterie");
						plugin.getServer().broadcastMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.YELLOW + "Soyez nombreux à participer ! Prix du ticket : " + ChatColor.AQUA + prixticket + " $" );

						plugin.loterieHasStarted = true;
						plugin.loterienotstarted = true;


						try {
							Thread.sleep(10000);
							plugin.getServer().broadcastMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.YELLOW + "Tirage dans 2 minutes ! Votre ticket : " + ChatColor.AQUA + "/kloterie | " + ChatColor.AQUA + "Prix du ticket : " + prixticket + " $");
							Thread.sleep(120000);



						} catch (InterruptedException e) {

							e.printStackTrace();
						}




						if(plugin.loterie.size() >= 1)
						{
							plugin.getServer().broadcastMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.YELLOW + "Lancement du tirage au sort !");


							int montantlot = plugin.loterie.size() * prixticket;

							plugin.getServer().broadcastMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.GREEN + "Montant du lot : " + ChatColor.AQUA + montantlot + " $");

							int size = plugin.loterie.size();
							Random random = new Random();
							int i = random.nextInt(size);

							Player playerwin = plugin.loterie.get(i);
							String playerwinname = playerwin.getName();

							plugin.getServer().broadcastMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.GREEN + "Le vainqueur de la loterie est " + ChatColor.AQUA + playerwinname + ChatColor.GREEN + " Bravo à lui ! " + ChatColor.AQUA + "( lot : " + montantlot + " $ )");

							double lot = new Integer(montantlot).doubleValue();
							EconManager.economy.bankDeposit(playerwinname, lot);

							playerwin.sendMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.YELLOW + "Bravo voici votre lot : " + ChatColor.AQUA + lot + " $");

							Double solde = EconManager.economy.getBalance(playerwinname);

							playerwin.sendMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.YELLOW + "Montant de votre compte : " + ChatColor.AQUA + solde + " $");
							
						    
						}
						else
						{
							plugin.getServer().broadcastMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.RED + "Aucun participant ! La Loterie est annuler pour cette fois ...");  
						}

						TimeListener.this.plugin.loterie.clear();
						TimeListener.this.plugin.loterieHasStarted = false;
						TimeListener.this.plugin.loterienotstarted = false;

					}
				}.start();
			}
		}, 0L, 12000L);
		
		}

        


	}

	@EventHandler
	public void OnPlayerQuit(PlayerQuitEvent event)
	{
		if(plugin.loterie.contains(event.getPlayer()))
		{
			plugin.loterie.remove(event.getPlayer());
		}
	}
}