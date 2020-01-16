package com.sxtanna.mc.posts;

import com.sxtanna.mc.posts.cmds.CommandOutpost;
import com.sxtanna.mc.posts.hook.HookFactionUID;
import com.sxtanna.mc.posts.hook.HookReplaceApi;
import com.sxtanna.mc.posts.hook.HookShopGuiApi;
import com.sxtanna.mc.posts.hook.HookWorldGuard;
import com.sxtanna.mc.posts.papi.OutpostReplace;
import com.sxtanna.mc.posts.post.ManagerContest;
import com.sxtanna.mc.posts.post.ManagerMessage;
import com.sxtanna.mc.posts.post.ManagerOutpost;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Outposts extends JavaPlugin
{

	@NotNull
	private final OutpostReplace outpostReplace = new OutpostReplace(this);
	@NotNull
	private final CommandOutpost commandOutpost = new CommandOutpost(this);


	@NotNull
	private final ManagerOutpost managerOutpost = new ManagerOutpost(this);
	@NotNull
	private final ManagerContest managerContest = new ManagerContest(this);
	@NotNull
	private final ManagerMessage managerMessage = new ManagerMessage(this);


	@NotNull
	private final HookReplaceApi hookReplaceApi = new HookReplaceApi(this);
	@NotNull
	private final HookFactionUID hookFactionUID = new HookFactionUID(this);
	@NotNull
	private final HookShopGuiApi hookShopGuiApi = new HookShopGuiApi(this);
	@NotNull
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


	@NotNull
	public ManagerOutpost getManagerOutpost()
	{
		return managerOutpost;
	}

	@NotNull
	public ManagerContest getManagerContest()
	{
		return managerContest;
	}

	@NotNull
	public ManagerMessage getManagerMessage()
	{
		return managerMessage;
	}


	@NotNull
	public OutpostReplace getOutpostReplace()
	{
		return outpostReplace;
	}


	@NotNull
	public HookReplaceApi getHookReplaceApi()
	{
		return hookReplaceApi;
	}

	@NotNull
	public HookFactionUID getHookFactionUID()
	{
		return hookFactionUID;
	}

	@NotNull
	public HookShopGuiApi getHookShopGuiApi()
	{
		return hookShopGuiApi;
	}

	@NotNull
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

		getManagerMessage().load();
		getManagerOutpost().load();
		getManagerContest().load();
	}

	private void killPlugin()
	{
		getHookReplaceApi().kill();
		getHookShopGuiApi().kill();
		getHookFactionUID().kill();
		getHookWorldGuard().kill();

		getManagerMessage().kill();
		getManagerContest().kill();
		getManagerOutpost().kill();
	}

}