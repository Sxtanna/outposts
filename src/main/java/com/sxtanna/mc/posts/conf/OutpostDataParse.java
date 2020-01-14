package com.sxtanna.mc.posts.conf;

import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.Parse;
import com.sxtanna.mc.posts.post.base.Outpost;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class OutpostDataParse implements Parse<Outpost>
{

	public static final OutpostDataParse INSTANCE = new OutpostDataParse();


	private OutpostDataParse()
	{
	}


	@Override
	public Optional<Outpost> pull(@NotNull final Outposts plugin, final @NotNull ConfigurationSection conf)
	{

		//<editor-fold desc="retrieving">
		final var name = conf.getString("name");
		final var zone = conf.getString("zone");

		final var captureTime = conf.getInt("data.time", 10);
		final var captureDone = conf.getStringList("data.done");
		//</editor-fold>


		//<editor-fold desc="validating">
		if (name == null || name.isBlank())
		{
			fail(plugin, conf, "outpost", "missing name");
			return Optional.empty();
		}

		if (zone == null || zone.isBlank())
		{
			fail(plugin, conf, "outpost", "missing zone");
			return Optional.empty();
		}

		final var parts = zone.split(";");
		if (parts.length != 2)
		{
			fail(plugin, conf, "outpost", "malformed zone data `{world};{region}`");
			return Optional.empty();
		}

		final var world = plugin.getServer().getWorld(parts[0]);
		if (world == null)
		{
			fail(plugin, conf, "outpost", "malformed zone data unknown world `%s`", parts[0]);
			return Optional.empty();
		}

		final var cube = plugin.getHookWorldGuard().getCuboidOfRegion(world, parts[1]);
		if (cube.isEmpty())
		{
			fail(plugin, conf, "outpost", "malformed zone data unknown region `%s`", parts[1]);
			return Optional.empty();
		}
		//</editor-fold>


		//<editor-fold desc="generating">
		final var post = new Outpost(name);

		post.setCaptureTime(captureTime);
		post.setCaptureDone(captureDone);

		post.setCaptureZoneName(zone);
		post.setCaptureZoneCube(cube.get());
		//</editor-fold>


		return Optional.of(post);
	}

}
