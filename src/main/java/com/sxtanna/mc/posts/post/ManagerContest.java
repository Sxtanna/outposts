package com.sxtanna.mc.posts.post;

import com.google.common.collect.Maps;
import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.State;
import com.sxtanna.mc.posts.data.ContestStateEvent;
import com.sxtanna.mc.posts.data.OutpostClaimEvent;
import com.sxtanna.mc.posts.data.OutpostEnterEvent;
import com.sxtanna.mc.posts.data.OutpostLeaveEvent;
import com.sxtanna.mc.posts.data.OutpostResetEvent;
import com.sxtanna.mc.posts.post.base.Contest;
import com.sxtanna.mc.posts.post.base.Outpost;
import com.sxtanna.mc.posts.post.data.CaptureState;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public final class ManagerContest implements State, Listener
{

	private final Outposts plugin;

	private final Map<Outpost, Contest> postToCont = Maps.newHashMap();


	public ManagerContest(final Outposts plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public void load()
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.getHookWorldGuard().addMoveHandler(this::moved);
	}

	@Override
	public void kill()
	{
		HandlerList.unregisterAll(this);

		postToCont.clear();
	}


	@NotNull
	public Contest getContest(@NotNull final Outpost outpost)
	{
		return postToCont.computeIfAbsent(outpost, Contest::new);
	}


	@EventHandler(priority = EventPriority.MONITOR)
	public void onOutpostEnter(@NotNull final OutpostEnterEvent event)
	{
		final var post = event.getOutpost();
		final var cont = event.getContest();

		//<editor-fold desc="enter logic">
		final var faction = plugin.getHookFactionUID().getFactionUUID(event.getPlayer());
		if (faction.isEmpty())
		{
			return; // if they aren't in a faction, ignore them
		}

		// record them into the contest
		cont.enter(faction.get(), event.getPlayer());
		//</editor-fold>

		//<editor-fold desc="state logic">
		var oldState = cont.getCaptureState();
		var newState = oldState;

		switch (oldState)
		{
			case NEUTRAL:
				if (!hasPreviousAlreadyCaptured(event.getPlayer(), post))
				{
					break; // they don't have the previous outpost captured, do nothing
				}

				newState = CaptureState.CAPTURING; // begin capturing
				break;
			case CLAIMED:
				if (memberOfTheCapturedFaction(event.getPlayer(), cont))
				{
					break; // they are a part of the already claimed faction, do nothing
				}

				if (allInsideAreTheSameFaction(cont))
				{
					newState = CaptureState.UNSEATING;  // begin unseating this claim
				}
				else
				{
					newState = CaptureState.CONTESTED_UNSEATING; // unseating is being attempted, but the claimed faction is still there
				}
				break;
			case CAPTURING:
				if (allInsideAreTheSameFaction(cont))
				{
					return; // this is another member of the same faction
				}

				newState = CaptureState.CONTESTED_CAPTURING; // another faction is contesting the capture
				break;
			case DWINDLING:
				if (!factionPreviouslyCapturing(event.getPlayer(), cont))
				{
					return; // they were not capturing this outpost
				}

				newState = CaptureState.CAPTURING; // resume their capture
				break;
			case UNSEATING:
				if (!memberOfTheCapturedFaction(event.getPlayer(), cont))
				{
					break; // unseating can continue since none of the claimed faction are present
				}

				newState = CaptureState.CONTESTED_UNSEATING; // contest this outpost from being unseated
				break;
			case CONTESTED_CAPTURING:
			case CONTESTED_UNSEATING:
				// do nothing when there is an unresolved contesting state
				break;
		}

		if (oldState == newState)
		{
			return; // no change in state, do nothing
		}

		// inform listeners that the state of this contest has changed
		plugin.getServer().getPluginManager().callEvent(new ContestStateEvent(event.getOutpost(), event.getContest(), oldState, newState));
		//</editor-fold>
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onOutpostLeave(@NotNull final OutpostLeaveEvent event)
	{
		final var post = event.getOutpost();
		final var cont = event.getContest();

		//<editor-fold desc="leave logic">
		final var faction = plugin.getHookFactionUID().getFactionUUID(event.getPlayer());
		if (faction.isEmpty())
		{
			return; // if they aren't in a faction, ignore them
		}

		// remove them frp, the contest
		cont.leave(faction.get(), event.getPlayer());
		//</editor-fold>

		//<editor-fold desc="state logic">
		var oldState = cont.getCaptureState();
		var newState = oldState;

		switch (oldState)
		{
			case NEUTRAL:
				// impossible state
				break;
			case CLAIMED:
				if (cont.getInside().isEmpty())
				{
					break; // do nothing if no one is in the zone
				}

				if (cont.getPlayersInside().stream().allMatch(player -> memberOfTheCapturedFaction(player, cont)))
				{
					break; // none of the players inside are a part of a different faction
				}

				newState = CaptureState.UNSEATING; // all players from claimed faction have left, leaving other factions
				break;
			case CAPTURING:
				if (!cont.getInside().isEmpty() || cont.getCapturedUUID().isPresent())
				{
					break; // this outpost is recovering from attempted unseating
				}

				newState = CaptureState.DWINDLING; // capture was not completed, begin dwindling
				break;
			case DWINDLING:
				// impossible state
				break;
			case UNSEATING:
				if (!cont.getInside().isEmpty())
				{
					break; // do nothing if there are still people unseating
				}

				newState = CaptureState.CAPTURING; // this outpost will recapture itself
				break;
			case CONTESTED_CAPTURING:
				if (!allInsideAreTheSameFaction(cont))
				{
					break; // there are other factions contesting
				}

				if (singleFactionInsideOrNull(cont).map(cont::getLevel).orElse(-1) >= 0)
				{
					newState = CaptureState.CAPTURING; // this faction can continue capturing
				}
				else
				{
					newState = CaptureState.UNSEATING; // this faction needs to unseat the previous capture
				}
				break;
			case CONTESTED_UNSEATING:
				if (cont.getPlayersInside().stream().noneMatch(player -> memberOfTheCapturedFaction(player, cont)))
				{
					newState = CaptureState.UNSEATING; // none of the players inside are a part of the claimed faction, continue unseating
				}
				else if (singleFactionInsideOrNull(cont).map(cont::getLevel).orElse(-1) >= post.getCaptureTime())
				{
					newState = CaptureState.CLAIMED; // if the claim level never went down, just set back to claimed
				}
				else
				{
					newState = CaptureState.CAPTURING; // begin reclaiming otherwise
				}
				break;
		}

		if (oldState == newState)
		{
			return; // no change in state, do nothing
		}

		// inform listeners that the state of this contest has changed
		plugin.getServer().getPluginManager().callEvent(new ContestStateEvent(event.getOutpost(), event.getContest(), oldState, newState));
		//</editor-fold>
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onContestState(@NotNull final ContestStateEvent event)
	{
		final var post = event.getOutpost();
		final var cont = event.getContest();

		cont.setCaptureState(event.getNewState());

		final var message = String.format("\n\n%s | state change: \n  %sO: %s\n  %sN: %s", event.getOutpost().getName(), ChatColor.RED, event.getOldState(), ChatColor.GREEN, event.getNewState());
		plugin.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(message));

		BukkitTask update = null;

		switch (event.getNewState())
		{
			case NEUTRAL:
				cont.getLevels().clear();
				cont.setCapturedUUID(null);

				if (!cont.getInside().isEmpty() && allInsideAreTheSameFaction(cont))
				{
					later(() ->
						  {
							  plugin.getServer().getPluginManager().callEvent(new ContestStateEvent(event.getOutpost(), event.getContest(), event.getContest().getCaptureState(), CaptureState.CAPTURING));
						  });
				}
				break;
			case CLAIMED:
				final var faction = singleFactionInsideOrNull(cont);
				if (faction.isEmpty())
				{
					break; // impossible state
				}

				if (cont.getCapturedUUID().equals(faction))
				{
					break; // do nothing, this is a claim recovery
				}

				plugin.getServer().broadcastMessage(String.format("outpost %s has been claimed by %s", event.getOutpost().getName(), faction.orElse("unknown")));

				cont.setCapturedUUID(faction.get());
				break;
			case CAPTURING:
				update = timer(20L, () ->
				{
					final var newLevel = singleFactionInsideOrNull(cont).map(cont::addLevel).orElse(-1);

					if (newLevel >= post.getCaptureTime())
					{
						plugin.getServer().getPluginManager().callEvent(new ContestStateEvent(event.getOutpost(), event.getContest(), event.getContest().getCaptureState(), CaptureState.CLAIMED));

						final var uuid = cont.getCapturedUUID();
						final var name = uuid.flatMap(plugin.getHookFactionUID()::getFactionName);

						if (uuid.isEmpty() || name.isEmpty())
						{
							return; // impossible state?
						}

						plugin.getServer().getPluginManager().callEvent(new OutpostClaimEvent(event.getOutpost(), event.getContest(), name.get(), uuid.get()));
					}
				});
				break;
			case DWINDLING:
			case UNSEATING:
				update = timer(20L, () ->
				{
					final var newLevel = singleFactionLevelsOrNull(cont).or(cont::getCapturedUUID).map(cont::subLevel).orElse(-1);

					if (newLevel <= 0)
					{
						plugin.getServer().getPluginManager().callEvent(new ContestStateEvent(event.getOutpost(), event.getContest(), event.getContest().getCaptureState(), CaptureState.NEUTRAL));
						plugin.getServer().getPluginManager().callEvent(new OutpostResetEvent(event.getOutpost(), event.getContest()));
					}
				});
				break;
			case CONTESTED_CAPTURING:
			case CONTESTED_UNSEATING:
				// do nothing
				break;
		}

		cont.swapUpdatingTask(update).ifPresent(BukkitTask::cancel);
	}


	private void moved(@NotNull Player player, @NotNull World world, @NotNull Collection<String> fromZones, @NotNull Collection<String> intoZones)
	{
		Outpost fromOutpost = null;
		Outpost intoOutpost = null;


		// handle leaving an outpost
		for (final String zone : fromZones)
		{
			fromOutpost = plugin.getManagerOutpost().getByZone(zone).orElse(null);

			if (fromOutpost != null)
			{
				break;
			}
		}

		if (fromOutpost != null)
		{
			plugin.getServer().getPluginManager().callEvent(new OutpostLeaveEvent(fromOutpost, getContest(fromOutpost), player));
		}


		// handle entering an outpost
		for (final String zone : intoZones)
		{
			intoOutpost = plugin.getManagerOutpost().getByZone(zone).orElse(null);

			if (intoOutpost != null)
			{
				break;
			}
		}

		if (intoOutpost != null)
		{
			plugin.getServer().getPluginManager().callEvent(new OutpostEnterEvent(intoOutpost, getContest(intoOutpost), player));
		}
	}


	private Optional<String> singleFactionInsideOrNull(@NotNull final Contest cont)
	{
		if (cont.getInside().size() != 1)
		{
			return Optional.empty();
		}

		return cont.getInside().keySet().stream().findFirst();
	}

	private Optional<String> singleFactionLevelsOrNull(@NotNull final Contest cont)
	{
		if (cont.getLevels().size() != 1)
		{
			return Optional.empty();
		}

		return cont.getLevels().keySet().stream().findFirst();
	}


	private boolean allInsideAreTheSameFaction(@NotNull final Contest cont)
	{
		return cont.getInside().size() == 1;
	}

	private boolean memberOfTheCapturedFaction(@NotNull final Player player, @NotNull final Contest cont)
	{
		final var uuid = cont.getCapturedUUID();
		final var fact = plugin.getHookFactionUID().getFactionUUID(player);

		return fact.isPresent() && uuid.isPresent() && fact.equals(uuid);
	}

	private boolean factionPreviouslyCapturing(@NotNull final Player player, @NotNull final Contest cont)
	{
		final var fact = plugin.getHookFactionUID().getFactionUUID(player);
		if (fact.isEmpty())
		{
			return false;
		}

		return cont.getLevels().size() == 1 && cont.getLevels().getOrDefault(fact.get(), -1) > 0;
	}

	private boolean hasPreviousAlreadyCaptured(@NotNull final Player player, @NotNull final Outpost post)
	{
		final var prev = post.getCapturePrev();
		if (prev == null)
		{
			return true; // just true if there is no previous requirement
		}

		final var cont = getContest(prev);
		if (cont.getCaptureState() != CaptureState.CLAIMED)
		{
			return false; // previous outpost isn't claimed
		}

		return memberOfTheCapturedFaction(player, cont);
	}


	private BukkitTask later(@NotNull Runnable runnable)
	{
		return plugin.getServer().getScheduler().runTask(plugin, runnable);
	}

	private BukkitTask timer(long period, @NotNull final Runnable runnable)
	{
		return plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, 0L, period);
	}

}
