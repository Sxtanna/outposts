package com.sxtanna.mc.posts.data.base;

import com.sxtanna.mc.posts.post.base.Contest;
import com.sxtanna.mc.posts.post.base.Outpost;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class OutpostEvent extends Event
{

	@NotNull
	private final Outpost outpost;
	@NotNull
	private final Contest contest;


	protected OutpostEvent(@NotNull final Outpost outpost, @NotNull final Contest contest)
	{
		this.outpost = outpost;
		this.contest = contest;
	}


	@NotNull
	public Outpost getOutpost()
	{
		return outpost;
	}

	@NotNull
	public Contest getContest()
	{
		return contest;
	}

}