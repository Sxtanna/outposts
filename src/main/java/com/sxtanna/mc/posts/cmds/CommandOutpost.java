package com.sxtanna.mc.posts.cmds;

import com.google.common.collect.Lists;
import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.State;
import com.sxtanna.mc.posts.post.base.Contest;
import com.sxtanna.mc.posts.post.base.Outpost;
import com.sxtanna.mc.posts.post.data.CaptureState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Level;
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
					reply(sender,
						  "you must specify which outpost you want the information of.");
				}
				else
				{
					final var outpost = plugin.getManagerOutpost().getByName(args[1]);
					if (outpost.isEmpty())
					{
						reply(sender,
							  String.format("outpost with the name `%s` does not exist", args[1]));
					}
					else
					{
						sendInfo(sender, outpost.get(), plugin.getManagerContest().getContest(outpost.get()));
					}
				}
				break;
			case "reload":

				try
				{
					plugin.reloadPlugin();

					reply(sender,
						  "successfully reloaded plugin");
				}
				catch (final Exception ex)
				{
					plugin.getLogger().log(Level.SEVERE, "failed to reload plugin", ex);

					reply(sender,
						  String.format("failed to reload plugin: %s", ex.getMessage()));
				}

				break;
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args)
	{
		final var outs = Lists.<String>newArrayList();
		final var last = args.length == 0 ? "" : args[args.length - 1];

		switch (args.length)
		{
			case 0:
			case 1:

				outs.add("info");
				outs.add("list");
				outs.add("reload");

				break;
			case 2:
				if (!args[0].equalsIgnoreCase("info"))
				{
					break;
				}

				for (final var post : plugin.getManagerOutpost().getAllOutposts())
				{
					outs.add(post.getName().toLowerCase().replace(' ', '_'));
				}

				break;
		}

		outs.removeIf(text -> !text.toLowerCase().startsWith(last.toLowerCase()));

		return outs;
	}


	private void reply(@NotNull final CommandSender sender, @NotNull final String... message)
	{
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.join("\n", message)));
	}


	private void sendList(@NotNull final CommandSender sender)
	{
		reply(sender,
			  "Loaded Outposts: ",
			  plugin.getManagerOutpost().getAllOutposts().stream().map(Outpost::getName).collect(Collectors.joining("\n  - ", "  - ", "")));
	}

	private void sendInfo(@NotNull final CommandSender sender, @NotNull final Outpost outpost, @NotNull final Contest contest)
	{
		final var message = new StringBuilder();

		message.append("&7&lOutpost Info:&r");

		message.append('\n').append("&a").append(outpost.getName()).append(" &8[&e").append(contest.getCaptureState()).append("&8]");

		if (contest.getCaptureState() == CaptureState.CLAIMED || contest.getCaptureState() == CaptureState.CONTESTED_UNSEATING)
		{
			message.append(" &8[&c").append(contest.getCapturedUUID().flatMap(plugin.getHookFactionUID()::getFactionName).orElse("Unknown")).append("&8]");
		}

		if (outpost.getCapturePrev() != null)
		{
			message.append('\n').append("  &7<== &6").append(outpost.getCapturePrev().getName());
		}

		if (outpost.getCaptureNext() != null)
		{
			message.append('\n').append("  &7==> &6").append(outpost.getCaptureNext().getName());
		}


		reply(sender, message.toString());
	}

}
