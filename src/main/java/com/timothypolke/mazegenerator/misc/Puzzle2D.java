package com.timothypolke.mazegenerator.misc;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Base64;
import java.security.SecureRandom;
import javax.imageio.ImageIO;

import lombok.Getter;

public class Puzzle2D extends BufferedImage{
	private int xCells;
	private int yCells;
	private int boundarySize;
	private int cellSize;
	
	private Graphics2D graphics;
	
	private BufferedImage innerImage;
	private Graphics2D innerGraphics;
	
	@Getter
    private String solved=null;
	@Getter
    private String unsolved=null;

	private Block2D[][] verticalWalls=null;
	private Block2D[][] horizontalWalls=null;
	private Block2D[][] cells=null;
	private Block2D[][] corners=null;
	
	private ArrayList<Block2D> route=null;
	private int x=0;
	private int y=0;

	public Puzzle2D(int xCells, int yCells, int boundarySize, int cellSize, Color clrForeground, Color clrBackground, Color clrHighlight){
		super((xCells*cellSize)+((xCells*boundarySize)+boundarySize) + (cellSize*2),(yCells*cellSize)+((yCells*boundarySize)+boundarySize) + (cellSize*2),BufferedImage.TYPE_INT_RGB);
	
		this.xCells=xCells;
		this.yCells=yCells;
		this.boundarySize=boundarySize;
		this.cellSize=cellSize;
		
		graphics=createGraphics();
		
		innerImage = new BufferedImage((xCells*cellSize)+((xCells*boundarySize)+boundarySize),(yCells*cellSize)+((yCells*boundarySize)+boundarySize),BufferedImage.TYPE_INT_RGB);
		innerGraphics = innerImage.createGraphics();
		
		graphics.setColor(clrBackground);
		graphics.fillRect(0, 0, super.getWidth(), super.getHeight());
		
		create();
		solve();
		
		redraw(clrForeground, clrBackground, clrBackground);
		unsolved = Base64.getEncoder().encodeToString(processImage());
		
		redraw(clrBackground, clrBackground, clrHighlight);
		solved = Base64.getEncoder().encodeToString(processImage());
	}

    public void create(){
		verticalWalls=new Block2D[yCells][xCells+1];
		horizontalWalls=new Block2D[yCells+1][xCells];
		cells=new Block2D[yCells][xCells];
		corners=new Block2D[yCells+1][xCells+1];
		
		ArrayList<Block2D> visitedCells=new ArrayList<>();

		createVerticalWalls(xCells+1,yCells);
		createHorizontalWalls(xCells,yCells+1);
		createCells();
		createCorners(xCells+1,yCells+1);
		
		visitedCells.add(cells[0][0]);
		while(determinePresenceOfClosedCells()){
			chooseDirection(visitedCells.get(visitedCells.size()-1).getColumnID(),visitedCells.get(visitedCells.size()-1).getRowID(),visitedCells);
		}		
	}
	
	public void solve(){
		ArrayList<Block2D> visitedCells = new ArrayList<>();
		
		route=new ArrayList<>();
		route.add(cells[0][0]);
		visitedCells.add(cells[0][0]);
		
		while(traverseVisited(yCells - 1, xCells - 1, visitedCells)){
			if(forwardMotion(x, y - 1, x, y, 0, visitedCells) && forwardMotion(x, y + 1, x, y + 1, 1, visitedCells) && forwardMotion(x - 1, y, x, y, 2, visitedCells) && forwardMotion(x + 1, y, x + 1, y, 3, visitedCells)) {
				route.remove(route.get(route.size() - 1));
				route.trimToSize();
				x = route.get(route.size() - 1).getColumnID();
				y = route.get(route.size() - 1).getRowID();
			}
		}
	}

