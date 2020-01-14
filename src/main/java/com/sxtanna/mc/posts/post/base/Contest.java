package com.sxtanna.mc.posts.post.base;

import com.google.common.collect.Maps;
import com.sxtanna.mc.posts.post.data.CaptureState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public final class Contest
{

	@NotNull
	private final Outpost outpost;


	private final Map<String, Integer>        levels = Maps.newHashMap();
	private final Map<String, Set<Player>>    inside = Maps.newHashMap();
	private final AtomicReference<BukkitTask> update = new AtomicReference<>();

	private CaptureState captureState;
	private String       capturedUUID;


	public Contest(@NotNull final Outpost outpost)
	{
		this.outpost = outpost;
	}

	@NotNull
	public Outpost getOutpost()
	{
		return outpost;
	}

	@NotNull
	public Map<String, Integer> getLevels()
	{
		return levels;
	}

	@NotNull
	public Map<String, Set<Player>> getInside()
	{
		return inside;
	}


	@NotNull
	public Optional<Player> getAnyPlayerInside()
	{
		return getInside().values().stream().findAny().stream().flatMap(Collection::stream).findAny();
	}

	@NotNull
	public Collection<Player> getPlayersInside()
	{
		return getInside().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
	}


	public void enter(@NotNull final String group, @NotNull final Player player)
	{
		inside.computeIfAbsent(group, ($) -> new HashSet<>()).add(player);
	}

	public void leave(@NotNull final String group, @NotNull final Player player)
	{
		final var players = inside.get(group);

		players.remove(player);

		if (players.isEmpty())
		{
			inside.remove(group);
		}
	}


	public int getLevel(@NotNull final String faction)
	{
		return getLevels().getOrDefault(faction, -1);
	}

	@NotNull
	public Integer addLevel(@NotNull final String faction)
	{
		return getLevels().compute(faction, ($, old) -> (old == null ? 0 : old) + 1);
	}

	@Nullable
	public Integer subLevel(@NotNull final String faction)
	{
		return getLevels().compute(faction, ($, old) -> (old == null || old == 0 ? null : old - 1));
	}


	@NotNull
	public CaptureState getCaptureState()
	{
		return captureState != null ? captureState : CaptureState.NEUTRAL;
	}

	public void setCaptureState(@NotNull final CaptureState captureState)
	{
		this.captureState = captureState;
	}


	@NotNull
	public Optional<String> getCapturedUUID()
	{
		return Optional.ofNullable(capturedUUID);
	}

	public void setCapturedUUID(@Nullable final String capturedUUID)
	{
		this.capturedUUID = capturedUUID;
	}


	@NotNull
	public Optional<BukkitTask> swapUpdatingTask(@Nullable final BukkitTask task)
	{
		return Optional.ofNullable(update.getAndSet(task));
	}

}
