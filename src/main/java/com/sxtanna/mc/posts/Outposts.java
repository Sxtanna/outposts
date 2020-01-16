package com.sxtanna.mc.posts;

import com.sxtanna.mc.posts.cmds.CommandOutpost;
import com.sxtanna.mc.posts.hook.HookFactionUID;
import com.sxtanna.mc.posts.hook.HookReplaceApi;
import com.sxtanna.mc.posts.hook.HookShopGuiApi;
import com.sxtanna.mc.posts.hook.HookWorldGuard;
import com.sxtanna.mc.posts.papi.OutpostReplace;
import com.sxtanna.mc.posts.post.ManagerContest;
import com.sxtanna.mc.posts.post.ManagerOutpost;
import org.bukkit.plugin.java.JavaPlugin;

public final class Outposts extends JavaPlugin
{

	private final OutpostReplace outpostReplace = new OutpostReplace(this);
	private final CommandOutpost commandOutpost = new CommandOutpost(this);

	private final ManagerOutpost managerOutpost = new ManagerOutpost(this);
	private final ManagerContest managerContest = new ManagerContest(this);

	private final HookReplaceApi hookReplaceApi = new HookReplaceApi(this);
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


	public OutpostReplace getOutpostReplace()
	{
		return outpostReplace;
	}


	public HookReplaceApi getHookReplaceApi()
	{
		return hookReplaceApi;
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
		getHookReplaceApi().load();
		getHookShopGuiApi().load();
		getHookFactionUID().load();
		getHookWorldGuard().load();

		getManagerOutpost().load();
		getManagerContest().load();
	}

	private void killPlugin()
	{
		getHookReplaceApi().kill();
		getHookShopGuiApi().kill();
		getHookFactionUID().kill();
		getHookWorldGuard().kill();

		getManagerContest().kill();
		getManagerOutpost().kill();
	}

}