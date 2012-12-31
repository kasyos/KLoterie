package fr.kasyos.kloterie;


import fr.kasyos.kloterie.MainKloterie;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandeKloterie implements CommandExecutor
{

	private MainKloterie plugin;

	public CommandeKloterie(MainKloterie plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,String[] args)
	{


            

			Player player = (Player)sender;

			if(!plugin.loterieHasStarted)
			{
				player.sendMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.RED + "Pas de loterie en cours !");
				return false;
			}

			if(plugin.loterie.contains(player))
			{
				player.sendMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.RED + "Un seul ticket par personne !");
				return false;
			}

			plugin.loterie.add(player);

			int prixticket = plugin.getConfig().getInt("PrixTicket");
			int montantlot = plugin.loterie.size() * prixticket;

			if(plugin.loterie.size() > 1)
			{
				plugin.broadcastToLoterie(ChatColor.AQUA + "[KLoterie] " + ChatColor.GREEN + "Un nouveau ticket acheter ! Nouveau montant du lot : " + ChatColor.AQUA + montantlot); 
			}
			
			Double solde = EconManager.economy.getBalance(player.getName());
			
			if(solde <= prixticket)
			{
				player.sendMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.RED + "Vous n'avez pas assez d'argent dans votre compte !" + ChatColor.AQUA + " Votre compte : " + solde + " $");
				return false;
			}


			player.sendMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.GREEN + "Votre participation à la loterie est confirmer !");
			player.sendMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.GREEN + "Prix de votre ticket : " + ChatColor.AQUA + prixticket + " $");
			player.sendMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.GREEN + "Montant du lot pour le moment : " + ChatColor.AQUA + montantlot + " $");
			EconManager.economy.bankWithdraw(player.getName(), prixticket);
			player.sendMessage(ChatColor.AQUA + "[KLoterie] " + ChatColor.GREEN + "Montant de votre compte : " + ChatColor.AQUA + solde + " $");
			

			




		



		return true;
	}


}

