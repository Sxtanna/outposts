package com.sxtanna.mc.posts.hook;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import com.sxtanna.mc.posts.base.Moved;
import com.sxtanna.mc.posts.base.State;
import com.sxtanna.mc.posts.util.Cuboid;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class HookWorldGuard implements State
{

	private final Plugin                          plugin;
	private final List<Moved>                     cached = new ArrayList<>();
	private       WeakReference<WorldGuardPlugin> hooked;


	public HookWorldGuard(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public void load()
	{
		try
		{
			final var plugin = WGBukkit.getPlugin();
			plugin.getSessionManager().registerHandler(new MoveFactory(), null);

			hooked = new WeakReference<>(plugin);
		}
		catch (final Throwable ex)
		{
			hooked = new WeakReference<>(null);

			plugin.getLogger().log(Level.WARNING, "failed to load worldguard hook", ex);
		}
	}

	@Override
	public void kill()
	{
		cached.clear();

		if (hooked == null)
		{
			return;
		}

		hooked.clear();
		hooked = null;
	}


	public void addMoveHandler(@NotNull final Moved moved)
	{
		cached.add(moved);
	}

	public Optional<Cuboid> getCuboidOfRegion(@NotNull final World world, @NotNull final String name)
	{
		final var hooked = this.hooked.get();

		if (hooked == null)
		{
			return Optional.empty();
		}

		final var region = hooked.getRegionManager(world).getRegion(name);
		if (region == null)
		{
			return Optional.empty();
		}


		final var min = region.getMinimumPoint();
		final var max = region.getMaximumPoint();

		return Optional.of(Cuboid.of(new Vector(min.getX(), min.getY(), min.getZ()),
									 new Vector(max.getX(), max.getY(), max.getZ())));
	}


	private final class MoveFactory extends Handler.Factory<MoveHandler>
	{

		@Override
		public MoveHandler create(final Session session)
		{
			return new MoveHandler(session);
		}

	}

	private final class MoveHandler extends Handler
	{

		public MoveHandler(final Session session)
		{
			super(session);
		}


		@Override
		public boolean onCrossBoundary(final Player player, final Location from, final Location to, final ApplicableRegionSet toSet, final Set<ProtectedRegion> intoRegionSet, final Set<ProtectedRegion> fromRegionSet, final MoveType moveType)
		{
			final var fromRegions = fromRegionSet.stream().map(ProtectedRegion::getId).collect(Collectors.toSet());
			final var intoRegions = intoRegionSet.stream().map(ProtectedRegion::getId).collect(Collectors.toSet());

			for (final Moved moved : cached)
			{
				moved.moved(player, to.getWorld(), fromRegions, intoRegions);
			}

			return true;
		}

	}

}