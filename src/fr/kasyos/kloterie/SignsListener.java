package fr.kasyos.kloterie;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SignsListener implements Listener 
{
	
	private MainKloterie plugin;


	public SignsListener(MainKloterie plugin)
    {
      this.plugin = plugin;
    }
	
    @EventHandler
    public void onSignChange(SignChangeEvent event)
    {

    	String[] lines = event.getLines(); 
        
        if(lines[0].contains("[KLoterie]") || lines[0].contains("[kloterie]"))
        {
    	if(event.getPlayer().hasPermission("kloterie.create"))
    	{
        	event.setLine(0, ChatColor.AQUA + "[KLoterie]");
        	
        	if(lines[2].equals("e") || lines[2].equals("i"))
             {
               event.getPlayer().sendMessage(ChatColor.GREEN + "[KLoterie] La loterie est désormais fonctionnelle !");
             }
        	else
        	 {
        		event.getBlock().breakNaturally();
        		event.getPlayer().sendMessage(ChatColor.RED + "[KLoterie] Mauvais formatage du panneau !");
        		event.getPlayer().sendMessage(ChatColor.RED + "----------------");
        		event.getPlayer().sendMessage(ChatColor.RED + "ligne 1 : [KLoterie]");
        		event.getPlayer().sendMessage(ChatColor.RED + "ligne 2 : prix");
        		event.getPlayer().sendMessage(ChatColor.RED + "ligne 3 : e/i");
        		event.getPlayer().sendMessage(ChatColor.RED + "ligne 4 : montant/ID");
        		event.getPlayer().sendMessage(ChatColor.RED + "----------------");
        	}
    	    }
             else
             {
             	event.getPlayer().sendMessage(ChatColor.RED + "[KLoterie] Vous n'avez pas la permission !");
        	    event.getBlock().breakNaturally();
             }

        }


    }


    @EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
    {
    	if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
    	{
    		if (((event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN)))
    		{
    			Player player = event.getPlayer();
    					
    				Sign sign = (Sign)event.getClickedBlock().getState();
    				String[] lines = sign.getLines();
    				
    				if (lines[0].contains("[KLoterie]"))
    				{
    			    	if(event.getPlayer().hasPermission("kloterie.use"))
    			    	{
    			    		Double solde = EconManager.economy.getBalance(player.getName());
        					double buy = Double.parseDouble(sign.getLine(1));
    			    		
        				  if (solde > buy)
        				   {
        					
                        
    					EconManager.economy.bankWithdraw(player.getName(),buy); 
    					
    					Random random = new Random();
    					int i = random.nextInt(26);
    				    if(i==0)
    				    {
        					if (lines[2].equals("e")) 
        					{
        						
        						double lot = Double.parseDouble(sign.getLine(3));
            					EconManager.economy.bankDeposit(player.getName(),lot);
            					
            					player.sendMessage(ChatColor.GREEN + "Bravo ! Vous avez gagner " + ChatColor.YELLOW + lot + " $");
            					player.updateInventory();
            					player.getWorld().playEffect(player.getLocation(), Effect.POTION_BREAK, 3);
		
        					}
        					if (lines[2].equals("i"))
        					{
            					Integer item = Integer.parseInt(sign.getLine(3));
            					event.getPlayer().getInventory().addItem(new ItemStack(item, 1));
            					
            					player.sendMessage(ChatColor.GREEN + "Bravo ! Vous avez gagner l'ID : " + ChatColor.YELLOW + item);
            					player.updateInventory();
            					player.getWorld().playEffect(player.getLocation(), Effect.POTION_BREAK, 3);
        					}

    				    }
    				    if(i!=0)
    					{
    						player.sendMessage(ChatColor.RED + "Désolé .... Vous avez perdu ! Try again ?");
    					}
        				   }
        				  else
        				  {
        					  event.getPlayer().sendMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.RED + "Vous n'avez pas assez d'argent !");
       			    	   event.getPlayer().sendMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.RED + "Vous disposez de : " + ChatColor.YELLOW + solde + ChatColor.RED + " $");
        				  }
    			    	}
    			    	else
    			    	{
    			    		event.getPlayer().sendMessage(ChatColor.RED + "[KLoterie] Vous n'avez pas la permission ...");
    			    	}
    				    
    				

     	            }
    		}
    	}
    }
	
}