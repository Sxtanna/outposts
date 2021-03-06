package com.sxtanna.mc.posts.hook;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.integration.Econ;
import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.State;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;

public final class HookFactionUID implements State
{

	@NotNull
	private final Outposts                plugin;
	private       WeakReference<Factions> hooked;


	public HookFactionUID(@NotNull final Outposts plugin)
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
		catch (final Throwable ex)
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

	public Optional<String> getFactionName(@NotNull final Player player)
	{
		final var hooked = this.hooked.get();
		if (hooked == null)
		{
			return Optional.empty();
		}

		final var faction = FPlayers.getInstance().getByPlayer(player).getFaction();

		if (!faction.isNormal())
		{
			return Optional.empty();
		}

		return Optional.of(faction.getTag());
	}

	public Optional<String> getFactionName(@NotNull final String factionUUID)
	{
		final var hooked = this.hooked.get();
		if (hooked == null)
		{
			return Optional.empty();
		}

		return Optional.ofNullable(Factions.getInstance().getFactionById(factionUUID).getTag());
	}

	public Optional<String> getFactionUUID(@NotNull final Player player)
	{
		final var hooked = this.hooked.get();
		if (hooked == null)
		{
			return Optional.empty();
		}

		final var faction = FPlayers.getInstance().getByPlayer(player).getFaction();

		if (!faction.isNormal())
		{
			return Optional.empty();
		}

		return Optional.of(faction.getId());
	}

	@NotNull
	public Collection<Player> getPlayersInFaction(@NotNull final String factionUUID)
	{
		final var hooked = this.hooked.get();
		if (hooked == null)
		{
			return Collections.emptyList();
		}

		final var faction = Factions.getInstance().getFactionById(factionUUID);
		if (faction == null || !faction.isNormal())
		{
			return Collections.emptyList();
		}

		return faction.getOnlinePlayers();
	}

}