package com.sxtanna.mc.posts.post.base;

import com.sxtanna.mc.posts.base.State;
import com.sxtanna.mc.posts.util.Cuboid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class Outpost implements State
{

	@NotNull
	private final String name;


	private Outpost      capturePrev;
	private Mode         captureMode;
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


	@Override
	public void load()
	{

	}

	@Override
	public void kill()
	{

	}


	@Nullable
	public Outpost getCapturePrev()
	{
		return capturePrev;
	}

	public void setCapturePrev(@Nullable final Outpost capturePrev)
	{
		this.capturePrev = capturePrev;
	}


	@NotNull
	public Mode getCaptureMode()
	{
		return captureMode != null ? captureMode : Mode.NEUTRAL;
	}

	public void setCaptureMode(@NotNull final Mode captureMode)
	{
		this.captureMode = captureMode;
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


	public enum Mode
	{

		NEUTRAL,
		CAPTURED,
		CONTESTED,

	}

}
