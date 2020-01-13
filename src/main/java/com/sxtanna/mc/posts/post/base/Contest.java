package com.sxtanna.mc.posts.post.base;

import com.google.common.collect.Sets;
import org.bukkit.entity.Player;

import java.util.Set;

public final class Contest
{

	// TODO implement capture timer, starting at 0 up to outpost.getCaptureTime
	// TODO counter should only rise while inside.allMatch { inside[0].faction }

	private final Outpost outpost;


	private final Set<Player> inside = Sets.newHashSet();


	public Contest(final Outpost outpost)
	{
		this.outpost = outpost;
	}


	public Outpost getOutpost()
	{
		return outpost;
	}

	public Set<Player> getInside()
	{
		return inside;
	}

}