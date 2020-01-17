package com.sxtanna.mc.posts.post;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.State;
import com.sxtanna.mc.posts.lang.Lang;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Manages loading and sending messages to players
 */
public final class ManagerMessage implements State
{

	@NotNull
	private final Outposts          plugin;
	@NotNull
	private final Set<UUID>         toggle = Sets.newHashSet();
	@NotNull
	private final Map<Lang, String> cached = Maps.newHashMap();


	public ManagerMessage(@NotNull final Outposts plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public void load()
	{
		final var file = new File(plugin.getDataFolder(), "lang.yml");
		if (!file.exists())
		{
			plugin.saveResource("lang.yml", false);
		}

		final var yaml = YamlConfiguration.loadConfiguration(file);

		for (final var name : yaml.getKeys(false))
		{
			final var lang = Lang.byName(name);
			if (lang.isEmpty())
			{
				continue;
			}

			cached.put(lang.get(), yaml.getString(name, lang.get().getDefaultMessage()));
		}
	}

	@Override
	public void kill()
	{
		cached.clear();
	}


	/**
	 * Whether a player wants to receive global plugin messages
	 */
	public boolean wantsMessages(@NotNull final UUID uuid)
	{
		return !toggle.contains(uuid);
	}

	/**
	 * Toggle whether a player wants global messages or not
	 */
	public void toggleMessagesTo(@NotNull final UUID uuid)
	{
		if (toggle.remove(uuid))
		{
			return;
		}

		toggle.add(uuid);
	}


	/**
	 * Create the final text of a message with custom placeholders
	 */
	@NotNull
	public String make(@NotNull final Lang lang, @NotNull final Object... placeholders)
	{
		if (placeholders.length % 2 != 0)
		{
			throw new IllegalArgumentException("Placeholders must all have values: " + Arrays.toString(placeholders));
		}

		var message = cached.getOrDefault(lang, lang.getDefaultMessage());
		if (message.isBlank())
		{
			return "";
		}

		message = message.replace(":prefix:", cached.getOrDefault(Lang.PREFIX, Lang.PREFIX.getDefaultMessage()));

		if (placeholders.length != 0)
		{
			for (int i = 0; i < placeholders.length; i += 2)
			{
				final var k = placeholders[i];
				final var v = placeholders[i + 1];

				if (!(k instanceof String))
				{
					throw new IllegalArgumentException("Placeholder values has an object out of position: " + k.getClass() + "[" + i + "]{" + k + "}");
				}

				message = message.replace(":" + k + ":", Objects.toString(v));
			}
		}

		return ChatColor.translateAlternateColorCodes('&', message);
	}

	/**
	 * Send a message to all players who want them
	 */
	public void send(@NotNull final Lang lang, @NotNull final Object... placeholders)
	{
		final var message = make(lang, placeholders);

		for (final var online : plugin.getServer().getOnlinePlayers())
		{
			if (!wantsMessages(online.getUniqueId()))
			{
				continue;
			}

			online.sendMessage(message);
		}
	}

	/**
	 * Send a message to a specific recipient, passing through optional placeholder plugins
	 */
	public void send(@NotNull final CommandSender recipient, @NotNull final Lang lang, @NotNull final Object... placeholders)
	{
		var message = make(lang, placeholders);
		if (message.isBlank())
		{
			return;
		}

		if (recipient instanceof OfflinePlayer)
		{
			message = plugin.getHookReplaceApi().request(message, ((OfflinePlayer) recipient));
		}

		recipient.sendMessage(message);
	}

}