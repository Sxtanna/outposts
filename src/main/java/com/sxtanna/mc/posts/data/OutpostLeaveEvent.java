package com.sxtanna.mc.posts.data;

import com.sxtanna.mc.posts.data.base.OutpostEvent;
import com.sxtanna.mc.posts.post.base.Contest;
import com.sxtanna.mc.posts.post.base.Outpost;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class OutpostLeaveEvent extends OutpostEvent
{

	private static final HandlerList HANDLERS = new HandlerList();

	public HandlerList getHandlers()
	{
		return HANDLERS;
	}

	public static HandlerList getHandlerList()
	{
		return HANDLERS;
	}


	@NotNull
	private final Player player;


	public OutpostLeaveEvent(final Outpost outpost, final Contest contest, @NotNull final Player player)
	{
		super(outpost, contest);
		this.player = player;
	}


	@NotNull
	public Player getPlayer()
	{
		return player;
	}

}
