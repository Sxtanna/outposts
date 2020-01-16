package com.sxtanna.mc.posts.base;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface Moved
{

	void moved(final Player player, final World world, final Collection<String> from, final Collection<String> into);

}