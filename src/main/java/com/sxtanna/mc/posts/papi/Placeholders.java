package com.sxtanna.mc.posts.papi;

import com.sxtanna.mc.posts.Outposts;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public final class Placeholders extends PlaceholderExpansion
{

	private final Outposts plugin;


	public Placeholders(final Outposts plugin)
	{
		this.plugin = plugin;
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
	public String getIdentifier()
	{
		return "outposts";
	}


	@Override
	public boolean persist()
	{
		return true;
	}

	@Override
	public String onRequest(final OfflinePlayer p, final String params)
	{

		switch (params.toLowerCase())
		{
			case "status":
				break;
			case "progress":
				break;
			case "capped":
				break;

		}

		return "";
	}

}
