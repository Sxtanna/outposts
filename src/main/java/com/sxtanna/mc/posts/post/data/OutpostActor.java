package com.sxtanna.mc.posts.post.data;

import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.post.base.Outpost;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface OutpostActor
{

	void act(@NotNull Outposts plugin, @NotNull final Outpost outpost, @NotNull final Player player);


	final class Message implements OutpostActor
	{

		@NotNull
		private final String message;


		public Message(@NotNull final String message)
		{
			this.message = message;
		}


		@Override
		public void act(@NotNull final Outposts plugin, final @NotNull Outpost outpost, final @NotNull Player player)
		{
			player.sendMessage(plugin.getHookReplaceApi().request(message, player, outpost));
		}

	}

	final class Command implements OutpostActor
	{

		@NotNull
		private final String  command;
		private final boolean console;


		public Command(@NotNull final String command, final boolean console)
		{
			this.command = command;
			this.console = console;
		}


		@Override
		public void act(@NotNull Outposts plugin, final @NotNull Outpost outpost, final @NotNull Player player)
		{
			Bukkit.dispatchCommand(console ? Bukkit.getConsoleSender() : player, plugin.getHookReplaceApi().request(command, player, outpost));
		}

	}

}
