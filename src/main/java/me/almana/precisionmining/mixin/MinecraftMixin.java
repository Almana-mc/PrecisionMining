package me.almana.precisionmining.mixin;

import me.almana.precisionmining.PrecisionMiningState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow @Final public Options options;

    @Inject(method = "startAttack", at = @At("HEAD"))
    private void precisionMining$resetOnClick(CallbackInfoReturnable<Boolean> cir) {
        PrecisionMiningState.setAwaitingRelease(false);
    }

    // down is false on instant-break ticks
    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void precisionMining$holdUntilRelease(boolean down, CallbackInfo ci) {
        if (!this.options.keyAttack.isDown() || !PrecisionMiningState.isEnabled()) {
            PrecisionMiningState.setAwaitingRelease(false);
        } else if (PrecisionMiningState.isAwaitingRelease()) {
            ci.cancel();
        }
    }
}
