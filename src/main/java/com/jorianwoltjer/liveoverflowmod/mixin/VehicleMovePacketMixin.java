package com.jorianwoltjer.liveoverflowmod.mixin;

import com.jorianwoltjer.liveoverflowmod.helper.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.jorianwoltjer.liveoverflowmod.client.ClientEntrypoint.passiveMods;

@Mixin(VehicleMoveC2SPacket.class)
public class VehicleMovePacketMixin {
    // Anti-human bypass for X
    @Redirect(method = "<init>(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getX()D"))
    public double getX(Entity instance){
        if (passiveMods.enabled) {
            return Utils.roundCoordinate(instance.getX());
        } else {
            return instance.getX();
        }
    }
    // Anti-human bypass for Z
    @Redirect(method = "<init>(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getZ()D"))
    public double getZ(Entity instance) {
        if (passiveMods.enabled) {
            return Utils.roundCoordinate(instance.getZ());
        } else {
            return instance.getX();
        }
    }
}