	public void createVerticalWalls(int xVerticalWalls,int yVerticalWalls){
		int posX = 0;
		int posY = boundarySize;
		for(int iy=0;iy<yVerticalWalls;iy++){
			for(int ix=0;ix<xVerticalWalls;ix++){
				verticalWalls[iy][ix]=new Block2D(iy,ix,posX,posY,false);
				posX = posX + cellSize + boundarySize;
			}
			posY = posY + cellSize + boundarySize;
			posX = 0;
		}
	}
	
	public void createHorizontalWalls(int xHorizontalWalls,int yHorizontalWalls){
		int posX = boundarySize;
		int posY = 0;
		for(int iy=0;iy<yHorizontalWalls;iy++){
			for(int ix=0;ix<xHorizontalWalls;ix++){
				horizontalWalls[iy][ix]=new Block2D(iy,ix,posX,posY,false);
				posX = posX + cellSize + boundarySize;
			}
			posY = posY + cellSize + boundarySize;
			posX = boundarySize;
		}
	}

	public void createCells(){
		int posX = boundarySize;
		int posY = boundarySize;
		for(int iy=0;iy<yCells;iy++){		
			for(int ix=0;ix<xCells;ix++){
				cells[iy][ix]=new Block2D(iy,ix,posX,posY,true);
				posX = posX + cellSize + boundarySize;	
			}
			posY = posY + cellSize + boundarySize;
			posX = boundarySize;
		}
	}

	public void createCorners(int xCorners,int yCorners){
		int posX = 0;
		int posY = 0;
		for(int iy=0;iy<yCorners;iy++){		
			for(int ix=0;ix<xCorners;ix++){
				corners[iy][ix]=new Block2D(iy,ix,posX,posY,false);
				posX = posX + cellSize + boundarySize;	
			}
			posY = posY + cellSize + boundarySize;
			posX = 0;
		}
	}
	
