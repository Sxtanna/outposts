package com.sxtanna.mc.posts.cmds;

import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.State;
import com.sxtanna.mc.posts.post.base.Contest;
import com.sxtanna.mc.posts.post.base.Outpost;
import com.sxtanna.mc.posts.post.data.CaptureState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandOutpost implements State, CommandExecutor, TabCompleter
{

	private final Outposts plugin;


	public CommandOutpost(final Outposts plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public void load()
	{
		final var command = plugin.getServer().getPluginCommand("outposts");
		if (command == null)
		{
			return;
		}

		command.setExecutor(this);
		command.setTabCompleter(this);
	}

	@Override
	public void kill()
	{
		final var command = plugin.getServer().getPluginCommand("outposts");
		if (command == null)
		{
			return;
		}

		command.setExecutor(null);
		command.setTabCompleter(null);
	}


	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length == 0)
		{
			// maybe send usage
			return true;
		}

		switch (args[0].toLowerCase())
		{
			case "list":
				sendList(sender);
				break;
			case "info":

				if (args.length < 2)
				{
					// TODO tell them to specify the outpost
					return true;
				}

				final var outpost = plugin.getManagerOutpost().getByName(args[1]);
				if (outpost.isEmpty())
				{
					// TODO tell them it doesn't exist
				}
				else
				{
					sendInfo(sender, outpost.get(), plugin.getManagerContest().getContest(outpost.get()));
				}

				break;
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args)
	{
		return Collections.emptyList();
	}


	private void sendList(final CommandSender sender)
	{
		final var names = plugin.getManagerOutpost().getAllOutposts().stream().map(Outpost::getName).collect(Collectors.joining("\n  - ", "  - ", ""));
		sender.sendMessage("Loaded Outposts: \n" + names);
	}

	private void sendInfo(final CommandSender sender, @NotNull final Outpost outpost, @NotNull final Contest contest)
	{
		final var message = new StringBuilder();

		message.append("Outpost Info:");

		message.append('\n').append("  Named: ").append(outpost.getName());
		message.append('\n').append("  State: ").append(contest.getCaptureState());

		if (outpost.getCapturePrev() != null)
		{
			message.append('\n').append("  Requires: ").append(outpost.getCapturePrev().getName());
		}

		if (contest.getCaptureState() == CaptureState.CLAIMED)
		{
			// message.append('\n').append("  Captured Name: ").append(contest.getCapturedByName().orElse("unknown"));
			// message.append('\n').append("  Captured UUID: ").append(contest.getCapturedByUUID().orElse("unknown"));
		}

		sender.sendMessage(message.toString());
	}

}
