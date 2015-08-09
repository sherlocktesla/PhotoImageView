package com.sherlocktesla.shapeimageview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("WrongCall")
public class ShapeImageView extends ImageView {
	
	private final float CORNERRADUIS = 25;// the default corner radius
	
	private final float OVALOFFSET = 0.8F;// the offset to change circle to oval
	
	private final float OVALCOMPARTORSTART = 0.6F;
	
	private final float OVALCOMPARTOREND = 0.85F;
	
	private ShapeType mShapeType = ShapeType.NONE;

	private static final ShapeType[] shapeTypeArray = {

			ShapeType.NONE, 
			ShapeType.ROUNDEDCORNERS,
			ShapeType.CIRCLE,
			ShapeType.TRIANGLE, 
			ShapeType.OVAL, 
			ShapeType.FIVEPOINTSTAR,
			ShapeType.FIVEEDGE

	};

	public ShapeImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ShapeImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ShapeImageView(Context context) {
		super(context);
		init(context, null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * here to get ShapeType from xml
	 */
	private void init(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CustomShape);
			mShapeType = shapeTypeArray[typedArray.getInt(R.styleable.CustomShape_ShapeType, 0)];
			typedArray.recycle();
			typedArray = null;
		}
	}

	/**
	 * here to set the shapeType
	 * 
	 * @param shapeType
	 */
	public void setShapeType(ShapeType shapeType) {
		mShapeType = shapeType;
		invalidate();
	}

