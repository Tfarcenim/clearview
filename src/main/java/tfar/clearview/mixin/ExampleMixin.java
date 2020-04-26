package tfar.clearview.mixin;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.clearview.ClearView;

@Mixin(BackgroundRenderer.class)
public class ExampleMixin {
	@Inject(method = "applyFog",at = @At("HEAD"),cancellable = true)
	private static void noFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci){
		if (ClearView.hook(camera,fogType,viewDistance,thickFog)){
			ci.cancel();
		}
	}
}
