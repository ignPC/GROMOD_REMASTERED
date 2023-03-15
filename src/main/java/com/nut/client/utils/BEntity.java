package com.nut.client.utils;

import java.util.ArrayList;
import java.util.List;

public class BEntity {
    public BVector3d currentPos;
    public BVector3d originalPos;
    public BVector3d explosionPos;
    public BVector3d previousPos;

    public List<BVector3d> positionHistory = new ArrayList<>();

    public BEntity(double x, double y, double z) {
        this.currentPos = new BVector3d(x, y, z);
        this.originalPos = this.currentPos;

        this.previousPos = null;
        this.explosionPos = null;
    }

    public BEntity setCurrentPos(double x, double y, double z) {
        this.previousPos = this.currentPos;
        this.currentPos.set(x, y, z);

        positionHistory.add(currentPos);
        return this;
    }

    public BEntity setExplosionPos(double x, double y, double z){
        this.previousPos = this.currentPos;
        this.currentPos.set(x, y, z);

        positionHistory.add(currentPos);
        explosionPos = currentPos;
        return this;
    }

    public List<BVector3d> getPositionHistory(){
        return positionHistory;
    }

    public BVector3d getExplosionPos(){
        return explosionPos;
    }

    public BVector3d getCurrentPos(){
        return currentPos;
    }
}
