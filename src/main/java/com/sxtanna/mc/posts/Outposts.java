package com.sxtanna.mc.posts;

import com.sxtanna.mc.posts.cmds.CommandOutpost;
import com.sxtanna.mc.posts.hook.HookFactionUID;
import com.sxtanna.mc.posts.hook.HookShopGuiApi;
import com.sxtanna.mc.posts.hook.HookWorldGuard;
import com.sxtanna.mc.posts.papi.Placeholders;
import com.sxtanna.mc.posts.post.ManagerContest;
import com.sxtanna.mc.posts.post.ManagerOutpost;
import org.bukkit.plugin.java.JavaPlugin;

public final class Outposts extends JavaPlugin
{

	private final CommandOutpost commandOutpost = new CommandOutpost(this);

	private final ManagerOutpost managerOutpost = new ManagerOutpost(this);
	private final ManagerContest managerContest = new ManagerContest(this);

	private final HookFactionUID hookFactionUID = new HookFactionUID(this);
	private final HookShopGuiApi hookShopGuiApi = new HookShopGuiApi(this);
	private final HookWorldGuard hookWorldGuard = new HookWorldGuard(this);


	@Override
	public void onLoad()
	{
		saveDefaultConfig();
	}

	@Override
	public void onEnable()
	{
		loadPlugin();

		attemptToRegisterPlaceholders();

		commandOutpost.load();
	}

	@Override
	public void onDisable()
	{
		commandOutpost.kill();

		killPlugin();
	}


	public ManagerOutpost getManagerOutpost()
	{
		return managerOutpost;
	}

	public ManagerContest getManagerContest()
	{
		return managerContest;
	}


	public HookFactionUID getHookFactionUID()
	{
		return hookFactionUID;
	}

	public HookShopGuiApi getHookShopGuiApi()
	{
		return hookShopGuiApi;
	}

	public HookWorldGuard getHookWorldGuard()
	{
		return hookWorldGuard;
	}


	public void reloadPlugin()
	{
		killPlugin();

		reloadConfig();

		loadPlugin();
	}


	private void loadPlugin()
	{
		getHookShopGuiApi().load();
		getHookFactionUID().load();
		getHookWorldGuard().load();

		getManagerOutpost().load();
		getManagerContest().load();
	}

	private void killPlugin()
	{
		getHookShopGuiApi().kill();
		getHookFactionUID().kill();
		getHookWorldGuard().kill();

		getManagerContest().kill();
		getManagerOutpost().kill();
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
