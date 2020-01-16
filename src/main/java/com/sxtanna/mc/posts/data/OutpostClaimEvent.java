package com.sxtanna.mc.posts.data;

import com.sxtanna.mc.posts.data.base.OutpostEvent;
import com.sxtanna.mc.posts.post.base.Contest;
import com.sxtanna.mc.posts.post.base.Outpost;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

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


	@NotNull
	private final String factionName;
	@NotNull
	private final String factionUUID;


	public OutpostClaimEvent(@NotNull final Outpost outpost, @NotNull final Contest contest, @NotNull final String factionName, @NotNull final String factionUUID)
	{
		super(outpost, contest);

		this.factionName = factionName;
		this.factionUUID = factionUUID;
	}


	@NotNull
	public String getFactionName()
	{
		return factionName;
	}

	@NotNull
	public String getFactionUUID()
	{
		return factionUUID;
	}

}