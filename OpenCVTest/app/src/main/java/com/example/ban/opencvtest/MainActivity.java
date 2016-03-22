package com.example.ban.opencvtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button runNegButton = (Button) findViewById(R.id.runNegButton);
        Button runBinButton = (Button) findViewById(R.id.runBinarizateButton);
        Button runConButton = (Button) findViewById(R.id.runDetectContourButton);
        Button runLineButton = (Button) findViewById(R.id.runDetectLinebutton);
        runNegButton.setEnabled(false);
        runBinButton.setEnabled(false);
        runConButton.setEnabled(false);
        runLineButton.setEnabled(false);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.result_1);
        imageView.setImageBitmap(image);
    }

    @Override
    public void onResume(){
        // 非同期でライブラリの読み込み/初期化を行う
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            //組み込みのopencvが見つからなかった場合はopencvManagerのインストールが促される
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            //組み込みopencvのロード成功
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    // ライブラリ初期化完了後に呼ばれるコールバック (onManagerConnected)
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.d("OK", "OK");
                    Button runNegButton = (Button) findViewById(R.id.runNegButton);
                    Button runBinButton = (Button) findViewById(R.id.runBinarizateButton);
                    Button runConButton = (Button) findViewById(R.id.runDetectContourButton);
                    Button runLineButton = (Button) findViewById(R.id.runDetectLinebutton);
                    runNegButton.setEnabled(true);
                    runBinButton.setEnabled(true);
                    runConButton.setEnabled(true);
                    runLineButton.setEnabled(true);
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void runNegButtonPushed(View view){

        Log.d("debug", "button pushed");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.result_1);
        Mat source = new Mat();
        Mat output = new Mat();
        Utils.bitmapToMat(bitmap, source);

        Core.absdiff(source, new Scalar(255, 255, 255), output);
        Bitmap dst = Bitmap.createBitmap(output.width(), output.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(output, dst);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(dst);

    }

    public void runBinarizateButtonPushed(View view){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.result_1);
        Mat source = new Mat();
        Mat output = new Mat();
        Utils.bitmapToMat(bitmap, source);

        Imgproc.cvtColor(source, source, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(source, output, 70, 255, Imgproc.THRESH_BINARY);
        Imgproc.cvtColor(output, output, Imgproc.COLOR_GRAY2BGRA, 4);

        Bitmap dst = Bitmap.createBitmap(output.width(), output.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(output, dst);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(dst);
    }

    public void runDetectContourButtonPushed(View view){

    }

    public void runDetectLineButtonPushed(View view){

    }
}
