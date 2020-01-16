package com.sxtanna.mc.posts.base;

import com.sxtanna.mc.posts.Outposts;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Parse<T>
{

	Optional<T> pull(@NotNull final Outposts plugin, @NotNull final ConfigurationSection conf);

	default void fail(@NotNull final Plugin plugin, @NotNull final ConfigurationSection conf, @NotNull final String name, @NotNull final String reason, final Object... arguments)
	{
		plugin.getLogger().warning(String.format("failed to load %s from section: `%s` : %s", name, conf.getCurrentPath(), String.format(reason, arguments)));
	}

}