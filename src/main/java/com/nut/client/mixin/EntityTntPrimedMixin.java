package com.nut.client.mixin;

import com.nut.client.utils.BEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityTNTPrimed.class)
public abstract class EntityTntPrimedMixin extends Entity {

    public BEntity bEntity;

    @Shadow public int fuse;

    @Shadow protected abstract void explode();

    public EntityTntPrimedMixin(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        this.bEntity = new BEntity(this.posX, this.posY, this.posZ);
    }

    /**
     * @author Gromit + PC
     * @reason Remove ugly ass smoke particle + send info to TNTVisualizationModule
     */
    @Overwrite
    public void onUpdate()
    {
        if(!(this.fuse - 1 <= 0 && !this.worldObj.isRemote)){
            this.bEntity.setCurrentPos(this.posX, this.posY, this.posZ);
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
