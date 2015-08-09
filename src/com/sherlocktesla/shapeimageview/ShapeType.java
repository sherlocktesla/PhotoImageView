package com.sherlocktesla.shapeimageview;

public enum ShapeType {

	NONE(0), 
	ROUNDEDCORNERS(1),
	CIRCLE(2), 
	TRIANGLE(3), 
	OVAL(4), 
	FIVEPOINTSTAR(5), 
	FIVEEDGE(6);

	ShapeType(int type) {
		// TODO Auto-generated constructor stub
		nativeInt = type;
	}

	final int nativeInt;

}
