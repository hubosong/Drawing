package robsonmachczew.drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import android.graphics.PointF;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;

public class MultiCanvasView extends View  {

    private Paint drawPaint, canvasPaint, textPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    private Path path;
    private SparseArray<Path> paths;

    public MultiCanvasView(Context context) {
        super(context);
        setupDrawing();
    }

    public MultiCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    public MultiCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupDrawing();
    }

    private void setupDrawing() {
        paths = new SparseArray<>();

        drawPaint = new Paint();
        drawPaint .setAntiAlias(true);
        drawPaint .setColor(Color.BLACK);
        drawPaint .setStyle(Paint.Style.STROKE);
        drawPaint .setStrokeJoin(Paint.Join.ROUND);
        drawPaint .setStrokeWidth(10f);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);

        //canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    public void clearCanvas(){
        drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    /*
    public void saveDrawing(){
        Bitmap whatTheUserDrewBitmap = getDrawingCache();
        whatTheUserDrewBitmap = ThumbnailUtils.extractThumbnail(whatTheUserDrewBitmap, 256, 256);
        ImageView testArea = ...
        testArea.setImageBitmap( whatTheUserDrewBitmap );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        whatTheUserDrewBitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] yourByteArray;
        yourByteArray = baos.toByteArray();
    }
    */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int id = event.getPointerId(index);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                path = new Path();
                path.moveTo(event.getX(index), event.getY(index));
                paths.put(id, path);
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i=0; i<event.getPointerCount(); i++) {
                    id = event.getPointerId(i);
                    path = paths.get(id);
                    if (path != null) path.lineTo(event.getX(i), event.getY(i));
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                path = paths.get(id);
                if (path != null) {
                    drawCanvas.drawPath(path, drawPaint);
                    paths.remove(id);
                }
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint); //pinta na tela, se tirar apenas risca e apaga
        for (int i=0; i<paths.size(); i++) {
            canvas.drawPath(paths.valueAt(i), drawPaint);
        }
        //mostrar quantidade de fingers
        canvas.drawText("Total pointers: " + paths.size(), 10, 40 , textPaint);
    }

}
