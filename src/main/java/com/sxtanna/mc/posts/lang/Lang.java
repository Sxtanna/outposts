package com.sxtanna.mc.posts.lang;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum Lang
{

	PREFIX("&c&lOUTPOSTS&r &8»&r"),

	COMMAND_RELOAD_PASS(":prefix:&r&f plugin has been reloaded!"),
	COMMAND_RELOAD_FAIL(":prefix:&r&f failed to reload plugin, :fail_message:"),

	COMMAND_TOGGLE_MSGS(":prefix:&r&f outpost status messages &l&n:toggle_status:&f."),

	OUTPOST_ENTER("You have entered &b:outpost_name:&f!"),
	OUTPOST_LEAVE("You have left &b:outpost_name:&f!"),

	OUTPOST_STATE_CLAIMED(":prefix:&r&f &b:outpost_name:&f has been captured by &c:faction_name:&f!"),
	OUTPOST_STATE_FORTIFY(":prefix:&r&f &c:faction_name:&f has recaptured &b:outpost_name:&f!"),

	OUTPOST_STATE_CAPTURING(":prefix:&r&f &c:faction_name:&f is capturing &b:outpost_name:&f!"),
	OUTPOST_STATE_UNSEATING(":prefix:&r&f &b:outpost_name:&f is being unseated by &c:faction_name:&f!"),

	OUTPOST_STATE_CONTESTED_CAPTURING(":prefix:&r&f &b:outpost_name:&f is being contested!"),
	OUTPOST_STATE_CONTESTED_UNSEATING(":prefix:&r&f &c:faction_name:&f is being defending &b:outpost_name:&f!"),

	;


	private final String defaultMessage;


	Lang(final String defaultMessage)
	{
		this.defaultMessage = defaultMessage;
	}


	public String getDefaultMessage()
	{
		return defaultMessage;
	}

	@NotNull
	public static Optional<Lang> byName(@NotNull final String name)
	{
		final var text = name.toUpperCase().replace(" ", "_");

		for (final var lang : values())
		{
			if (!lang.name().equals(text))
			{
				continue;
			}

			return Optional.of(lang);
		}

		return Optional.empty();
	}

}