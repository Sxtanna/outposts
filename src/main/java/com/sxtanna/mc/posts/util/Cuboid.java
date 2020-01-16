package com.sxtanna.mc.posts.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Objects;

public final class Cuboid
{

	private static final Cuboid NONE = Cuboid.of(new Vector(0, 0, 0), new Vector(0, 0, 0), null);


	@NotNull
	private final Vector min;
	@NotNull
	private final Vector max;

	@NotNull
	private final WeakReference<World> world;


	private Cuboid(@NotNull final Vector min, @NotNull final Vector max, @Nullable final World world)
	{
		this.min = min;
		this.max = max;
		this.world = new WeakReference<>(world);
	}


	@NotNull
	public Vector getMin()
	{
		return min;
	}

	@NotNull
	public Vector getMax()
	{
		return max;
	}

	@Nullable
	public World getWorld()
	{
		return world.get();
	}


	public boolean inside(@NotNull final Location location)
	{
		final var world = getWorld();
		if (world == null || location.getWorld() == null || !world.getUID().equals(location.getWorld().getUID()))
		{
			return false;
		}

		final var vector = location.toVector();
		vector.setX(vector.getBlockX());
		vector.setY(vector.getBlockY());
		vector.setZ(vector.getBlockZ());

		return vector.isInAABB(getMin(), getMax());
	}

	public boolean nearby(@NotNull final Location location, final double radius)
	{
		final var world = getWorld();
		if (world == null || location.getWorld() == null || !world.getUID().equals(location.getWorld().getUID()))
		{
			return false;
		}

		return location.toVector().isInSphere(getMax().getMidpoint(getMin()), radius);
	}


	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof Cuboid))
		{
			return false;
		}

		final Cuboid cuboid = (Cuboid) o;
		return getMin().equals(cuboid.getMin()) && getMax().equals(cuboid.getMax());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getMin(), getMax());
	}

	@Override
	public String toString()
	{
		return String.format("Cuboid[min: %s, max: %s]", min, max);
	}


	@NotNull
	public static Cuboid of(@NotNull final Vector min, @NotNull final Vector max, @NotNull final World world)
	{
		return new Cuboid(min, max, world);
	}

	@NotNull
	public static Cuboid none()
	{
		return NONE;
	}

}