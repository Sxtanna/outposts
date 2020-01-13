package com.sxtanna.mc.posts.post;

import com.google.common.collect.Maps;
import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.State;
import com.sxtanna.mc.posts.data.OutpostEnterEvent;
import com.sxtanna.mc.posts.data.OutpostLeaveEvent;
import com.sxtanna.mc.posts.post.base.Contest;
import com.sxtanna.mc.posts.post.base.Outpost;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public final class ContestManager implements State, Listener
{

	private final Outposts plugin;

	private final Map<String, Outpost>  zoneToPost = Maps.newHashMap();
	private final Map<Outpost, Contest> postToCont = Maps.newHashMap();


	public ContestManager(final Outposts plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public void load()
	{
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.getWorldGuardHook().addMoveHandler(this::moved);


		for (final var post : plugin.getOutpostManager().getAllOutposts())
		{
			final var zone = post.getCaptureZoneName().split(";")[1];

			zoneToPost.put(zone, post);
		}
	}

	@Override
	public void kill()
	{
		HandlerList.unregisterAll(this);

		zoneToPost.clear();
		postToCont.clear();
	}

	@NotNull
	public Contest getContest(@NotNull final Outpost outpost)
	{
		return postToCont.computeIfAbsent(outpost, Contest::new);
	}


	private void moved(@NotNull Player player, @NotNull World world, @NotNull Collection<String> fromZones, @NotNull Collection<String> intoZones)
	{
		Outpost fromOutpost = null;
		Outpost intoOutpost = null;


		// handle leaving an outpost
		for (final String zone : fromZones)
		{
			fromOutpost = zoneToPost.get(zone);

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
			intoOutpost = zoneToPost.get(zone);

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


	private void handleOutpostEnter(final OutpostEnterEvent event)
	{
		event.getContest().getInside().add(event.getPlayer());
	}

	private void handleOutpostLeave(final OutpostLeaveEvent event)
	{
		event.getContest().getInside().remove(event.getPlayer());
	}


	@EventHandler(priority = EventPriority.MONITOR)
	public void onEnter(@NotNull final OutpostEnterEvent event)
	{
		handleOutpostEnter(event);
	}


	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeave(@NotNull final OutpostLeaveEvent event)
	{
		handleOutpostLeave(event);
	}

}
