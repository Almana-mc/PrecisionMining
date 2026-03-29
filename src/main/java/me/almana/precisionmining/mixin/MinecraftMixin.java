package me.almana.precisionmining.mixin;

import me.almana.precisionmining.PrecisionMiningState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow @Nullable public ClientLevel level;
    @Shadow @Nullable public MultiPlayerGameMode gameMode;
    @Shadow @Nullable public LocalPlayer player;
    @Shadow public Options options;

    @Unique private boolean precisionMining$awaitAttackReset = false;

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void precisionMining$holdAttackUntilRelease(boolean isBreaking, CallbackInfo ci) {
        if (!PrecisionMiningState.isEnabled() || !this.options.keyAttack.isDown()) {
            this.precisionMining$awaitAttackReset = false;
            return;
        }

        if (this.precisionMining$awaitAttackReset && isBreaking) {
            this.gameMode.stopDestroyBlock();
            ci.cancel();
        }
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void precisionMining$cancelStartUntilRelease(CallbackInfoReturnable<Boolean> cir) {
        if (!PrecisionMiningState.isEnabled()) {
            this.precisionMining$awaitAttackReset = false;
            return;
        }

        if (this.precisionMining$awaitAttackReset) {
            this.gameMode.stopDestroyBlock();
            cir.setReturnValue(false);
        }
    }

    @Redirect(
            method = "startAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"
            )
    )
    private boolean precisionMining$trackStartedBreak(MultiPlayerGameMode gameMode, BlockPos pos, Direction direction) {
        boolean instantMineTarget = PrecisionMiningState.isEnabled() && this.precisionMining$canInstantMine(pos);
        boolean startedDestroy = gameMode.startDestroyBlock(pos, direction);
        if (instantMineTarget && startedDestroy) {
            this.precisionMining$awaitAttackReset = true;
        } else if (PrecisionMiningState.isEnabled() && this.precisionMining$brokeBlock(pos)) {
            this.precisionMining$awaitAttackReset = true;
        }
        return startedDestroy;
    }

    @Redirect(
            method = "continueAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;continueDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"
            )
    )
    private boolean precisionMining$trackContinuedBreak(MultiPlayerGameMode gameMode, BlockPos pos, Direction direction) {
        boolean shouldSwing = gameMode.continueDestroyBlock(pos, direction);
        if (PrecisionMiningState.isEnabled() && this.precisionMining$brokeBlock(pos)) {
            this.precisionMining$awaitAttackReset = true;
        }
        return shouldSwing;
    }

    @Unique
    private boolean precisionMining$brokeBlock(BlockPos pos) {
        return this.level != null && this.level.getBlockState(pos).isAir();
    }

    @Unique
    private boolean precisionMining$canInstantMine(BlockPos pos) {
        if (this.level == null || this.player == null) {
            return false;
        }

        BlockState state = this.level.getBlockState(pos);
        return !state.isAir() && state.getDestroyProgress(this.player, this.level, pos) >= 1.0F;
    }
}
