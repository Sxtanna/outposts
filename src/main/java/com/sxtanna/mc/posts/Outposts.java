package com.sxtanna.mc.posts;

import com.sxtanna.mc.posts.hook.FactionUIDHook;
import com.sxtanna.mc.posts.hook.ShopGuiApiHook;
import com.sxtanna.mc.posts.hook.WorldGuardHook;
import com.sxtanna.mc.posts.papi.Placeholders;
import com.sxtanna.mc.posts.post.ContestManager;
import com.sxtanna.mc.posts.post.OutpostManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Outposts extends JavaPlugin
{

	private final OutpostManager outpostManager = new OutpostManager(this);
	private final ContestManager contestManager = new ContestManager(this);

	private final FactionUIDHook factionUIDHook = new FactionUIDHook(this);
	private final ShopGuiApiHook shopGuiApiHook = new ShopGuiApiHook(this);
	private final WorldGuardHook worldGuardHook = new WorldGuardHook(this);


	@Override
	public void onLoad()
	{
		saveDefaultConfig();
	}

	@Override
	public void onEnable()
	{
		getShopGuiApiHook().load();
		getFactionUIDHook().load();
		getWorldGuardHook().load();

		getOutpostManager().load();
		getContestManager().load();

		attemptToRegisterPlaceholders();
	}

	@Override
	public void onDisable()
	{
		getShopGuiApiHook().kill();
		getFactionUIDHook().kill();
		getWorldGuardHook().kill();

		getContestManager().kill();
		getOutpostManager().kill();
	}


	public OutpostManager getOutpostManager()
	{
		return outpostManager;
	}

	public ContestManager getContestManager()
	{
		return contestManager;
	}


	public FactionUIDHook getFactionUIDHook()
	{
		return factionUIDHook;
	}

	public ShopGuiApiHook getShopGuiApiHook()
	{
		return shopGuiApiHook;
	}

	public WorldGuardHook getWorldGuardHook()
	{
		return worldGuardHook;
	}


	private void attemptToRegisterPlaceholders()
	{
		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
		{
			new Placeholders(this).register();
		}
		else
		{
			getLogger().warning("placeholderapi not found, placeholders will not work!");
		}
	}

}
