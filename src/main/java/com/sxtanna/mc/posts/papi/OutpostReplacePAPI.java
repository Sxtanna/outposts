package com.sxtanna.mc.posts.papi;

import com.sxtanna.mc.posts.Outposts;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public final class OutpostReplacePAPI extends PlaceholderExpansion implements OutpostReplace.ReplaceFunctionProvider
{

	@NotNull
	private final Outposts plugin;


	public OutpostReplacePAPI(@NotNull final Outposts plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public String getIdentifier()
	{
		return "outposts";
	}

	@Override
	public String getAuthor()
	{
		return "Sxtanna";
	}

	@Override
	public String getVersion()
	{
		return "1.0";
	}

	@Override
	public boolean persist()
	{
		return true;
	}


	@Override
	public String onRequest(final OfflinePlayer player, final String params)
	{
		return plugin.getOutpostReplace().apply(player, params);
	}


	@Override
	public void selfRegister()
	{
		register();
	}

	@Override
	public BiFunction<OfflinePlayer, String, String> getFunction()
	{
		return PlaceholderAPI::setPlaceholders;
	}

}