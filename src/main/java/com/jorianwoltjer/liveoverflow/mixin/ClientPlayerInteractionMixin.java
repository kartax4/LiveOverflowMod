package com.jorianwoltjer.liveoverflow.mixin;

import com.jorianwoltjer.liveoverflow.LiveOverflowMod;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.jorianwoltjer.liveoverflow.client.Keybinds.mc;
import static com.jorianwoltjer.liveoverflow.client.Keybinds.networkHandler;

@Mixin(net.minecraft.client.network.ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionMixin {
    // Insta-Mine hack
    @Inject(method = "attackBlock", at = @At(value = "HEAD"), cancellable = true)
    private void attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        assert mc.world != null;
        assert mc.player != null;
        BlockState blockState = mc.world.getBlockState(pos);
        double speed = blockState.calcBlockBreakingDelta(mc.player, mc.world, pos);
        LiveOverflowMod.LOGGER.info("Block breaking speed: " + speed);
        if (!blockState.isAir() && speed > 0.5F) {  // If you can break the block fast enough, break it instantly
            mc.world.breakBlock(pos, true, mc.player);
            networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction));
            networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, pos, direction));
            cir.setReturnValue(true);  // Return true to break the block on the client-side
        }
    }
}
