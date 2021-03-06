package com.sxtanna.mc.posts.hook;

import com.sxtanna.mc.posts.Outposts;
import com.sxtanna.mc.posts.base.State;
import net.brcdev.shopgui.ShopGuiPlugin;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.modifier.ModifierType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.logging.Level;

public final class HookShopGuiApi implements State
{

	@NotNull
	private final Outposts                     plugin;
	private       WeakReference<ShopGuiPlugin> hooked;


	public HookShopGuiApi(@NotNull final Outposts plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public void load()
	{
		try
		{
			hooked = new WeakReference<>(ShopGuiPlusApi.getPlugin());
		}
		catch (final Throwable ex)
		{
			hooked = new WeakReference<>(null);

			plugin.getLogger().log(Level.WARNING, "failed to load shopguiplus hook", ex);
		}
	}

	@Override
	public void kill()
	{
		if (hooked == null)
		{
			return;
		}

		hooked.clear();
		hooked = null;
	}


	public void setSellBooster(final double multiplier, @NotNull final Player player)
	{
		final var hooked = this.hooked.get();
		if (hooked == null)
		{
			return;
		}

		if (!hooked.getPlayerManager().isPlayerLoaded(player))
		{
			return; // maybe load them?
		}

		final var data = hooked.getPlayerManager().getPlayerData(player);
		data.getPriceModifiers().setGlobalModifier(ModifierType.SELL, multiplier);
	}

}