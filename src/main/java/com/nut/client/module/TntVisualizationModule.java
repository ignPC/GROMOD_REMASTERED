package com.nut.client.module;

import com.nut.client.annotation.AutoInit;
import com.nut.client.annotation.Component;
import com.nut.client.utils.*;
import net.minecraft.util.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.AxisAlignedBB;

import static com.nut.client.utils.ConsoleUtils.printC;

@Component
public class TntVisualizationModule {

    public static TntVisualizationModule instance;

    public final List<BEntity> explosionList = new ArrayList<>();
    public final List<List<BEntity>> totalRecording;

    public boolean recordingExplosion = false;
    public boolean recordingFirstExplosion = false;
    public boolean simulating = false;

    private int ticksSinceExplosion = 0;

    private int currentEntityPointer = 0;
    private int currentTickPointer = 0;

    private BVector3d currentPos;

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
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (!recordingFirstExplosion) return;

        if (!explosionList.isEmpty()) {

            if (ticksSinceExplosion == 0) {
                MessageUtils.addClientMessage("First Explosion Detected");
                printC("=============================", Color.YELLOW);
                printC("TIMER STARTED\n", Color.YELLOW);

                for (int i = 0; i < 60; i++) {
                    totalRecording.set(i, null);
                }
            }

            recordingExplosion = true;

            Utils.liveDebug(ticksSinceExplosion, explosionList);

            List<BEntity> copyExplosionList = new ArrayList<>(explosionList);
            totalRecording.set(ticksSinceExplosion, copyExplosionList);
            explosionList.clear();

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

            currentPos = totalRecording.get(currentTickPointer).get(currentEntityPointer).getExplosionPos();

            Utils.debugTotal(totalRecording);
            startSim();
        }
    }


    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if(simulating){
            RenderUtils.drawCube(currentPos.x - 0.49f, currentPos.y, currentPos.z - 0.49f, 1f, 0.98f, 0.98f, new BColor(1.0f, 0.0f, 0.0f, 1.0f), TextureType.NONE);
        }
    }

    public void startSim(){
        simulating = true;
    }

    public void showNextGametick() {
        int originalTickPointer = currentTickPointer;
        int originalEntityPointer = currentEntityPointer;

        currentEntityPointer++;

        // if entity pointer not out of range set current pos to next in list
        if (currentEntityPointer < totalRecording.get(currentTickPointer).size()) {
            currentPos = totalRecording.get(currentTickPointer).get(currentEntityPointer).getExplosionPos();
            MessageUtils.addClientMessage("Showing Tick: " + String.format("%02d", currentTickPointer) + " Order: " + String.format("%02d", currentEntityPointer));
            return;
        }

        // while tick pointer < total size, and no data found or entity pointer greater than entity size, decrement tick pointer
        while (currentTickPointer < totalRecording.size() && (totalRecording.get(currentTickPointer) == null || currentEntityPointer >= totalRecording.get(currentTickPointer).size())) {
            currentTickPointer++;
            currentEntityPointer = 0;
        }

        // if while statement didn't find data, go back to original values
        if (currentTickPointer >= totalRecording.size()) {
            currentTickPointer = originalTickPointer;
            currentEntityPointer = originalEntityPointer;
            MessageUtils.addClientMessage("end of recording reached...");
            return;
        }

        // if while statement did find data, set position to pointer
        currentPos = totalRecording.get(currentTickPointer).get(currentEntityPointer).getExplosionPos();

        MessageUtils.addClientMessage("Showing Tick: " + String.format("%02d", currentTickPointer) + " Order: " + String.format("%02d", currentEntityPointer));
    }

    public void showPreviousGametick() {
        int originalTickPointer = currentTickPointer;
        int originalEntityPointer = currentEntityPointer;

        currentEntityPointer--;

        // if entity pointer not out of range set current pos to previous in list
        if (currentEntityPointer >= 0) {
            currentPos = totalRecording.get(currentTickPointer).get(currentEntityPointer).getExplosionPos();
            MessageUtils.addClientMessage("Showing Tick: " + String.format("%02d", currentTickPointer) + " Order: " + String.format("%02d", currentEntityPointer));
            return;
        }

        // while tick pointer >= 0 and no data found or entity pointer less than 0, decrement tick pointer
        while (currentTickPointer >= 0 && (totalRecording.get(currentTickPointer) == null || currentEntityPointer < 0)) {
            currentTickPointer--;
            currentEntityPointer = 0;
        }

        // if while statement didn't find data, go back to original values
        if (currentTickPointer < 0) {
            currentTickPointer = originalTickPointer;
            currentEntityPointer = originalEntityPointer;
            MessageUtils.addClientMessage("end of recording reached...");
            return;
        }

        // if while statement did find data, set position to pointer
        currentPos = totalRecording.get(currentTickPointer).get(currentEntityPointer).getExplosionPos();

        MessageUtils.addClientMessage("Showing Tick: " + String.format("%02d", currentTickPointer) + " Order: " + String.format("%02d", currentEntityPointer));
    }


    public void startRecording() {
        if(simulating)
            stopVisualisation();

        if(!recordingExplosion)
            recordingFirstExplosion = true;

        MessageUtils.addClientMessage("Waiting For Explosions...");
    }

    public void stopVisualisation(){
        if(simulating)
            simulating = false;

        for (int i = 0; i < 60; i++) {
            totalRecording.set(i, null);
        }

        currentTickPointer = 0;
        currentEntityPointer = 0;

        MessageUtils.addClientMessage("Stopping TNT Visualisation...");
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

        private static void liveDebug(int ticksSinceExplosion, List<BEntity> explosionList){
            printC("======START GAMETICK " + String.format("%02d", ticksSinceExplosion) + "======", Color.GREEN);
            int i = 1;
            for(BEntity bEntity : explosionList) {
                BVector3d vec = bEntity.currentPos;
                String formattedVec = String.format("%.2f %.2f %.2f", vec.x, vec.y, vec.z);
                printC(i + ": " + formattedVec, Color.RED);
                i++;
            }
            printC("========END GAMETICK " + String.format("%02d", ticksSinceExplosion) + "======\n", Color.GREEN);
        }

        private static void debugTotal(List<List<BEntity>> totalRecording){
            printC("TIMER ENDED", Color.YELLOW);
            printC("=============================\n", Color.YELLOW);

            for (List<BEntity> orderList : totalRecording) {
                if(orderList == null) continue;
                for(BEntity bEntity: orderList){
                    BVector3d vec = bEntity.currentPos;
                    if(vec == null) continue;
                    String formattedVec = String.format("%.2f %.2f %.2f", vec.x, vec.y, vec.z);
                    String tick = Utils.formatNum(totalRecording.indexOf(orderList));
                    String order = Utils.formatNum(orderList.indexOf(bEntity));
                    printC("TICK " + tick + ": ORDER " + order + ": [" + formattedVec + "]", Color.RED);
                    MessageUtils.addClientMessage("TICK " + tick + ": ORDER " + order + ": [" + formattedVec + "]");
                }
                printC("=========================================", Color.RED);
            }
        }
    }
}




