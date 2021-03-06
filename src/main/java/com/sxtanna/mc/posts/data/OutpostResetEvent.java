package com.sxtanna.mc.posts.data;

import com.sxtanna.mc.posts.data.base.OutpostEvent;
import com.sxtanna.mc.posts.post.base.Contest;
import com.sxtanna.mc.posts.post.base.Outpost;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class OutpostResetEvent extends OutpostEvent
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


	public OutpostResetEvent(@NotNull final Outpost outpost, @NotNull final Contest contest)
	{
		super(outpost, contest);
	}

}