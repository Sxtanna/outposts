package com.sxtanna.mc.posts.post;

import com.google.common.collect.Maps;
import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.State;
import com.sxtanna.mc.posts.conf.OutpostConfParse;
import com.sxtanna.mc.posts.post.base.Outpost;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class OutpostManager implements State
{

	private final Outposts             plugin;
	private final Map<String, Outpost> cached = Maps.newHashMap();


	public OutpostManager(final Outposts plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public void load()
	{
		loadPosts();
		plugin.getLogger().info(String.format("loaded outposts: %s", cached.values().stream().map(Outpost::getName).collect(Collectors.joining(", ", "[", "]"))));
	}

	@Override
	public void kill()
	{
		cached.clear();
	}


	@NotNull
	public Collection<Outpost> getAllOutposts()
	{
		return cached.values();
	}

	@NotNull
	public Optional<Outpost> getByLocation(@NotNull final Supplier<Location> supplier)
	{
		final var location = supplier.get().toVector();

		for (final var outpost : getAllOutposts())
		{
			if (!outpost.getCaptureZoneCube().inside(location))
			{
				continue;
			}

			return Optional.of(outpost);
		}

		return Optional.empty();
	}


	private void loadPosts()
	{
		final var conf = plugin.getConfig().getConfigurationSection("posts");

		OutpostConfParse.INSTANCE.pull(plugin, conf).ifPresent(values -> values.forEach(post -> cached.put(post.getName().replace(' ', '_'), post)));
	}

}
