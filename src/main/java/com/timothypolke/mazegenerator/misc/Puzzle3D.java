package com.timothypolke.mazegenerator.misc;

import lombok.Getter;

import java.util.ArrayList;
import java.security.SecureRandom;

public class Puzzle3D{
	
	private int xCells;
	private int yCells;
	private int zCells;
	private int boundarySize;
	private int cellSize;
	
	@Getter
	private String solved=null;
	@Getter
	private String unsolved=null;
	
	private Block3D[][][] verticalWalls=null;
	private Block3D[][][] horizontalWalls=null;
	private Block3D[][][] flatWalls=null;
	private Block3D[][][] cells=null;
	private Block3D[][][] verticalFrames=null;
	private Block3D[][][] horizontalFrames=null;
	private Block3D[][][] flatFrames=null;
	private Block3D[][][] corners=null;	
	
	private ArrayList<Block3D> route=null;
	private int x=0;
	private int y=0;
	private int z=0;
	
	public Puzzle3D(int xCells,int yCells,int zCells,int boundarySize,int cellSize){
	
		this.xCells=xCells;
		this.yCells=yCells;
		this.zCells=zCells;
		this.boundarySize=boundarySize;
		this.cellSize=cellSize;
		
		create();
		solve();
		
		unsolved = redrawPuzzle();
		solved = redrawSolution();
	}

	public void create(){
		verticalWalls=new Block3D[zCells][yCells][xCells+1];
		horizontalWalls=new Block3D[zCells][yCells+1][xCells];
		flatWalls=new Block3D[zCells+1][yCells][xCells];
		cells=new Block3D[zCells][yCells][xCells];
		
		verticalFrames=new Block3D[zCells+1][yCells+1][xCells+2];
		horizontalFrames=new Block3D[zCells+1][yCells+2][xCells+1];
		flatFrames=new Block3D[zCells+2][yCells+1][xCells+1];
		corners = new Block3D[zCells+2][yCells+2][xCells+2];
		
		createVerticalWalls(xCells+1,yCells,zCells);
		createHorizontalWalls(xCells,yCells+1,zCells);
		createFlatWalls(xCells,yCells,zCells+1);
		createCells();
		
		createVerticalFrames(xCells+2,yCells+1,zCells+1);
		createHorizontalFrames(xCells+1,yCells+2,zCells+1);
		createFlatFrames(xCells+1,yCells+1,zCells+2);
		createCorners(xCells+1,yCells+1,zCells+1);
		
		ArrayList<Block3D> visitedCells=new ArrayList<>();
		
		visitedCells.add(cells[0][0][0]);
		horizontalWalls[0][0][0].setOpen(true);
		horizontalWalls[zCells-1][yCells][xCells-1].setOpen(true);
		
		while(determinePresenceOfClosedCells()){
			chooseDirection(visitedCells.get(visitedCells.size()-1).getColumnID(),visitedCells.get(visitedCells.size()-1).getRowID(),visitedCells.get(visitedCells.size()-1).getLayerID(),visitedCells);
		}	
	}

	public void solve(){
		ArrayList<Block3D> visitedCells = new ArrayList<>();
		
		route=new ArrayList<>();
		route.add(cells[0][0][0]);
		visitedCells.add(cells[0][0][0]);
		
		while(traverseVisited(xCells - 1, yCells - 1, zCells - 1, visitedCells)){
			if(forwardMotion(x, y - 1, z, x, y, z, 0, visitedCells) && forwardMotion(x, y + 1, z, x, y + 1, z, 1, visitedCells) && forwardMotion(x - 1, y, z, x, y, z, 2, visitedCells) && forwardMotion(x + 1, y, z, x + 1, y, z, 3, visitedCells) && forwardMotion(x, y, z - 1, x, y, z, 4, visitedCells) && forwardMotion(x, y, z + 1, x, y, z + 1, 5, visitedCells)){
				route.remove(route.get(route.size()-1));
				route.trimToSize();
				x=route.get(route.size()-1).getColumnID();
				y=route.get(route.size()-1).getRowID();
				z=route.get(route.size()-1).getLayerID();
			}
		}
	}

	public void createVerticalWalls(int xVerticalWalls,int yVerticalWalls,int zVerticalWalls){
		int posX = 0;
		int posY = boundarySize;
		int posZ = boundarySize;
		for(int iz=0;iz<zVerticalWalls;iz++){
			for(int iy=0;iy<yVerticalWalls;iy++){
				for(int ix=0;ix<xVerticalWalls;ix++){
					verticalWalls[iz][iy][ix]=new Block3D(ix,iy,iz,posX,posY,posZ,boundarySize,cellSize,cellSize,false);
					posX = posX + cellSize + boundarySize;
				}
				posY = posY + cellSize + boundarySize;
				posX = 0;
			}
			posZ = posZ + cellSize + boundarySize;
			posY = boundarySize;
		}
	}

