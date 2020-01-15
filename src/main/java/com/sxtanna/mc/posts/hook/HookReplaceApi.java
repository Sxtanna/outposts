package com.sxtanna.mc.posts.hook;

import com.google.common.collect.Lists;
import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.Hooks;
import com.sxtanna.mc.posts.papi.OutpostReplaceMVDW;
import com.sxtanna.mc.posts.papi.OutpostReplacePAPI;
import com.sxtanna.mc.posts.post.base.Outpost;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;

public final class HookReplaceApi implements Hooks
{

	@NotNull
	private final Outposts plugin;

	@NotNull
	private final List<BiFunction<OfflinePlayer, String, String>> replacers = Lists.newArrayList();


	public HookReplaceApi(@NotNull final Outposts plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public void load()
	{
		attemptToRegisterClipPlaceholders();
		attemptToRegisterMVDWPlaceholders();
	}

	@Override
	public void kill()
	{
		replacers.clear();
	}


	public String request(@NotNull final String text, @Nullable final OfflinePlayer player, @Nullable final Outpost outpost)
	{
		var finalText = text;

		for (final var replacer : replacers)
		{
			finalText = replacer.apply(player, finalText);
		}


		if (player != null)
		{
			finalText = finalText.replace(":player_name:", player.getName());
		}

		if (outpost != null)
		{
			finalText = finalText.replace(":outpost_name:", outpost.getName());
		}

		return finalText;
	}


	private void attemptToRegisterClipPlaceholders()
	{
		if (!plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
		{
			return;
		}

		try
		{
			final var replace = new OutpostReplacePAPI(plugin);
			replace.selfRegister();

			replacers.add(replace.getFunction());

			plugin.getLogger().info("registered PAPI placeholders!");
		}
		catch (final Throwable ex)
		{
			plugin.getLogger().warning("failed to register PAPI placeholders: " + ex.getMessage());
		}
	}

	private void attemptToRegisterMVDWPlaceholders()
	{
		if (!plugin.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI"))
		{
			return;
		}

		try
		{
			final var replace = new OutpostReplaceMVDW(plugin);
			replace.selfRegister();

			replacers.add(replace.getFunction());

			plugin.getLogger().info("registered MVDW placeholders!");
		}
		catch (final Throwable ex)
		{
			plugin.getLogger().warning("failed to register MVDW placeholders: " + ex.getMessage());
		}
	}


}
