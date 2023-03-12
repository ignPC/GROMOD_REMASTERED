package com.nut.client.mixin;

import com.nut.client.annotation.AutoInit;
import com.nut.client.module.TntVisualizationModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityTNTPrimed.class)
public abstract class EntityTntPrimedMixin extends Entity {

    @Shadow public int fuse;

    @Shadow protected abstract void explode();

    public EntityTntPrimedMixin(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {

    }

    /**
     * @author Gromit + PC
     * @reason Remove ugly ass smoke particle + send info to TNTVisualizationModule
     */
    @Overwrite
    public void onUpdate()
    {
        if(this.fuse-- <= 0 && this.worldObj.isRemote){
            TntVisualizationModule tntVis = TntVisualizationModule.getInstance();

            if(tntVis.recordingExplosion || tntVis.recordingFirstExplosion) {
                Vector3d vec = new Vector3d();
                vec.x = this.posX;
                vec.y = this.posY;
                vec.z = this.posZ;

                tntVis.explosionList.add(vec);
            }
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        if (this.fuse-- <= 0)
        {
            this.setDead();

            if (!this.worldObj.isRemote)
            {
                this.explode();
            }
        }
        else
        {
            this.handleWaterMovement();
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {

    }
}