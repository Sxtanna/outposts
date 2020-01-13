package com.sxtanna.mc.posts.conf;

import com.google.common.collect.Lists;
import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.Parse;
import com.sxtanna.mc.posts.post.base.Outpost;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;


public final class OutpostConfParse implements Parse<Collection<Outpost>>
{

	public static final OutpostConfParse INSTANCE = new OutpostConfParse();


	private OutpostConfParse()
	{
	}


	@NotNull
	@Override
	public Optional<Collection<Outpost>> pull(@NotNull final Outposts plugin, final @NotNull ConfigurationSection conf)
	{
		final var data = Lists.<Outpost>newArrayList();

		for (final var key : conf.getKeys(false))
		{
			final var section = conf.getConfigurationSection(key);

			// if true, this is a single output
			if (section.getString("name") != null)
			{
				// attempt to load the outpost from this section, and add to data if present
				OutpostDataParse.INSTANCE.pull(plugin, section).ifPresent(data::add);
				continue;
			}

			// this is a group of outposts, in order of their tiers

			final var posts = Lists.<Outpost>newArrayList();

			for (final String tier : section.getKeys(false))
			{
				// attempt to load every tier of this outpost group section, and add to posts if present
				OutpostDataParse.INSTANCE.pull(plugin, section.getConfigurationSection(tier)).ifPresent(posts::add);
			}


			// iterate over the outpost group and set their requirements
			final var iter = posts.listIterator(posts.size());

			while (iter.hasPrevious())
			{
				final var here = iter.previous();

				if (!iter.hasPrevious())
				{
					break;
				}

				here.setCapturePrev(iter.previous());
			}

			// add all the newly loaded outposts to the data list
			data.addAll(posts);
		}


		return Optional.of(data);
	}

	@Override
	public void push(@NotNull final Outposts plugin, final @NotNull Collection<Outpost> data, final @NotNull ConfigurationSection conf)
	{

	}

}