	/**
	 * here to get the shapteType
	 * 
	 * @return
	 */
	public ShapeType getShapeType() {
		return mShapeType;
	}


	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		drawShape(canvas);
	}

	/**
	 * Here to draw the shape bitmap we want
	 * 
	 * @param canvas
	 */
	private void drawShape(Canvas canvas) {
		BitmapDrawable drawable = (BitmapDrawable) getDrawable();
		if(drawable == null){
			drawable = (BitmapDrawable) getBackground();
		}
		if(drawable == null ||getWidth() == 0 || getHeight() == 0){
			return ;
		}
		Bitmap bitmap = drawable.getBitmap();
		switch (mShapeType) {
		case ROUNDEDCORNERS:
			drawRoundedCorners(canvas, bitmap);
			break;
		case CIRCLE:
			drawCircle(canvas, bitmap);
			break;
		case TRIANGLE:
			drawTriangle(canvas, bitmap);
			break;
		case OVAL:
			drawOval(canvas, bitmap);
			break;
		case FIVEPOINTSTAR:
			drawFivePoint(canvas, bitmap);
			break;
		case FIVEEDGE:
			drawFiveEdge(canvas, bitmap);
			break;	
		default:
			super.onDraw(canvas);
			break;
		}

	}
	
	
	/**here to get the paint we need
	 * @param bitmap
	 * @return
	 */
	private Paint getDrawPaint(Bitmap bitmap){
		final BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		Paint paint= new Paint(){
			{
				setAntiAlias(true);
				setStyle(Style.FILL_AND_STROKE);
				setShader(shader);			
			}
		};		
		return paint;
	}
	
	

	
	/**here to get the scaled bitmap
	 * @param scale
	 * @param bitmap
	 * @return
	 */
	private Bitmap getScaleBitmap(Bitmap bitmap){
		float scale;
		int dwidth = bitmap.getWidth();
		int dheight = bitmap.getHeight();
		final Matrix matrix = new Matrix();
        int vwidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int vheight = getHeight() - getPaddingTop() - getPaddingBottom();
        if (dwidth <= vwidth && dheight <= vheight) {
        	scale = Math.min((float) vwidth / (float) dwidth,
                    (float) vheight / (float) dheight);
        } else {
            scale = Math.max((float) vwidth / (float) dwidth,
                    (float) vheight / (float) dheight);
        }
        matrix.setScale(scale, scale);
		return Bitmap.createBitmap(bitmap,0,0,dwidth,dheight,matrix,true);
		
		
	}
	/**here to draw circle
	 * @param canvas
	 * @param bitmap
	 */
	private void drawCircle(Canvas canvas,Bitmap bitmap){
		final float centerX = getWidth()*0.5f;
		final float centerY = getHeight()*0.5f;
		final float radius = centerX>= centerY?centerY:centerX;
		canvas.drawCircle(centerX, centerY, radius, getDrawPaint(getScaleBitmap(bitmap)));
	}
	

	/**here to draw roundedCorners
	 * @param canvas
	 * @param bitmap
	 */
	private void drawRoundedCorners(Canvas canvas,Bitmap bitmap){
		final RectF rect = new RectF(canvas.getClipBounds());
		canvas.drawRoundRect(rect, CORNERRADUIS, CORNERRADUIS, getDrawPaint(getScaleBitmap(bitmap)));
	}
	

	

	/**here to draw triangle
	 * @param canvas
	 * @param bitmap
	 */
	private void drawTriangle(Canvas canvas,Bitmap bitmap){
		final Rect rect = canvas.getClipBounds();
		final PointF top = new PointF((rect.left+rect.right)*0.5f, rect.top);
		final PointF left = new PointF(rect.left, rect.bottom);
		final PointF right = new PointF(rect.right, rect.bottom);
		final Path path = new Path();
		path.lineTo(top.x, top.y);
		path.lineTo(left.x, left.y);
		path.lineTo(right.x, right.y);
		path.lineTo(top.x, top.y);
		canvas.drawPath(path, getDrawPaint(getScaleBitmap(bitmap)));
		
	}
	
	/**here to draw oval
	 * @param canvas
	 * @param bitmap
	 */
	private void drawOval(Canvas canvas,Bitmap bitmap){
		
		Rect rect = canvas.getClipBounds();
		final float ovalWeight = (float)rect.height()/(float)rect.width();
		if(ovalWeight >OVALCOMPARTOREND || ovalWeight <OVALCOMPARTORSTART){
			rect = new Rect(rect.left, rect.top, rect.left+(int)(rect.width()*OVALOFFSET), rect.bottom);
		}
		final RectF ovalRect = new RectF(rect);
		canvas.drawOval(ovalRect, getDrawPaint(getScaleBitmap(bitmap)));
	}
	
	/**here to draw fivePointStar
	 * @param canvas
	 * @param bitmap
	 */
	private void drawFivePoint(Canvas canvas,Bitmap bitmap){
		final Path path = new Path();
		final Rect rect = canvas.getClipBounds();
		final float polygonLen = Math.min(rect.width(), rect.height());
		final PointF top = new PointF(rect.left+polygonLen*0.5f, rect.top);
		final PointF left = new PointF(rect.left, rect.top+polygonLen*0.4f);
		final PointF right = new PointF(rect.left+polygonLen, rect.top+polygonLen*0.4f);
		final PointF leftBottom = new PointF(rect.left+polygonLen*0.2f, rect.top+polygonLen);
		final PointF rightBottom = new PointF(rect.left+polygonLen*0.8f, rect.top+polygonLen);
		path.lineTo(top.x, top.y);
		path.lineTo(leftBottom.x, leftBottom.y);
		path.lineTo(right.x, right.y);
		path.lineTo(left.x, left.y);
		path.lineTo(rightBottom.x, rightBottom.y);
		path.lineTo(top.x, top.y);
		canvas.drawPath(path, getDrawPaint(getScaleBitmap(bitmap)));	
	}
	
	/**here to draw fiveEdgeStar
	 * @param canvas
	 * @param bitmap
	 */
	private void drawFiveEdge(Canvas canvas,Bitmap bitmap){
		final Path path = new Path();
		final Rect rect = canvas.getClipBounds();
		final float polygonLen = Math.min(rect.width(), rect.height());
		final PointF top = new PointF(rect.left+polygonLen*0.5f, rect.top);
		final PointF left = new PointF(rect.left, rect.top+polygonLen*0.4f);
		final PointF right = new PointF(rect.left+polygonLen, rect.top+polygonLen*0.4f);
		final PointF leftBottom = new PointF(rect.left+polygonLen*0.2f, rect.top+polygonLen);
		final PointF rightBottom = new PointF(rect.left+polygonLen*0.8f, rect.top+polygonLen);
		path.lineTo(top.x, top.y);
		path.lineTo(left.x, left.y);
		path.lineTo(leftBottom.x, leftBottom.y);
		path.lineTo(rightBottom.x, rightBottom.y);
		path.lineTo(right.x, right.y);
		path.lineTo(top.x, top.y);
		canvas.drawPath(path, getDrawPaint(getScaleBitmap(bitmap)));	
	}

}
