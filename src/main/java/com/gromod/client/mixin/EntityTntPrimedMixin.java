package com.gromod.client.mixin;

import com.gromod.client.MainBean;
import com.gromod.client.module.patching.PatchingFpsModule;
import com.gromod.client.renderer.entity.CustomTntRenderer;
import com.gromod.client.utils.BEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;

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

        ArrayList<Entity> renderEntityList = MainBean.getInstance().getCustomEntityRenderer().getCustomTNT().getRenderEntityList();

        if(!renderEntityList.contains(this) && PatchingFpsModule.getInstance().isPatchingFps()) {
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
            return;
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
            renderEntityList.remove(this);

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

    /**
     * @author pc
     * @reason idk
     */
    @Overwrite
    public boolean canBeCollidedWith()
    {
        return false;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {

    }
}
