package com.sxtanna.mc.posts.util;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class Cuboid
{

	private static final Cuboid NONE = Cuboid.of(new Vector(0, 0, 0), new Vector(0, 0, 0));


	@NotNull
	private final Vector min;
	@NotNull
	private final Vector max;


	private Cuboid(@NotNull final Vector min, @NotNull final Vector max)
	{
		this.min = min;
		this.max = max;
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


	public boolean inside(@NotNull final Vector vector)
	{
		return inside(vector.getX(), vector.getY(), vector.getZ());
	}

	public boolean inside(final double x, final double y, final double z)
	{
		return x >= min.getX() && x <= max.getX() && y >= min.getY() && y <= max.getY() && z >= min.getZ() && z <= max.getZ();
	}

	public boolean nearby(@NotNull final Vector vector, final double radius)
	{
		return vector.isInSphere(getMax().subtract(getMin()), radius);
	}


	@NotNull
	public static Cuboid of(@NotNull final Vector min, @NotNull final Vector max)
	{
		return new Cuboid(min, max);
	}

	@NotNull
	public static Cuboid none()
	{
		return NONE;
	}

}
