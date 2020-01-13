package com.sxtanna.mc.posts.hook;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.integration.Econ;
import com.sxtanna.mc.posts.base.Hooks;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.logging.Level;

public final class FactionUIDHook implements Hooks
{

	private final Plugin                  plugin;
	private       WeakReference<Factions> hooked;


	public FactionUIDHook(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public void load()
	{
		try
		{
			hooked = new WeakReference<>(Factions.getInstance());
		}
		catch (final NoClassDefFoundError ex)
		{
			hooked = new WeakReference<>(null);

			plugin.getLogger().log(Level.WARNING, "failed to load factions hook", ex);
		}
	}

	@Override
	public void kill()
	{
		if (hooked == null)
		{
			return;
		}


		hooked.clear();
		hooked = null;
	}


	public void addFactionPoints(@NotNull final Player player, final double amount)
	{
		final var hooked = this.hooked.get();
		if (hooked == null)
		{
			return;
		}

		final var fPlayer = FPlayers.getInstance().getByPlayer(player);

		if (!fPlayer.getFaction().isNormal())
		{
			return;
		}

		Econ.deposit(fPlayer.getFaction().getAccountId(), amount);
	}

	public boolean inTheSameFaction(@NotNull final Player thisPlayer, @NotNull final Player thatPlayer)
	{
		final var hooked = this.hooked.get();
		if (hooked == null)
		{
			return false;
		}

		final var thisFPlayer = FPlayers.getInstance().getByPlayer(thisPlayer);
		final var thatFPlayer = FPlayers.getInstance().getByPlayer(thatPlayer);

		if (!thisFPlayer.getFaction().isNormal() || !thatFPlayer.getFaction().isNormal())
		{
			return false;
		}

		return thisFPlayer.getFaction().getId().equals(thatFPlayer.getFaction().getId());
	}

}
