package com.visor.illusionh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Custom SurfaceView for plotting Hering Illusion and linking it with a Seekbar
 * 
 * @author Muhammad Saad Shamim
 */
public class CanvasView extends SurfaceView {

	/** paint for background intersecting lines */
	private final Paint xLinesPaint = new Paint();

	/** paint for foreground parallel lines */
	private final Paint pLinesPaint = new Paint();

	/** scoring variables */
	private int varCurveOffset = 0;
	private int maxCurveOffset;
	
	/** drawing variables */
	private int height, width, heightIncr, widthIncr, widthMid,
		heightFirstThird, heightSecondThird, widthOffset;
	
	/** for drawing curves */
	private final Path path = new Path();
	
	public CanvasView(Context context) {
		super(context);
		commonSetup(context);
	}

	public CanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		commonSetup(context);
	}

	public CanvasView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		commonSetup(context);
	}

	/**
	 * Setup of colors and screen measurements
	 * 
	 * @param context
	 */
	private void commonSetup(Context context){
		setWillNotDraw(false);
		xLinesPaint.setColor(Color.WHITE);
		pLinesPaint.setColor(Color.WHITE);
		pLinesPaint.setStyle(Paint.Style.STROKE);
		pLinesPaint.setStrokeWidth(10f);
		
		// get screen dimensions
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		
		// split into increments
		heightIncr = (int)(0.05*height);
		widthIncr = (int)(0.1*width);
		
		// divide major sections for the parallel lines
		widthMid = (int)(0.5*width);
		heightFirstThird = (int)((0.333333)*height);
		heightSecondThird = height - heightFirstThird;
		widthOffset = (int)(widthMid*0.3);
		
		// SeekBar variables
		maxCurveOffset = width/2;
		varCurveOffset = (int) (maxCurveOffset * (0.3));
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		// draw along vertical height
		for(int i = 0; i < height; i+=heightIncr){
			canvas.drawLine(0, i, width, height-i, xLinesPaint);
			canvas.drawLine(0, height-i, width, i, xLinesPaint);
		}

		// draw along horizontal width
		for(int i = 0; i < width; i+=widthIncr){
			canvas.drawLine(i, 0, width-i, height, xLinesPaint);
			canvas.drawLine(width-i, 0, i, height, xLinesPaint);
		}

		// draw two parallel lines
		path.reset();
		path.moveTo(widthMid+widthOffset, 0);
		path.cubicTo(widthMid+varCurveOffset, heightFirstThird, widthMid+varCurveOffset,
				heightSecondThird, widthMid+widthOffset, height);
		canvas.drawPath(path, pLinesPaint);
		
		path.moveTo(widthMid-widthOffset, 0);
		path.cubicTo(widthMid-varCurveOffset, heightFirstThird, widthMid-varCurveOffset,
				heightSecondThird, widthMid-widthOffset, height);
		canvas.drawPath(path, pLinesPaint);
	}

	/**
	 * Method for synchronizing SeekBar changes with the parallel
	 * line curvature in the Hering Illusion
	 * 
	 * @param SeekBar to be synchronized with Hering Illusion
	 */
	public void setSeekBar(SeekBar seekBar) {
		
		// set basic variables for SeekBar
		
		seekBar.setMax(maxCurveOffset);
		seekBar.setProgress(varCurveOffset);
		
		// activate synchronization and redrawing
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				varCurveOffset = progress;
				invalidate();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
	}

	/**
	 * Score measuring level of curvature to correct illusion
	 * (expected to vary for different individuals)
	 * 
	 * @return Score 
	 */
	public float getScore(){
		return ((float) varCurveOffset)/((float) maxCurveOffset);
	}
}
