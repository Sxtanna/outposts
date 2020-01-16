package com.sxtanna.mc.posts.papi;

import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.post.base.Contest;
import com.sxtanna.mc.posts.post.base.Outpost;
import com.sxtanna.mc.posts.post.data.CaptureState;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;

public final class OutpostReplace implements BiFunction<OfflinePlayer, String, String>
{

	private static final DecimalFormat FORMAT = new DecimalFormat("#0");
	private static final String[]      INPUTS = {
			"outposts_name",
			"outposts_status",
			"outposts_progress",
			"outposts_capped",
			};


	@NotNull
	private final Outposts plugin;


	public OutpostReplace(@NotNull final Outposts plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public String apply(@Nullable final OfflinePlayer player, @NotNull final String input)
	{
		return requestValueFor(player, input.split("_")).orElse("");
	}

	@NotNull
	@Contract(pure = true)
	public String[] getValidInputs()
	{
		return INPUTS;
	}


	private Optional<String> requestValueFor(@Nullable final OfflinePlayer player, @NotNull final String[] args)
	{
		if (args.length == 0)
		{
			return Optional.empty();
		}

		final var cont = resolveContest(player, args.length <= 1 ? null : String.join("_", Arrays.copyOfRange(args, 1, args.length)));
		if (cont.isEmpty())
		{
			return Optional.empty();
		}

		switch (args[0].toLowerCase())
		{
			case "name":
				return cont.map(Contest::getOutpost).map(Outpost::getName);
			case "status":
				return cont.map(Contest::getCaptureState).map(CaptureState::name);
			case "progress":
				return cont.map(Contest::getCapturePercentage).map(FORMAT::format);
			case "capped":
				return cont.flatMap(Contest::getCapturedUUID).flatMap(plugin.getHookFactionUID()::getFactionName);
		}

		return Optional.empty();
	}

	private Optional<Contest> resolveContest(@Nullable final OfflinePlayer player, @Nullable final String name)
	{
		final Optional<Outpost> outpost;

		if (name != null && !name.isBlank())
		{
			outpost = plugin.getManagerOutpost().getByName(name);
		}
		else if (player != null && player.isOnline())
		{
			outpost = plugin.getManagerOutpost().getByLocation(player.getPlayer()::getLocation);
		}
		else
		{
			outpost = Optional.empty();
		}

		return outpost.map(plugin.getManagerContest()::getContest);
	}


	interface ReplaceFunctionProvider
	{

		void selfRegister();

		BiFunction<OfflinePlayer, String, String> getFunction();

	}

}