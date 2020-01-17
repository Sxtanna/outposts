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

/**
 * Manages the lifetime of an Outpost
 */
public final class ManagerOutpost implements State
{

	private final Outposts plugin;

	private final Map<String, Outpost> nameToPost = Maps.newHashMap();
	private final Map<String, Outpost> zoneToPost = Maps.newHashMap();


	public ManagerOutpost(final Outposts plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public void load()
	{
		loadPosts();

		final var names = nameToPost.values()
									.stream()
									.map(Outpost::getName)
									.collect(Collectors.joining(", ", "[", "]"));

		plugin.getLogger().info(String.format("loaded outposts: %s", names));
	}

	@Override
	public void kill()
	{
		nameToPost.clear();
		zoneToPost.clear();
	}


	/**
	 * Retrieve all loaded outposts
	 */
	@NotNull
	public Collection<Outpost> getAllOutposts()
	{
		return nameToPost.values();
	}


	/**
	 * Retrieve an outpost by its name
	 */
	@NotNull
	public Optional<Outpost> getByName(@NotNull final String name)
	{
		return Optional.ofNullable(nameToPost.get(name.replace(' ', '_').toLowerCase()));
	}

	/**
	 * Retrieve an outpost by the name of its zone (region)
	 */
	@NotNull
	public Optional<Outpost> getByZone(@NotNull final String zone)
	{
		return Optional.ofNullable(zoneToPost.get(zone));
	}


	/**
	 * Retrieve an outpost by location, (uses both worth and AABB checks)
	 */
	@NotNull
	public Optional<Outpost> getByLocation(@NotNull final Supplier<Location> supplier)
	{
		final var location = supplier.get();

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
		final var pull = OutpostConfParse.INSTANCE.pull(plugin, plugin.getConfig().getConfigurationSection("posts"));
		if (pull.isEmpty())
		{
			return;
		}

		for (final Outpost post : pull.get())
		{
			nameToPost.put(post.getUUID(), post);
			zoneToPost.put(post.getCaptureZoneName().split(";")[1], post);
		}
	}

}