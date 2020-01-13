package com.sxtanna.mc.posts;

import com.sxtanna.mc.posts.util.Cuboid;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public final class CuboidTests
{

	static final Vector min = new Vector(-10, -10, -10);
	static final Vector max = new Vector(+10, +10, +10);


	@Test
	void testInside()
	{
		final var cuboid = Cuboid.of(min, max);

		assertTrue(cuboid.inside(new Vector(0, 0, 0)));
	}

	@Test
	void testNearby()
	{
		final var cuboid = Cuboid.of(min, max);

		assertTrue(cuboid.nearby(new Vector(15, 15, 15), 20));
	}

}
