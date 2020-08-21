package robsonmachczew.drawing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MultiCanvasView multiCanvasView;
    private SimpleCanvasView simpleCanvasView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        multiCanvasView = findViewById(R.id.multicanvas);
        simpleCanvasView = findViewById(R.id.simplecanvas);

        /*
        //acessar fragment via frmalayout
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.simple, new SimpleCanvasFragment());
        ft.add(R.id.multi, new MultiFragment());
        //ft.addToBackStack(null);
        ft.commit();
        */

    }

    public void clear(View view) {
        multiCanvasView.clearCanvas();
        simpleCanvasView.clearCanvas();
    }
}
