package com.sxtanna.mc.posts.post.base;

import com.sxtanna.mc.posts.post.data.OutpostActor;
import com.sxtanna.mc.posts.util.Cuboid;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Outpost
{

	@NotNull
	private final String name;


	private Outpost            capturePrev;
	private Outpost            captureNext;
	private int                captureTime;
	private List<OutpostActor> captureDone;


	private String captureZoneName;
	private Cuboid captureZoneCube;


	public Outpost(@NotNull final String name)
	{
		this.name = name;
	}


	@NotNull
	public String getName()
	{
		return name;
	}

	@NotNull
	public String getUUID()
	{
		return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', getName())).replace(" ", "_").toLowerCase();
	}


	@NotNull
	public Optional<Outpost> getCapturePrev()
	{
		return Optional.ofNullable(capturePrev);
	}

	public void setCapturePrev(@Nullable final Outpost capturePrev)
	{
		this.capturePrev = capturePrev;
	}


	@NotNull
	public Optional<Outpost> getCaptureNext()
	{
		return Optional.ofNullable(captureNext);
	}

	public void setCaptureNext(@Nullable final Outpost captureNext)
	{
		this.captureNext = captureNext;
	}


	public int getCaptureTime()
	{
		return captureTime;
	}

	public void setCaptureTime(final int captureTime)
	{
		this.captureTime = captureTime;
	}


	@NotNull
	public List<OutpostActor> getCaptureDone()
	{
		return captureDone != null ? captureDone : Collections.emptyList();
	}

	public void setCaptureDone(@NotNull final Collection<OutpostActor> captureDone)
	{
		this.captureDone = new ArrayList<>(captureDone);
	}


	@NotNull
	public String getCaptureZoneName()
	{
		return captureZoneName;
	}

	public void setCaptureZoneName(@NotNull final String captureZoneName)
	{
		this.captureZoneName = captureZoneName;
	}


	@NotNull
	public Cuboid getCaptureZoneCube()
	{
		return captureZoneCube != null ? captureZoneCube : Cuboid.none();
	}

	public void setCaptureZoneCube(@NotNull final Cuboid captureZone)
	{
		this.captureZoneCube = captureZone;
	}


	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof Outpost))
		{
			return false;
		}

		final Outpost outpost = (Outpost) o;
		return getUUID().equals(outpost.getUUID());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getUUID());
	}

	@Override
	public String toString()
	{
		return String.format("Outpost[name: '%s', prev: '%s', next: '%s']",
							 name,
							 capturePrev == null ? "none" : capturePrev.getName(),
							 captureNext == null ? "none" : captureNext.getName());
	}

}