package com.sxtanna.mc.posts.papi;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import com.sxtanna.mc.posts.Outposts;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public final class OutpostReplaceMVDW implements PlaceholderReplacer, OutpostReplace.ReplaceFunctionProvider
{

	@NotNull
	private final Outposts plugin;


	public OutpostReplaceMVDW(@NotNull final Outposts plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public String onPlaceholderReplace(final PlaceholderReplaceEvent event)
	{
		return plugin.getOutpostReplace().apply(event.getOfflinePlayer(), event.getPlaceholder());
	}


	@Override
	public void selfRegister()
	{
		for (final var input : plugin.getOutpostReplace().getValidInputs())
		{
			PlaceholderAPI.registerPlaceholder(plugin, input, this);
		}
	}

	@Override
	public BiFunction<OfflinePlayer, String, String> getFunction()
	{
		return PlaceholderAPI::replacePlaceholders;
	}

}