	public void createHorizontalWalls(int xHorizontalWalls,int yHorizontalWalls,int zHorizontalWalls){
		int posX = boundarySize;
		int posY = 0;
		int posZ = boundarySize;
		for(int iz=0;iz<zHorizontalWalls;iz++){
			for(int iy=0;iy<yHorizontalWalls;iy++){
				for(int ix=0;ix<xHorizontalWalls;ix++){
					horizontalWalls[iz][iy][ix]=new Block3D(ix,iy,iz,posX,posY,posZ,cellSize,boundarySize,cellSize,false);
					posX = posX + cellSize + boundarySize;
				}
				posY = posY + cellSize + boundarySize;
				posX = boundarySize;
			}
			posZ = posZ + cellSize + boundarySize;
			posY = 0;
		}
	}

	public void createFlatWalls(int xFlatWalls,int yFlatWalls,int zFlatWalls){
		int posX = boundarySize;
		int posY = boundarySize;
		int posZ = 0;
		for(int iz=0;iz<zFlatWalls;iz++){
			for(int iy=0;iy<yFlatWalls;iy++){
				for(int ix=0;ix<xFlatWalls;ix++){
					flatWalls[iz][iy][ix]=new Block3D(ix,iy,iz,posX,posY,posZ,cellSize,cellSize,boundarySize,false);
					posX = posX + cellSize + boundarySize;
				}
				posY = posY + cellSize + boundarySize;
				posX = boundarySize;
			}
			posZ = posZ + cellSize + boundarySize;
			posY = boundarySize;
		}
	}

	public void createCells(){
		int posX = boundarySize;
		int posY = boundarySize;
		int posZ = boundarySize;
		for(int iz=0;iz<zCells;iz++){	
			for(int iy=0;iy<yCells;iy++){		
				for(int ix=0;ix<xCells;ix++){
					cells[iz][iy][ix]=new Block3D(ix,iy,iz,posX,posY,posZ,cellSize,cellSize,cellSize,true);
					posX = posX + cellSize + boundarySize;	
				}
				posY = posY + cellSize + boundarySize;
				posX = boundarySize;
			}
			posZ = posZ + cellSize + boundarySize;
			posY = boundarySize;
		}
	}

	public void createVerticalFrames(int xVerticalFrames,int yVerticalFrames,int zVerticalFrames){
		int posX = boundarySize;
		int posY = 0;
		int posZ = 0;
		for(int iz=0;iz<zVerticalFrames;iz++){
			for(int iy=0;iy<yVerticalFrames;iy++){
				for(int ix=0;ix<xVerticalFrames;ix++){
					verticalFrames[iz][iy][ix]=new Block3D(ix,iy,iz,posX,posY,posZ,cellSize,boundarySize,boundarySize,false);
					posX = posX + cellSize + boundarySize;
				}
				posY = posY + cellSize + boundarySize;
				posX = boundarySize;
			}
			posZ = posZ + cellSize + boundarySize;
			posY = 0;
		}
	}

	public void createHorizontalFrames(int xHorizontalFrames,int yHorizontalFrames,int zHorizontalFrames){
		int posX = 0;
		int posY = boundarySize;
		int posZ = 0;
		for(int iz=0;iz<zHorizontalFrames;iz++){
			for(int iy=0;iy<yHorizontalFrames;iy++){
				for(int ix=0;ix<xHorizontalFrames;ix++){
					horizontalFrames[iz][iy][ix]=new Block3D(ix,iy,iz,posX,posY,posZ,boundarySize,cellSize,boundarySize,false);
					posX = posX + cellSize + boundarySize;
				}
				posY = posY + cellSize + boundarySize;
				posX = 0;
			}
			posZ = posZ + cellSize + boundarySize;
			posY = boundarySize;
		}
	}

	public void createFlatFrames(int xFlatFrames,int yFlatFrames,int zFlatFrames){
		int posX = 0;
		int posY = 0;
		int posZ = boundarySize;
		for(int iz=0;iz<zFlatFrames;iz++){
			for(int iy=0;iy<yFlatFrames;iy++){
				for(int ix=0;ix<xFlatFrames;ix++){
					flatFrames[iz][iy][ix]=new Block3D(ix,iy,iz,posX,posY,posZ,boundarySize,boundarySize,cellSize,false);
					posX = posX + cellSize + boundarySize;
				}
				posY = posY + cellSize + boundarySize;
				posX = 0;
			}
			posZ = posZ + cellSize + boundarySize;
			posY = 0;
		}
	}

	public void createCorners(int xCorners,int yCorners,int zCorners){
		int posX = 0;
		int posY = 0;
		int posZ = 0;
		for(int iz=0;iz<zCorners;iz++){
			for(int iy=0;iy<yCorners;iy++){
				for(int ix=0;ix<xCorners;ix++){
					corners[iz][iy][ix]=new Block3D(ix,iy,iz,posX,posY,posZ,boundarySize,boundarySize,boundarySize,false);
					posX = posX + cellSize + boundarySize;
				}
				posY = posY + cellSize + boundarySize;
				posX = 0;
			}
			posZ = posZ + cellSize + boundarySize;
			posY = 0;
		}
	}

