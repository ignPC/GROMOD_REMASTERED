package com.nut.client.module;

import akka.io.Tcp;
import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.utils.MessageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import scala.collection.parallel.ParIterableLike;

import static com.nut.client.utils.ConsoleUtils.printC;

@Component
public class TntVisualizationModule {

    public static TntVisualizationModule instance;

    public final List<Vector3d> explosionList = new ArrayList<>();
    public final List<List<Vector3d>> totalRecording;

    private int ticksSinceExplosion = 0;
    public boolean recordingExplosion = false;
    public boolean recordingFirstExplosion = false;

    @AutoInit
    public TntVisualizationModule() {

        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
        totalRecording = new ArrayList<>();

        for (int i = 0; i < 60; i++) {
            totalRecording.add(null);
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent e){
        if(!recordingFirstExplosion) return;

        if(!explosionList.isEmpty()){

            if(ticksSinceExplosion == 0){
                MessageUtils.addClientMessage("First Explosion Detected");
                printC("=============================", Color.YELLOW);
                printC("TIMER STARTED\n", Color.YELLOW);
            }

            debugGametick();

            List<Vector3d> copyExplosionList = new ArrayList<>(explosionList);
            totalRecording.set(ticksSinceExplosion, copyExplosionList);
            explosionList.clear();
            recordingExplosion = true;

            handleTimer();
        }
        else if (recordingExplosion){
            handleTimer();
        }
    }

    public void handleTimer(){
        ticksSinceExplosion++;
        if(ticksSinceExplosion >= 60) {
            ticksSinceExplosion = 0;
            recordingExplosion = false;
            recordingFirstExplosion = false;
            printC("TIMER ENDED", Color.YELLOW);
            printC("=============================\n", Color.YELLOW);

            for (List<Vector3d> orderList : totalRecording) {
                if(orderList == null) continue;
                for(Vector3d vec: orderList){
                    if(vec == null) continue;
                    String formattedVec = String.format("%.2f %.2f %.2f", vec.x, vec.y, vec.z);
                    String tick = Utils.formatNum(totalRecording.indexOf(orderList));
                    String order = Utils.formatNum(orderList.indexOf(vec));
                    printC("TICK " + tick + ": ORDER " + order + ": [" + formattedVec + "]", Color.RED);
                    MessageUtils.addClientMessage("TICK " + tick + ": ORDER " + order + ": [" + formattedVec + "]");
                }
                printC("=========================================", Color.RED);
            }

            for (int i = 0; i < 60; i++) {
                totalRecording.set(i, null);
            }
        }
    }

    public void showNextGametick(){

    }

    public void showPreviousGametick(){

    }

    public void startRecording() {
        if(!recordingExplosion)
            recordingFirstExplosion = true;

        MessageUtils.addClientMessage("Waiting For Explosions...");
    }

    public void debugGametick(){
        printC("======START GAMETICK " + String.format("%02d", ticksSinceExplosion) + "======", Color.GREEN);
        int i = 1;
        for(Vector3d vec : explosionList) {
            String formattedVec = String.format("%.2f %.2f %.2f", vec.x, vec.y, vec.z);
            printC(i + ": " + formattedVec, Color.RED);
            i++;
        }
        printC("========END GAMETICK " + String.format("%02d", ticksSinceExplosion) + "======\n", Color.GREEN);
    }

    public static TntVisualizationModule getInstance(){
        return instance;
    }

    static class Utils {
        public static AxisAlignedBB getAABB(double x, double y, double z) {
            int minX = (int) Math.floor(x);
            int minY = (int) Math.floor(y);
            int minZ = (int) Math.floor(z);
            int maxX = minX + 1;
            int maxY = minY + 1;
            int maxZ = minZ + 1;
            return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ );
        }

        public static String formatNum(String numb){
            return String.format("%02d", Integer.parseInt(numb));
        }

        public static String formatNum(int numb){
            return String.format("%02d", numb);
        }
    }
}




