package com.timothypolke.mazegenerator.misc;

import lombok.Getter;
import lombok.Setter;

public class Block3D{
	@Setter
	@Getter
	private int columnID=0;
	@Setter
	@Getter
	private int rowID=0;
	@Setter
	@Getter
	private int layerID=0;
	@Setter
	private boolean open=false;
	@Getter
	private float[][] points=null;
	
	public Block3D(int columnID, int rowID, int layerID, int startX, int startY, int startZ, int sizeX, int sizeY, int sizeZ, boolean open){
		setColumnID(columnID);
		setRowID(rowID);
		setLayerID(layerID);
		setStartX();
		setStartY();
		setStartZ();
		setSizeX();
		setSizeY();
		setSizeZ();
		setOpen(open);
		
		createPoints(startX, startY, startZ, sizeX, sizeY, sizeZ);
	}

	public void setStartX(){
	}

	public void setStartY(){
	}

	public void setStartZ(){
	}

	public void setSizeX(){
	}

	public void setSizeY(){
	}

	public void setSizeZ(){
	}

	public boolean getOpen(){
		return open;
	}

	public void createPoints(float originX, float originY, float originZ, int xChange, int yChange, int zChange){
		points = new float[8][3];
		
		points[0] = new float[]{originX + 0, originY + 0, originZ + 0}; //0,0,0
		points[1] = new float[]{originX + 0, originY + yChange, originZ + 0}; //0,1,0
		points[2] = new float[]{originX + xChange, originY + yChange, originZ + 0}; //1,1,0
		points[3] = new float[]{originX + xChange, originY + 0, originZ + 0}; //1,0,0
		points[4] = new float[]{originX + 0, originY + 0, originZ + zChange}; //0,0,1
		points[5] = new float[]{originX + 0, originY + yChange, originZ + zChange}; //0,1,1
		points[6] = new float[]{originX + xChange, originY + yChange, originZ + zChange}; //1,1,1
		points[7] = new float[]{originX + xChange, originY + 0, originZ + zChange}; //1,0,1
	}
}