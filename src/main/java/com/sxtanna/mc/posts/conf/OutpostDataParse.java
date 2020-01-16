package com.sxtanna.mc.posts.conf;

import com.google.common.collect.Lists;
import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.Parse;
import com.sxtanna.mc.posts.post.base.Outpost;
import com.sxtanna.mc.posts.post.data.OutpostActor;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
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
		final var outpostName = conf.getString("name");
		final var outpostZone = conf.getString("zone");

		final var captureTime = conf.getInt("data.time", 10);
		final var captureDone = conf.getStringList("data.done");
		//</editor-fold>


		//<editor-fold desc="validating">
		if (captureTime <= 0)
		{
			fail(plugin, conf, "outpost", "capture time must be greater than 0");
			return Optional.empty();
		}

		if (outpostName == null || outpostName.isBlank())
		{
			fail(plugin, conf, "outpost", "missing name");
			return Optional.empty();
		}

		if (outpostZone == null || outpostZone.isBlank())
		{
			fail(plugin, conf, "outpost", "missing zone");
			return Optional.empty();
		}

		final var parts = outpostZone.split(";");
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
		final var post = new Outpost(outpostName);

		post.setCaptureTime(captureTime);

		post.setCaptureZoneName(outpostZone);
		post.setCaptureZoneCube(cube.get());

		final var done = Lists.<OutpostActor>newArrayList();

		for (final var text : captureDone)
		{
			final var data = text.split(" ");
			if (data.length <= 1)
			{
				continue;
			}

			final var name = data[0];
			if (!name.startsWith("[") || !name.endsWith("]"))
			{
				continue;
			}

			final var pass = String.join(" ", Arrays.copyOfRange(data, 1, data.length));

			OutpostActor actor = null;

			switch (name.substring(1, name.length() - 1).toLowerCase())
			{
				case "message":
					actor = new OutpostActor.Message(pass);
					break;
				case "command-player":
					actor = new OutpostActor.Command(pass, false);
					break;
				case "command-server":
					actor = new OutpostActor.Command(pass, true);
					break;
			}

			if (actor != null)
			{
				done.add(actor);
			}
		}

		post.setCaptureDone(done);
		//</editor-fold>


		return Optional.of(post);
	}

}