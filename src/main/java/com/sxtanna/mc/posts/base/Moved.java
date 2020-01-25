package com.sxtanna.mc.posts.base;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Moved
{

	void moved(@NotNull final Player player, @NotNull final World world, @NotNull final Collection<String> from, @NotNull final Collection<String> into);

}