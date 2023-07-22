package com.gromod.client.mixin;

import com.gromod.client.module.patching.ExplosionParticlesModule;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow @Final private float explosionSize;
    @Shadow @Final private World worldObj;
    @Shadow @Final private double explosionX;
    @Shadow @Final private double explosionY;
    @Shadow @Final private double explosionZ;
    @Shadow @Final private Entity exploder;
    @Shadow @Final private Map<EntityPlayer, Vec3> playerKnockbackMap;
    @Shadow @Final private boolean isSmoking;

    /**
     * @author Gromit
     * @reason Turning Off Explosion Damage To Environment
     */
    @Overwrite
    public void doExplosionA() {
        Explosion explosion = ((Explosion) (Object) this);

        int k;
        int l;
        for(int j = 0; j < 16; ++j) {
            for(k = 0; k < 16; ++k) {
                for(l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = ((float)j / 15.0F * 2.0F - 1.0F);
                        double d1 = ((float)k / 15.0F * 2.0F - 1.0F);
                        double d2 = ((float)l / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = explosionSize * (0.7F + worldObj.rand.nextFloat() * 0.6F);
                        double d4 = explosionX;
                        double d6 = explosionY;
                        double d8 = explosionZ;

                        for(float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            IBlockState iblockstate = worldObj.getBlockState(blockpos);
                            if (iblockstate.getBlock().getMaterial() != Material.air) {
                                float f2 = exploder != null ? exploder.getExplosionResistance(explosion, worldObj, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(worldObj, blockpos, (Entity)null, explosion);
                                f -= (f2 + 0.3F) * 0.3F;
                            }

                            d4 += d0 * 0.30000001192092896;
                            d6 += d1 * 0.30000001192092896;
                            d8 += d2 * 0.30000001192092896;
                        }
                    }
                }
            }
        }

        float f3 = explosionSize * 2.0F;
        k = MathHelper.floor_double(explosionX - (double)f3 - 1.0);
        l = MathHelper.floor_double(explosionX + (double)f3 + 1.0);
        int i2 = MathHelper.floor_double(explosionY - (double)f3 - 1.0);
        int i1 = MathHelper.floor_double(explosionY + (double)f3 + 1.0);
        int j2 = MathHelper.floor_double(explosionZ - (double)f3 - 1.0);
        int j1 = MathHelper.floor_double(explosionZ + (double)f3 + 1.0);
        List<Entity> list = worldObj.getEntitiesWithinAABBExcludingEntity(exploder, new AxisAlignedBB(k, i2, j2, l, i1, j1));
        ForgeEventFactory.onExplosionDetonate(worldObj, explosion, list, f3);
        Vec3 vec3 = new Vec3(explosionX, explosionY, explosionZ);

        for (Entity entity : list) {
            if (!entity.isImmuneToExplosions()) {
                double d12 = entity.getDistance(explosionX, explosionY, explosionZ) / (double) f3;
                if (d12 <= 1.0) {
                    double d5 = entity.posX - explosionX;
                    double d7 = entity.posY + (double) entity.getEyeHeight() - explosionY;
                    double d9 = entity.posZ - explosionZ;
                    double d13 = MathHelper.sqrt_double(d5 * d5 + d7 * d7 + d9 * d9);
                    if (d13 != 0.0) {
                        d5 /= d13;
                        d7 /= d13;
                        d9 /= d13;
                        double d14 = worldObj.getBlockDensity(vec3, entity.getEntityBoundingBox());
                        double d10 = (1.0 - d12) * d14;
                        entity.attackEntityFrom(DamageSource.setExplosionSource(explosion), (float) ((int) ((d10 * d10 + d10) / 2.0 * 8.0 * (double) f3 + 1.0)));
                        double d11 = EnchantmentProtection.func_92092_a(entity, d10);
                        entity.motionX += d5 * d11;
                        entity.motionY += d7 * d11;
                        entity.motionZ += d9 * d11;
                        if (entity instanceof EntityPlayer && !((EntityPlayer) entity).capabilities.disableDamage) {
                            playerKnockbackMap.put((EntityPlayer) entity, new Vec3(d5 * d10, d7 * d10, d9 * d10));
                        }
                    }
                }
            }
        }
    }

    /**
     * @author PC
     * @reason Explosion Particles
     */
    @Overwrite
    public void doExplosionB(boolean spawnParticles) {
        this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

        if (this.explosionSize >= 2.0F && this.isSmoking) {
            if (spawnParticles && ExplosionParticlesModule.getInstance().isExplosionparticles()) {
                this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
            }
        } else {
            if (spawnParticles && ExplosionParticlesModule.getInstance().isExplosionparticles()) {
                this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
            }
        }
    }
}
