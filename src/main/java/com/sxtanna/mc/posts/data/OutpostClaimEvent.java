package com.sxtanna.mc.posts.data;

import com.sxtanna.mc.posts.data.base.OutpostEvent;
import com.sxtanna.mc.posts.post.base.Contest;
import com.sxtanna.mc.posts.post.base.Outpost;
import org.bukkit.event.HandlerList;

public final class OutpostClaimEvent extends OutpostEvent
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


	public OutpostClaimEvent(final Outpost outpost, final Contest contest)
	{
		super(outpost, contest);
	}

}
