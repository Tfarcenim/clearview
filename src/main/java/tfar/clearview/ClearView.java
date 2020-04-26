package tfar.clearview;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.tag.FluidTags;

public class ClearView implements ModInitializer {
	public static final String MODID = "clearview";

	@Override
	public void onInitialize() {
		AutoConfig.register(ClientConfig.class, Toml4jConfigSerializer::new);

		ClientConfig.register();
	}

	public static boolean hook(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog){

		if (fogType == BackgroundRenderer.FogType.FOG_SKY && !ClientConfig.config.enable_sky_fog) return true;
		if (fogType == BackgroundRenderer.FogType.FOG_TERRAIN && !ClientConfig.config.enable_terrain_fog) return true;

		FluidState fluidState = camera.getSubmergedFluidState();
		Entity entity = camera.getFocusedEntity();
		boolean fluidnotempty = fluidState.getFluid() != Fluids.EMPTY;
		if (fluidnotempty) {
			if (fluidState.matches(FluidTags.WATER) && !ClientConfig.config.enable_water_fog)  {
				return true;
			}
			if (fluidState.matches(FluidTags.LAVA))
				if (!ClientConfig.config.enable_lava_fog)
					return true;

		}
		if (entity instanceof LivingEntity)
			if (((LivingEntity) entity).hasStatusEffect(StatusEffects.BLINDNESS))
				return !ClientConfig.config.enable_blindness_fog;
		return false;
	}

	@Config(name = ClearView.MODID)
	public static class ClientConfig implements ConfigData {
		public static ClientConfig config;

		public boolean enable_blindness_fog;
		public boolean enable_water_fog;
		public boolean enable_lava_fog;
		public boolean enable_sky_fog;
		public boolean enable_terrain_fog;

		public static void register() {
			config = AutoConfig.getConfigHolder(ClientConfig.class).getConfig();
		}
	}
}
