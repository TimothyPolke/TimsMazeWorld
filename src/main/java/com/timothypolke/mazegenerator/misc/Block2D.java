package com.timothypolke.mazegenerator.misc;

import lombok.Getter;
import lombok.Setter;

@Setter
public class Block2D {
	
	@Getter
    private int rowID=0;
	@Getter
    private int columnID=0;
	@Getter
    private int startX=0;
	@Getter
    private int startY=0;

	private boolean open=false;
	
	public Block2D(int rowID, int columnID, int startX, int startY, boolean open){
		setRowID(rowID);
		setColumnID(columnID);
		setStartX(startX);
		setStartY(startY);
		setOpen(open);
	}

    public boolean getOpen(){
		return open;
	}
}