	public void chooseDirection(int posX,int posY,ArrayList<Block2D> visitedCells) {
		ArrayList<Integer> uncheckedDirections = new ArrayList<>();
		uncheckedDirections.add(0);
		uncheckedDirections.add(1);
		uncheckedDirections.add(2);
		uncheckedDirections.add(3);

		SecureRandom rand = new SecureRandom();
		int direction;
		int xChange = 0;
		int yChange = 0;
		int xResult = 0;
		int yResult = 0;
		boolean type = false;
		while (!uncheckedDirections.isEmpty()) {
			direction = uncheckedDirections.get(rand.nextInt(uncheckedDirections.size()));

			if (direction == 0) {
				xChange = posX;
				yChange = posY - 1;
				xResult = posX;
				yResult = posY;
				type = true;
			} else if (direction == 1) {
				xChange = posX;
				yChange = posY + 1;
				xResult = posX;
				yResult = posY + 1;
				type = true;
			} else if (direction == 2) {
				xChange = posX - 1;
				yChange = posY;
				xResult = posX;
				yResult = posY;
				type = false;
			} else if (direction == 3) {
				xChange = posX + 1;
				yChange = posY;
				xResult = posX + 1;
				yResult = posY;
				type = false;
			}

			try {
				if (!checkVisitedStatus(cells[yChange][xChange], visitedCells)) {
					if (!type) {
						verticalWalls[yResult][xResult].setOpen(true);
					} else {
						horizontalWalls[yResult][xResult].setOpen(true);
					}
				}
				visitedCells.add(cells[yChange][xChange]);
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
		for(int y=0;y<yCells;y++){
			for(int x=0;x<xCells;x++){
				if(!horizontalWalls[cells[y][x].getRowID()][cells[y][x].getColumnID()].getOpen() && !horizontalWalls[cells[y][x].getRowID() + 1][cells[y][x].getColumnID()].getOpen() && !verticalWalls[cells[y][x].getRowID()][cells[y][x].getColumnID()].getOpen() && !verticalWalls[cells[y][x].getRowID()][cells[y][x].getColumnID() + 1].getOpen()){
					present=true;
					break loop;
				}
			}
		}
		return present;
	}
	
	public boolean checkVisitedStatus(Block2D cell, ArrayList<Block2D> visitedCells){
		boolean result=false;
        for (Block2D visitedCell : visitedCells) {
            if (visitedCell.getColumnID() == cell.getColumnID() && visitedCell.getRowID() == cell.getRowID()) {
                result = true;
                break;
            }
        }
		return result;
	}
	
	public boolean forwardMotion(int x1,int y1,int x2,int y2,int direction,ArrayList<Block2D> history){
		boolean failed=true;
		try{
			if (direction==0||direction==1){
				if(traverseVisited(y1, x1, history) && horizontalWalls[y2][x2].getOpen()){
					route.add(cells[y1][x1]);
					history.add(cells[y1][x1]);
					x=x1;
					y=y1;
					failed=false;
				}
			}
			else if (direction==2||direction==3){
				if(traverseVisited(y1, x1, history) && verticalWalls[y2][x2].getOpen()){
					route.add(cells[y1][x1]);
					history.add(cells[y1][x1]);
					x=x1;
					y=y1;
					failed=false;
				}
			}
		}
		catch(IndexOutOfBoundsException e){
			e.printStackTrace();
		}
		return failed;
	}
	
	public boolean traverseVisited(int y,int x,ArrayList<Block2D> history){
		boolean found=false;
        for (Block2D block2D : history) {
            if (block2D.getColumnID() == x && block2D.getRowID() == y) {
                found = true;
                break;
            }
        }
		return !found;
	}
	
	public void highlightCell(Color color,int posX,int posY,int type){
		innerGraphics.setColor(color);
		if(type==0){
			innerGraphics.fillRect(horizontalWalls[posY][posX].getStartX(),horizontalWalls[posY][posX].getStartY(),cellSize,boundarySize);
		}
		else if(type==1){
			innerGraphics.fillRect(verticalWalls[posY][posX].getStartX(),verticalWalls[posY][posX].getStartY(),boundarySize,cellSize);
		}
		else if(type==2){
			innerGraphics.fillRect(cells[posY][posX].getStartX(),cells[posY][posX].getStartY(),cellSize,cellSize);
		}
		else if(type==3){
			innerGraphics.fillRect(corners[posY][posX].getStartX(),corners[posY][posX].getStartY(),boundarySize,boundarySize);
		}
	}
	
	public void redraw(Color foreground,Color background,Color highlight){		
		for(int iy=0;iy<yCells+1;iy++){
			for(int ix=0;ix<xCells;ix++){
				if(horizontalWalls[iy][ix].getOpen()){
					highlightCell(background,ix,iy,0);
				}
				else{
					highlightCell(foreground,ix,iy,0);
				}
			}
		}
		
		for(int iy=0;iy<yCells;iy++){
			for(int ix=0;ix<xCells+1;ix++){
				if(verticalWalls[iy][ix].getOpen()){
					highlightCell(background,ix,iy,1);
				}
				else{
					highlightCell(foreground,ix,iy,1);
				}
			}
		}
		
		for(int iy=0;iy<yCells+1;iy++){
			for(int ix=0;ix<xCells+1;ix++){
				highlightCell(foreground,ix,iy,3);
			}
		}
	
		for(int iy=0;iy<yCells;iy++){
			for(int ix=0;ix<xCells;ix++){
				if(route.contains(cells[iy][ix])){
					highlightCell(highlight,ix,iy,2);
				}
				else{
					highlightCell(background,ix,iy,2);
				}
			}          
		}
		
		highlightCell(background,0,0,0);
		highlightCell(background,xCells-1,yCells,0);
		
		graphics.drawImage(innerImage, cellSize,  cellSize,  null);
	}   		
	
	public byte[] processImage(){
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		byte[] imageInByteArray=null;
		try{
			ImageIO.write(this,"jpg",byteArrayOutputStream);
			byteArrayOutputStream.flush();
			imageInByteArray=byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return imageInByteArray;
	}
}