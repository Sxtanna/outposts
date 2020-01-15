package com.sxtanna.mc.posts.post.base;

import com.sxtanna.mc.posts.util.Cuboid;
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


	private Outpost      capturePrev;
	private Outpost      captureNext;
	private int          captureTime;
	private List<String> captureDone;


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
	public List<String> getCaptureDone()
	{
		return captureDone != null ? captureDone : Collections.emptyList();
	}

	public void setCaptureDone(@NotNull final Collection<String> captureDone)
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
		return getName().equals(outpost.getName());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getName());
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
