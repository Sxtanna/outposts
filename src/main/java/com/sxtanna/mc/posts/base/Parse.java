package com.sxtanna.mc.posts.base;

import com.sxtanna.mc.posts.Outposts;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Parse<T>
{

	Optional<T> pull(@NotNull final Outposts plugin, @NotNull final ConfigurationSection conf);

	void push(@NotNull final Outposts plugin, @NotNull final T data, @NotNull final ConfigurationSection conf);


	default void fail(@NotNull final Plugin plugin, @NotNull final ConfigurationSection conf, @NotNull final String type, @NotNull final String reason, final Object... arguments)
	{
		plugin.getLogger().warning(String.format("failed to %s %s section: `%s` : %s", type, type.startsWith("pull") ? "from" : "into", conf.getCurrentPath(), String.format(reason, arguments)));
	}

}