	public void chooseDirection(int posX,int posY,int posZ,ArrayList<Block3D> visitedCells) {
		ArrayList<Integer> uncheckedDirections = new ArrayList<>();
		uncheckedDirections.add(0);
		uncheckedDirections.add(1);
		uncheckedDirections.add(2);
		uncheckedDirections.add(3);
		uncheckedDirections.add(4);
		uncheckedDirections.add(5);
		
		SecureRandom rand = new SecureRandom();
		int direction;
		int xChange = 0;
		int yChange = 0;
		int zChange = 0;
		int xResult = 0;
		int yResult = 0;
		int zResult = 0;
		int type = 0;
		while (!uncheckedDirections.isEmpty()) {
			direction = uncheckedDirections.get(rand.nextInt(uncheckedDirections.size()));
			
			if (direction == 0) {
				xChange = posX - 1;
				yChange = posY;
				zChange = posZ;
				xResult = posX;
				yResult = posY;
				zResult = posZ;
				type = 0;
			} else if (direction == 1) {
				xChange = posX + 1;
				yChange = posY;
				zChange = posZ;
				xResult = posX + 1;
				yResult = posY;
				zResult = posZ;
				type = 0;
			} else if (direction == 2) {
				xChange = posX;
				yChange = posY - 1;
				zChange = posZ;
				xResult = posX;
				yResult = posY;
				zResult = posZ;
				type = 1;
			} else if (direction == 3) {
				xChange = posX;
				yChange = posY + 1;
				zChange = posZ;
				xResult = posX;
				yResult = posY + 1;
				zResult = posZ;
				type = 1;
			} else if (direction == 4) {
				xChange = posX;
				yChange = posY;
				zChange = posZ - 1;
				xResult = posX;
				yResult = posY;
				zResult = posZ;
				type = 2;
			} else if (direction == 5) {
				xChange = posX;
				yChange = posY;
				zChange = posZ + 1;
				xResult = posX;
				yResult = posY;
				zResult = posZ + 1;
				type = 2;
			}
			
			try {
				if (!checkVisitedStatus(cells[zChange][yChange][xChange], visitedCells)) {
					if (type == 0) {
						verticalWalls[zResult][yResult][xResult].setOpen(true);
					}
					else if (type == 1) {
						horizontalWalls[zResult][yResult][xResult].setOpen(true);
					}
					else if (type == 2) {
						flatWalls[zResult][yResult][xResult].setOpen(true);
					}
				}
				visitedCells.add(cells[zChange][yChange][xChange]);
				uncheckedDirections.remove(direction);
				break;
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean determinePresenceOfClosedCells(){
		boolean present=false;
		loop:
		for(int posZ=0;posZ<zCells;posZ++){
			for(int posY=0;posY<yCells;posY++){
				for(int posX=0;posX<xCells;posX++){
					if(!horizontalWalls[cells[posZ][posY][posX].getLayerID()][cells[posZ][posY][posX].getRowID()][cells[posZ][posY][posX].getColumnID()].getOpen() && !horizontalWalls[cells[posZ][posY][posX].getLayerID()][cells[posZ][posY][posX].getRowID() + 1][cells[posZ][posY][posX].getColumnID()].getOpen() && !verticalWalls[cells[posZ][posY][posX].getLayerID()][cells[posZ][posY][posX].getRowID()][cells[posZ][posY][posX].getColumnID()].getOpen() && !verticalWalls[cells[posZ][posY][posX].getLayerID()][cells[posZ][posY][posX].getRowID()][cells[posZ][posY][posX].getColumnID() + 1].getOpen() && !flatWalls[cells[posZ][posY][posX].getLayerID()][cells[posZ][posY][posX].getRowID()][cells[posZ][posY][posX].getColumnID()].getOpen() && !flatWalls[cells[posZ][posY][posX].getLayerID() + 1][cells[posZ][posY][posX].getRowID()][cells[posZ][posY][posX].getColumnID()].getOpen()){
						present=true;
						break loop;
					}
				}
			}
		}
		return present;
	}

	public boolean checkVisitedStatus(Block3D cell,ArrayList<Block3D> visitedCells){
		boolean result=false;
		for (Block3D visitedCell : visitedCells) {
			if (visitedCell.getColumnID() == cell.getColumnID() && visitedCell.getRowID() == cell.getRowID() && visitedCell.getLayerID() == cell.getLayerID()) {
				result = true;
				break;
			}
		}
		return result;
	}

	public boolean forwardMotion(int x1,int y1,int z1,int x2,int y2,int z2,int direction,ArrayList<Block3D> history){
		boolean failed=true;
		try{
			if (direction==0 || direction==1){
				if(traverseVisited(x1, y1, z1, history) && horizontalWalls[z2][y2][x2].getOpen()){
					route.add(cells[z1][y1][x1]);
					history.add(cells[z1][y1][x1]);
					x=x1;
					y=y1;
					z=z1;
					failed=false;
				}
			}
			else if (direction == 2 || direction == 3){
				if(traverseVisited(x1, y1, z1, history) && verticalWalls[z2][y2][x2].getOpen()){
					route.add(cells[z1][y1][x1]);
					history.add(cells[z1][y1][x1]);
					x=x1;
					y=y1;
					z=z1;
					failed=false;
				}
			}
			else if (direction==4 || direction==5){
				if(traverseVisited(x1, y1, z1, history) && flatWalls[z2][y2][x2].getOpen()){
					route.add(cells[z1][y1][x1]);
					history.add(cells[z1][y1][x1]);
					x=x1;
					y=y1;
					z=z1;
					failed=false;
				}
			}
		}
		catch(IndexOutOfBoundsException e){
			e.printStackTrace();
		}
		return failed;
	}

	public boolean traverseVisited(int varX,int varY,int varZ,ArrayList<Block3D> history){
		boolean found=false;
		for (Block3D block3D : history) {
			if (block3D.getColumnID() == varX && block3D.getRowID() == varY && block3D.getLayerID() == varZ) {
				found = true;
				break;
			}
		}
		return !found;
	}

	public String redrawPuzzle(){
		StringBuilder objData = new StringBuilder();
		int counter = 0;	
		
		//flat wall variables
		float[][][][][] flatWallsPoints = new float[zCells+1][yCells][xCells][8][3];
		String[][][][][] flatWallsFaces = new String[zCells+1][yCells][xCells][6][4];
		String[][][][] flatWallsPointsRef = new String[zCells+1][yCells][xCells][8];
		
		//horizontal wall variables
		float[][][][][] horizontalWallsPoints = new float[zCells][yCells+1][xCells][8][3];
		String[][][][][] horizontalWallsFaces = new String[zCells][yCells+1][xCells][6][4];
		String[][][][] horizontalWallsPointsRef = new String[zCells][yCells+1][xCells][8];	
		
		//vertical wall variables
		float[][][][][] verticalWallsPoints = new float[zCells][yCells][xCells+1][8][3];
		String[][][][][] verticalWallsFaces = new String[zCells][yCells][xCells+1][6][4];
		String[][][][] verticalWallsPointsRef = new String[zCells][yCells][xCells+1][8];
		
		//flat frame variables
		float[][][][][] flatFramesPoints = new float[zCells][yCells+1][xCells+1][8][3];
		String[][][][][] flatFramesFaces = new String[zCells][yCells+1][xCells+1][6][4];
		String[][][][] flatFramesPointsRef = new String[zCells][yCells+1][xCells+1][8];
		
		//horizontal frame variables
		float[][][][][] horizontalFramesPoints = new float[zCells+1][yCells][xCells+1][8][3];
		String[][][][][] horizontalFramesFaces = new String[zCells+1][yCells][xCells+1][6][4];
		String[][][][] horizontalFramesPointsRef = new String[zCells+1][yCells][xCells+1][8];
		
		//vertical frame variables
		float[][][][][] verticalFramesPoints = new float[zCells+1][yCells+1][xCells][8][3];
		String[][][][][] verticalFramesFaces = new String[zCells+1][yCells+1][xCells][6][4];
		String[][][][] verticalFramesPointsRef = new String[zCells+1][yCells+1][xCells][8];
		
		//corners variables
		float[][][][][] cornersPoints = new float[zCells+1][yCells+1][xCells+1][8][3];
		String[][][][][] cornersFaces = new String[zCells+1][yCells+1][xCells+1][6][4];
		String[][][][] cornersPointsRef = new String[zCells+1][yCells+1][xCells+1][8];
		
		//flat wall vertices
		for(int iz=0;iz<zCells+1;iz++){
			for(int iy=0;iy<yCells;iy++){
				for(int ix=0;ix<xCells;ix++){
					flatWallsPoints[iz][iy][ix] = flatWalls[iz][iy][ix].getPoints();
					for (int i0=0;i0<8;i0++){
						counter = counter + 1;
						flatWallsPointsRef[iz][iy][ix][i0] = String.valueOf(counter);
						objData.append("v ");
						for (int i1=0;i1<3;i1++){
							objData.append(flatWallsPoints[iz][iy][ix][i0][i1]).append(" ");
						}
						objData.append("\n");
					}
				}
			}				
		}
		
		//horizontal wall vertices
		for(int iz=0;iz<zCells;iz++){
			for(int iy=0;iy<yCells+1;iy++){
				for(int ix=0;ix<xCells;ix++){
					horizontalWallsPoints[iz][iy][ix] = horizontalWalls[iz][iy][ix].getPoints();
					for (int i0=0;i0<8;i0++){
						counter = counter + 1;
						horizontalWallsPointsRef[iz][iy][ix][i0] = String.valueOf(counter);
						objData.append("v ");
						for (int i1=0;i1<3;i1++){
							objData.append(horizontalWallsPoints[iz][iy][ix][i0][i1]).append(" ");
						}
						objData.append("\n");
					}
				}
			}				
		}
		
		//vertical wall vertices
		for(int iz=0;iz<zCells;iz++){
			for(int iy=0;iy<yCells;iy++){
				for(int ix=0;ix<xCells+1;ix++){
					verticalWallsPoints[iz][iy][ix] = verticalWalls[iz][iy][ix].getPoints();
					for (int i0=0;i0<8;i0++){
						counter = counter + 1;
						verticalWallsPointsRef[iz][iy][ix][i0] = String.valueOf(counter);
						objData.append("v ");
						for (int i1=0;i1<3;i1++){
							objData.append(verticalWallsPoints[iz][iy][ix][i0][i1]).append(" ");
						}
						objData.append("\n");
					}
				}
			}				
		}
		
		//flat wall faces
		for(int iz=0;iz<zCells+1;iz++){
			for(int iy=0;iy<yCells;iy++){
				for(int ix=0;ix<xCells;ix++){
					flatWallsFaces[iz][iy][ix][0] = new String[]{flatWallsPointsRef[iz][iy][ix][0],flatWallsPointsRef[iz][iy][ix][1],flatWallsPointsRef[iz][iy][ix][2],flatWallsPointsRef[iz][iy][ix][3]};
					flatWallsFaces[iz][iy][ix][1] = new String[]{flatWallsPointsRef[iz][iy][ix][4],flatWallsPointsRef[iz][iy][ix][5],flatWallsPointsRef[iz][iy][ix][6],flatWallsPointsRef[iz][iy][ix][7]};
					flatWallsFaces[iz][iy][ix][2] = new String[]{flatWallsPointsRef[iz][iy][ix][0],flatWallsPointsRef[iz][iy][ix][1],flatWallsPointsRef[iz][iy][ix][5],flatWallsPointsRef[iz][iy][ix][4]};
					flatWallsFaces[iz][iy][ix][3] = new String[]{flatWallsPointsRef[iz][iy][ix][2],flatWallsPointsRef[iz][iy][ix][3],flatWallsPointsRef[iz][iy][ix][7],flatWallsPointsRef[iz][iy][ix][6]};
					flatWallsFaces[iz][iy][ix][4] = new String[]{flatWallsPointsRef[iz][iy][ix][1],flatWallsPointsRef[iz][iy][ix][2],flatWallsPointsRef[iz][iy][ix][6],flatWallsPointsRef[iz][iy][ix][5]};
					flatWallsFaces[iz][iy][ix][5] = new String[]{flatWallsPointsRef[iz][iy][ix][0],flatWallsPointsRef[iz][iy][ix][3],flatWallsPointsRef[iz][iy][ix][7],flatWallsPointsRef[iz][iy][ix][4]};
					if (!flatWalls[iz][iy][ix].getOpen()){
						for (int i0=0;i0<6;i0++){
							objData.append("f ");
							for (int i1=0;i1<4;i1++){
								objData.append(flatWallsFaces[iz][iy][ix][i0][i1]).append(" ");
							}
							objData.append("#flat wall").append("\n");
						}
					}
				}
			}
		}
		
		//horizontal wall faces
		for(int iz=0;iz<zCells;iz++){
			for(int iy=0;iy<yCells+1;iy++){
				for(int ix=0;ix<xCells;ix++){
					horizontalWallsFaces[iz][iy][ix][0] = new String[]{horizontalWallsPointsRef[iz][iy][ix][0],horizontalWallsPointsRef[iz][iy][ix][1],horizontalWallsPointsRef[iz][iy][ix][2],horizontalWallsPointsRef[iz][iy][ix][3]};
					horizontalWallsFaces[iz][iy][ix][1] = new String[]{horizontalWallsPointsRef[iz][iy][ix][4],horizontalWallsPointsRef[iz][iy][ix][5],horizontalWallsPointsRef[iz][iy][ix][6],horizontalWallsPointsRef[iz][iy][ix][7]};
					horizontalWallsFaces[iz][iy][ix][2] = new String[]{horizontalWallsPointsRef[iz][iy][ix][0],horizontalWallsPointsRef[iz][iy][ix][1],horizontalWallsPointsRef[iz][iy][ix][5],horizontalWallsPointsRef[iz][iy][ix][4]};
					horizontalWallsFaces[iz][iy][ix][3] = new String[]{horizontalWallsPointsRef[iz][iy][ix][2],horizontalWallsPointsRef[iz][iy][ix][3],horizontalWallsPointsRef[iz][iy][ix][7],horizontalWallsPointsRef[iz][iy][ix][6]};
					horizontalWallsFaces[iz][iy][ix][4] = new String[]{horizontalWallsPointsRef[iz][iy][ix][1],horizontalWallsPointsRef[iz][iy][ix][2],horizontalWallsPointsRef[iz][iy][ix][6],horizontalWallsPointsRef[iz][iy][ix][5]};
					horizontalWallsFaces[iz][iy][ix][5] = new String[]{horizontalWallsPointsRef[iz][iy][ix][0],horizontalWallsPointsRef[iz][iy][ix][3],horizontalWallsPointsRef[iz][iy][ix][7],horizontalWallsPointsRef[iz][iy][ix][4]};
					if (!horizontalWalls[iz][iy][ix].getOpen()){
						for (int i0=0;i0<6;i0++){
							objData.append("f ");
							for (int i1=0;i1<4;i1++){
								objData.append(horizontalWallsFaces[iz][iy][ix][i0][i1]).append(" ");
							}
							objData.append("#horizontal wall").append("\n");
						}
					}
				}
			}
		}
		
		//vertical wall faces
		for(int iz=0;iz<zCells;iz++){
			for(int iy=0;iy<yCells;iy++){
				for(int ix=0;ix<xCells+1;ix++){
					verticalWallsFaces[iz][iy][ix][0] = new String[]{verticalWallsPointsRef[iz][iy][ix][0],verticalWallsPointsRef[iz][iy][ix][1],verticalWallsPointsRef[iz][iy][ix][2],verticalWallsPointsRef[iz][iy][ix][3]};
					verticalWallsFaces[iz][iy][ix][1] = new String[]{verticalWallsPointsRef[iz][iy][ix][4],verticalWallsPointsRef[iz][iy][ix][5],verticalWallsPointsRef[iz][iy][ix][6],verticalWallsPointsRef[iz][iy][ix][7]};
					verticalWallsFaces[iz][iy][ix][2] = new String[]{verticalWallsPointsRef[iz][iy][ix][0],verticalWallsPointsRef[iz][iy][ix][1],verticalWallsPointsRef[iz][iy][ix][5],verticalWallsPointsRef[iz][iy][ix][4]};
					verticalWallsFaces[iz][iy][ix][3] = new String[]{verticalWallsPointsRef[iz][iy][ix][2],verticalWallsPointsRef[iz][iy][ix][3],verticalWallsPointsRef[iz][iy][ix][7],verticalWallsPointsRef[iz][iy][ix][6]};
					verticalWallsFaces[iz][iy][ix][4] = new String[]{verticalWallsPointsRef[iz][iy][ix][1],verticalWallsPointsRef[iz][iy][ix][2],verticalWallsPointsRef[iz][iy][ix][6],verticalWallsPointsRef[iz][iy][ix][5]};
					verticalWallsFaces[iz][iy][ix][5] = new String[]{verticalWallsPointsRef[iz][iy][ix][0],verticalWallsPointsRef[iz][iy][ix][3],verticalWallsPointsRef[iz][iy][ix][7],verticalWallsPointsRef[iz][iy][ix][4]};
					if (!verticalWalls[iz][iy][ix].getOpen()){
						for (int i0=0;i0<6;i0++){
							objData.append("f ");
							for (int i1=0;i1<4;i1++){
								objData.append(verticalWallsFaces[iz][iy][ix][i0][i1]).append(" ");
							}
							objData.append("#vertical wall").append("\n");
						}
					}
				}
			}
		}
		
		//flat frame vertices
		for(int iz=0;iz<zCells;iz++){
			for(int iy=0;iy<yCells+1;iy++){
				for(int ix=0;ix<xCells+1;ix++){
					flatFramesPoints[iz][iy][ix] = flatFrames[iz][iy][ix].getPoints();
					for (int i0=0;i0<8;i0++){
						counter = counter + 1;
						flatFramesPointsRef[iz][iy][ix][i0] = String.valueOf(counter);
						objData.append("v ");
						for (int i1=0;i1<3;i1++){
							objData.append(flatFramesPoints[iz][iy][ix][i0][i1]).append(" ");
						}
						objData.append("\n");
					}
				}
			}
		}	
		
		//horizontal frame vertices
		for(int iz=0;iz<zCells+1;iz++){
			for(int iy=0;iy<yCells;iy++){
				for(int ix=0;ix<xCells+1;ix++){
					horizontalFramesPoints[iz][iy][ix] = horizontalFrames[iz][iy][ix].getPoints();
					for (int i0=0;i0<8;i0++){
						counter = counter + 1;
						horizontalFramesPointsRef[iz][iy][ix][i0] = String.valueOf(counter);
						objData.append("v ");
						for (int i1=0;i1<3;i1++){
							objData.append(horizontalFramesPoints[iz][iy][ix][i0][i1]).append(" ");
						}
						objData.append("\n");
					}
				}
			}
		}		
		
		//vertical frame vertices
		for(int iz=0;iz<zCells+1;iz++){
			for(int iy=0;iy<yCells+1;iy++){
				for(int ix=0;ix<xCells;ix++){
					verticalFramesPoints[iz][iy][ix] = verticalFrames[iz][iy][ix].getPoints();
					for (int i0=0;i0<8;i0++){
						counter = counter + 1;
						verticalFramesPointsRef[iz][iy][ix][i0] = String.valueOf(counter);
						objData.append("v ");
						for (int i1=0;i1<3;i1++){
							objData.append(verticalFramesPoints[iz][iy][ix][i0][i1]).append(" ");
						}
						objData.append("\n");
					}
				}
			}
		}				
		
		//corners vertices
		for(int iz=0;iz<zCells+1;iz++){
			for(int iy=0;iy<yCells+1;iy++){
				for(int ix=0;ix<xCells+1;ix++){
					cornersPoints[iz][iy][ix] = corners[iz][iy][ix].getPoints();
					for (int i0=0;i0<8;i0++){
						counter = counter + 1;
						cornersPointsRef[iz][iy][ix][i0] = String.valueOf(counter);
						objData.append("v ");
						for (int i1=0;i1<3;i1++){
							objData.append(cornersPoints[iz][iy][ix][i0][i1]).append(" ");
						}
						objData.append("\n");
					}
				}
			}
		}	
		
		//flat frame faces
		for(int iz=0;iz<zCells;iz++){
			for(int iy=0;iy<yCells+1;iy++){
				for(int ix=0;ix<xCells+1;ix++){
					flatFramesFaces[iz][iy][ix][0] = new String[]{flatFramesPointsRef[iz][iy][ix][0],flatFramesPointsRef[iz][iy][ix][1],flatFramesPointsRef[iz][iy][ix][2],flatFramesPointsRef[iz][iy][ix][3]};
					flatFramesFaces[iz][iy][ix][1] = new String[]{flatFramesPointsRef[iz][iy][ix][4],flatFramesPointsRef[iz][iy][ix][5],flatFramesPointsRef[iz][iy][ix][6],flatFramesPointsRef[iz][iy][ix][7]};
					flatFramesFaces[iz][iy][ix][2] = new String[]{flatFramesPointsRef[iz][iy][ix][0],flatFramesPointsRef[iz][iy][ix][1],flatFramesPointsRef[iz][iy][ix][5],flatFramesPointsRef[iz][iy][ix][4]};
					flatFramesFaces[iz][iy][ix][3] = new String[]{flatFramesPointsRef[iz][iy][ix][2],flatFramesPointsRef[iz][iy][ix][3],flatFramesPointsRef[iz][iy][ix][7],flatFramesPointsRef[iz][iy][ix][6]};
					flatFramesFaces[iz][iy][ix][4] = new String[]{flatFramesPointsRef[iz][iy][ix][1],flatFramesPointsRef[iz][iy][ix][2],flatFramesPointsRef[iz][iy][ix][6],flatFramesPointsRef[iz][iy][ix][5]};
					flatFramesFaces[iz][iy][ix][5] = new String[]{flatFramesPointsRef[iz][iy][ix][0],flatFramesPointsRef[iz][iy][ix][3],flatFramesPointsRef[iz][iy][ix][7],flatFramesPointsRef[iz][iy][ix][4]};
					for (int i0=0;i0<6;i0++){
						objData.append("f ");
						for (int i1=0;i1<4;i1++){
							objData.append(flatFramesFaces[iz][iy][ix][i0][i1]).append(" ");
						}
						objData.append("#flat frame").append("\n");
					}
				}
			}
		}
		
		//horizontal frame faces
		for(int iz=0;iz<zCells+1;iz++){
			for(int iy=0;iy<yCells;iy++){
				for(int ix=0;ix<xCells+1;ix++){
					horizontalFramesFaces[iz][iy][ix][0] = new String[]{horizontalFramesPointsRef[iz][iy][ix][0],horizontalFramesPointsRef[iz][iy][ix][1],horizontalFramesPointsRef[iz][iy][ix][2],horizontalFramesPointsRef[iz][iy][ix][3]};
					horizontalFramesFaces[iz][iy][ix][1] = new String[]{horizontalFramesPointsRef[iz][iy][ix][4],horizontalFramesPointsRef[iz][iy][ix][5],horizontalFramesPointsRef[iz][iy][ix][6],horizontalFramesPointsRef[iz][iy][ix][7]};
					horizontalFramesFaces[iz][iy][ix][2] = new String[]{horizontalFramesPointsRef[iz][iy][ix][0],horizontalFramesPointsRef[iz][iy][ix][1],horizontalFramesPointsRef[iz][iy][ix][5],horizontalFramesPointsRef[iz][iy][ix][4]};
					horizontalFramesFaces[iz][iy][ix][3] = new String[]{horizontalFramesPointsRef[iz][iy][ix][2],horizontalFramesPointsRef[iz][iy][ix][3],horizontalFramesPointsRef[iz][iy][ix][7],horizontalFramesPointsRef[iz][iy][ix][6]};
					horizontalFramesFaces[iz][iy][ix][4] = new String[]{horizontalFramesPointsRef[iz][iy][ix][1],horizontalFramesPointsRef[iz][iy][ix][2],horizontalFramesPointsRef[iz][iy][ix][6],horizontalFramesPointsRef[iz][iy][ix][5]};
					horizontalFramesFaces[iz][iy][ix][5] = new String[]{horizontalFramesPointsRef[iz][iy][ix][0],horizontalFramesPointsRef[iz][iy][ix][3],horizontalFramesPointsRef[iz][iy][ix][7],horizontalFramesPointsRef[iz][iy][ix][4]};
					for (int i0=0;i0<6;i0++){
						objData.append("f ");
						for (int i1=0;i1<4;i1++){
							objData.append(horizontalFramesFaces[iz][iy][ix][i0][i1]).append(" ");
						}
						objData.append("#horizontal frame").append("\n");
					}
				}
			}
		}
		
		//vertical frames faces
		for(int iz=0;iz<zCells+1;iz++){
			for(int iy=0;iy<yCells+1;iy++){
				for(int ix=0;ix<xCells;ix++){
					verticalFramesFaces[iz][iy][ix][0] = new String[]{verticalFramesPointsRef[iz][iy][ix][0],verticalFramesPointsRef[iz][iy][ix][1],verticalFramesPointsRef[iz][iy][ix][2],verticalFramesPointsRef[iz][iy][ix][3]};
					verticalFramesFaces[iz][iy][ix][1] = new String[]{verticalFramesPointsRef[iz][iy][ix][4],verticalFramesPointsRef[iz][iy][ix][5],verticalFramesPointsRef[iz][iy][ix][6],verticalFramesPointsRef[iz][iy][ix][7]};
					verticalFramesFaces[iz][iy][ix][2] = new String[]{verticalFramesPointsRef[iz][iy][ix][0],verticalFramesPointsRef[iz][iy][ix][1],verticalFramesPointsRef[iz][iy][ix][5],verticalFramesPointsRef[iz][iy][ix][4]};
					verticalFramesFaces[iz][iy][ix][3] = new String[]{verticalFramesPointsRef[iz][iy][ix][2],verticalFramesPointsRef[iz][iy][ix][3],verticalFramesPointsRef[iz][iy][ix][7],verticalFramesPointsRef[iz][iy][ix][6]};
					verticalFramesFaces[iz][iy][ix][4] = new String[]{verticalFramesPointsRef[iz][iy][ix][1],verticalFramesPointsRef[iz][iy][ix][2],verticalFramesPointsRef[iz][iy][ix][6],verticalFramesPointsRef[iz][iy][ix][5]};
					verticalFramesFaces[iz][iy][ix][5] = new String[]{verticalFramesPointsRef[iz][iy][ix][0],verticalFramesPointsRef[iz][iy][ix][3],verticalFramesPointsRef[iz][iy][ix][7],verticalFramesPointsRef[iz][iy][ix][4]};
					for (int i0=0;i0<6;i0++){
						objData.append("f ");
						for (int i1=0;i1<4;i1++){
							objData.append(verticalFramesFaces[iz][iy][ix][i0][i1]).append(" ");
						}
						objData.append("#vertical frame").append("\n");
					}
				}
			}
		}
		
		//corners faces
		for(int iz=0;iz<zCells+1;iz++){
			for(int iy=0;iy<yCells+1;iy++){
				for(int ix=0;ix<xCells+1;ix++){
					cornersFaces[iz][iy][ix][0] = new String[]{cornersPointsRef[iz][iy][ix][0],cornersPointsRef[iz][iy][ix][1],cornersPointsRef[iz][iy][ix][2],cornersPointsRef[iz][iy][ix][3]};
					cornersFaces[iz][iy][ix][1] = new String[]{cornersPointsRef[iz][iy][ix][4],cornersPointsRef[iz][iy][ix][5],cornersPointsRef[iz][iy][ix][6],cornersPointsRef[iz][iy][ix][7]};
					cornersFaces[iz][iy][ix][2] = new String[]{cornersPointsRef[iz][iy][ix][0],cornersPointsRef[iz][iy][ix][1],cornersPointsRef[iz][iy][ix][5],cornersPointsRef[iz][iy][ix][4]};
					cornersFaces[iz][iy][ix][3] = new String[]{cornersPointsRef[iz][iy][ix][2],cornersPointsRef[iz][iy][ix][3],cornersPointsRef[iz][iy][ix][7],cornersPointsRef[iz][iy][ix][6]};
					cornersFaces[iz][iy][ix][4] = new String[]{cornersPointsRef[iz][iy][ix][1],cornersPointsRef[iz][iy][ix][2],cornersPointsRef[iz][iy][ix][6],cornersPointsRef[iz][iy][ix][5]};
					cornersFaces[iz][iy][ix][5] = new String[]{cornersPointsRef[iz][iy][ix][0],cornersPointsRef[iz][iy][ix][3],cornersPointsRef[iz][iy][ix][7],cornersPointsRef[iz][iy][ix][4]};
					for (int i0=0;i0<6;i0++){
						objData.append("f ");
						for (int i1=0;i1<4;i1++){
							objData.append(cornersFaces[iz][iy][ix][i0][i1]).append(" ");
						}
						objData.append("#corner").append("\n");
					}
				}
			}
		}
		
		return objData.toString();
	}

	public String redrawSolution(){
		StringBuilder objData = new StringBuilder();
		int counter = 0;
		
		ArrayList<float[][]> coords = new ArrayList<>();
		String[] coordsRef = new String[8]; 
		String[][] faces = new String[6][4];
		for (Block3D block3D : route) {
			coords.add(block3D.getPoints());
			for (int i0 = 0; i0 < 8; i0++) {
				counter = counter + 1;
				coordsRef[i0] = String.valueOf(counter);
				objData.append("v ");
				for (int i1 = 0; i1 < 3; i1++) {
					objData.append(coords.get(coords.size() - 1)[i0][i1]).append(" ");
				}
				objData.append("\n");
			}
			
			faces[0] = new String[]{coordsRef[0], coordsRef[1], coordsRef[2], coordsRef[3]};
			faces[1] = new String[]{coordsRef[4], coordsRef[5], coordsRef[6], coordsRef[7]};
			faces[2] = new String[]{coordsRef[0], coordsRef[1], coordsRef[5], coordsRef[4]};
			faces[3] = new String[]{coordsRef[2], coordsRef[3], coordsRef[7], coordsRef[6]};
			faces[4] = new String[]{coordsRef[1], coordsRef[2], coordsRef[6], coordsRef[5]};
			faces[5] = new String[]{coordsRef[0], coordsRef[3], coordsRef[7], coordsRef[4]};
			
			for (int i0 = 0; i0 < 6; i0++) {
				objData.append("f ");
				for (int i1 = 0; i1 < 4; i1++) {
					objData.append(faces[i0][i1]).append(" ");
				}
				objData.append("\n");
			}
		}
		
		return objData.toString();
	}
}