package com.sxtanna.mc.posts.cmds;

import com.google.common.collect.Lists;
import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.State;
import com.sxtanna.mc.posts.lang.Lang;
import com.sxtanna.mc.posts.post.base.Contest;
import com.sxtanna.mc.posts.post.base.Outpost;
import com.sxtanna.mc.posts.post.data.CaptureState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
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
		if (args.length == 0 || args[0].equalsIgnoreCase("help"))
		{
			if (sender.hasPermission("outposts.command"))
			{
				sendHelp(sender);
			}

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
					reply(sender, "you must specify which outpost you want the information of.");
				}
				else
				{
					final var outpost = plugin.getManagerOutpost().getByName(args[1]);
					if (outpost.isEmpty())
					{
						reply(sender, String.format("outpost with the name `%s` does not exist", args[1]));
					}
					else
					{
						sendInfo(sender, outpost.get(), plugin.getManagerContest().getContest(outpost.get()));
					}
				}
				break;
			case "toggle":
				if (!(sender instanceof Player))
				{
					return true;
				}

				final var player = ((Player) sender);
				plugin.getManagerMessage().toggleMessagesTo(player.getUniqueId());

				plugin.getManagerMessage().send(player, Lang.COMMAND_TOGGLE_MSGS,
												"toggle_status", plugin.getManagerMessage().wantsMessages(player.getUniqueId()) ? "on" : "off");
				break;
			case "reload":
				try
				{
					plugin.reloadPlugin();

					plugin.getManagerMessage().send(sender, Lang.COMMAND_RELOAD_PASS);
				}
				catch (final Exception ex)
				{
					plugin.getLogger().log(Level.SEVERE, "failed to reload plugin", ex);

					plugin.getManagerMessage().send(sender, Lang.COMMAND_RELOAD_FAIL, "fail_message", ex.getMessage());
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
				if (sender.hasPermission("outposts.command"))
				{
					outs.add("help");
				}
				if (sender.hasPermission("outposts.command.info"))
				{
					outs.add("info");
				}
				if (sender.hasPermission("outposts.command.list"))
				{
					outs.add("list");
				}
				if (sender.hasPermission("outposts.command.toggle"))
				{
					outs.add("toggle");
				}
				if (sender.hasPermission("outposts.command.reload"))
				{
					outs.add("reload");
				}
				break;
			case 2:
				if (!args[0].equalsIgnoreCase("info") || !sender.hasPermission("outposts.command.info"))
				{
					break;
				}

				for (final var post : plugin.getManagerOutpost().getAllOutposts())
				{
					outs.add(post.getUUID());
				}
				break;
		}

		outs.sort(Comparator.naturalOrder());
		outs.removeIf(text -> !text.toLowerCase().startsWith(last.toLowerCase()));

		return outs;
	}


	private void reply(@NotNull final CommandSender sender, @NotNull final String... message)
	{
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.join("\n", message)));
	}


	private void sendHelp(@NotNull final CommandSender sender)
	{
		final var message = new StringBuilder();

		message.append("&c&lOutposts Commands&r").append('\n');

		if (sender.hasPermission("outposts.command.info"))
		{
			message.append("  &7/outposts info").append('\n');
		}
		if (sender.hasPermission("outposts.command.list"))
		{
			message.append("  &7/outposts list").append('\n');
		}
		if (sender.hasPermission("outposts.command.toggle"))
		{
			message.append("  &7/outposts toggle").append('\n');
		}
		if (sender.hasPermission("outposts.command.reload"))
		{
			message.append("  &7/outposts reload").append('\n');
		}

		reply(sender, message.toString());
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

		message.append("&lOutpost Info:&r");
		message.append("\n  &a").append(outpost.getName()).append(" &8[&e").append(contest.getCaptureState()).append("&8]");

		if (contest.getCaptureState() == CaptureState.CLAIMED || contest.getCaptureState() == CaptureState.CONTESTED_UNSEATING)
		{
			message.append(" &8[&c").append(contest.getCapturedUUID().flatMap(plugin.getHookFactionUID()::getFactionName).orElse("Unknown")).append("&8]");
		}

		outpost.getCapturePrev().ifPresent(prev -> message.append("\n    &7<== &6").append(prev.getName()));
		outpost.getCaptureNext().ifPresent(next -> message.append("\n    &7==> &6").append(next.getName()));

		reply(sender, message.toString());
	}

}