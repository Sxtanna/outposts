package com.sxtanna.mc.posts.data;

import com.sxtanna.mc.posts.data.base.OutpostEvent;
import com.sxtanna.mc.posts.post.base.Contest;
import com.sxtanna.mc.posts.post.base.Outpost;
import com.sxtanna.mc.posts.post.data.CaptureState;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class ContestStateEvent extends OutpostEvent
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
	private final CaptureState oldState;
	@NotNull
	private final CaptureState newState;


	public ContestStateEvent(@NotNull final Outpost outpost, @NotNull final Contest contest, @NotNull final CaptureState oldState, @NotNull final CaptureState newState)
	{
		super(outpost, contest);

		this.oldState = oldState;
		this.newState = newState;
	}


	@NotNull
	public CaptureState getOldState()
	{
		return oldState;
	}

	@NotNull
	public CaptureState getNewState()
	{
		return newState;
	}

}