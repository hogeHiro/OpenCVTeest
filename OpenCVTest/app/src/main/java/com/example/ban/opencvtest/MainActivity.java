package com.example.ban.opencvtest;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String PACAGE_NAME = "com.example.ban.opencvtest";
    //    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/OpenCVTest";
    public static String DATA_PATH = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        DATA_PATH = getFilesDir().getPath();
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
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.raw.result_1);
        imageView.setImageBitmap(image);

        File dataDir = new File(DATA_PATH);
        try {
            AssetManager assetManager = getAssets();
            String list[] = assetManager.list("ocr");
            for (String filePath : list) {
                File newFile = new File(DATA_PATH + "/tessdata/" + filePath);
                if (!newFile.exists()) {
//                        BufferedReader is = new BufferedReader(new InputStreamReader(assetManager.open("ocr/" + filePath)));
//                        newFile.getParentFile().mkdirs();
//                        newFile.createNewFile();
//                        BufferedWriter os = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile)));


//                        String line;
//                        while ((line = is.readLine()) != null) {
//                            os.write(line);
//                            os.newLine();
//                        }
                    InputStream is = assetManager.open("ocr/" + filePath);
                    newFile.getParentFile().mkdirs();
//                        newFile.createNewFile();
                    OutputStream os = new FileOutputStream(newFile);
                    byte[] buf = new byte[1024];
                    int len;
                    //while ((lenf = gin.read(buff)) > 0) {
                    while ((len = is.read(buf)) > 0) {
                        os.write(buf, 0, len);
                    }

                    is.close();
//                        os.flush();
                    os.close();
                    newFile.setReadable(true, false);
                    newFile.setWritable(true, false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onResume() {
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


    public void runNegButtonPushed(View view) {

        Log.d("debug", "button pushed");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.result_1);
        Mat source = new Mat();
        Mat output = new Mat();
        Utils.bitmapToMat(bitmap, source);

        Core.absdiff(source, new Scalar(255, 255, 255), output);
        Bitmap dst = Bitmap.createBitmap(output.width(), output.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(output, dst);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(dst);

    }

    public void runBinarizateButtonPushed(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.result_1);
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

    public void runDetectContourButtonPushed(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.result_1);
        Mat source = new Mat();
        Mat output = new Mat();
        Utils.bitmapToMat(bitmap, source);

        Imgproc.cvtColor(source, source, Imgproc.COLOR_RGB2GRAY);
        Imgproc.morphologyEx(source, source, Imgproc.MORPH_OPEN, new Mat(), new Point(-1, -1), 8);
        Imgproc.Canny(source, output, 10, 200);

        Bitmap dst = Bitmap.createBitmap(output.width(), output.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(output, dst);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(dst);
    }

    public void runDetectLineButtonPushed(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.result_1).copy(Bitmap.Config.ARGB_8888, true);
        Mat source = new Mat();
        Utils.bitmapToMat(bitmap, source);

        Bitmap bitmapTemplate = BitmapFactory.decodeResource(getResources(), R.raw.result_template2).copy(Bitmap.Config.ARGB_8888, true);
        Mat template = new Mat();
        Utils.bitmapToMat(bitmapTemplate, template);

        Mat output = new Mat();
        Imgproc.matchTemplate(source, template, output, Imgproc.TM_CCOEFF_NORMED);
        Core.MinMaxLocResult maxResult = Core.minMaxLoc(output);
        Log.d("temp max", String.format("max = %f", maxResult.maxVal));

        Point point = maxResult.maxLoc;
        Point endPoint = new Point(point.x + template.width(), point.y + template.height());

        Mat rect = new Mat(source, new Rect((int) point.x, (int) point.y, template.width(), template.height()));
        int score = getScore(rect);
        Log.d("score", String.format("score = %d", score));

        Point p1 = maxResult.maxLoc;
        Point p2 = new Point(p1.x + template.width() - 2, p1.y + template.height() - 2);
        Imgproc.rectangle(source, p1, p2, new Scalar(255, 0, 0), -1);
        Bitmap dst = Bitmap.createBitmap(source.width(), source.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(source, dst);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(dst);

    }

    public void runOCRButtonPushed(View view) {
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inScaled = false;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.result_1, opt).copy(Bitmap.Config.ARGB_8888, true);
            Mat source = new Mat();
            Utils.bitmapToMat(bitmap, source);

            Bitmap bitmapTemplate = BitmapFactory.decodeResource(getResources(), R.raw.result_template2, opt).copy(Bitmap.Config.ARGB_8888, true);
            Mat template = new Mat();
            Utils.bitmapToMat(bitmapTemplate, template);

            Mat output = new Mat();
            Imgproc.matchTemplate(source, template, output, Imgproc.TM_CCOEFF_NORMED);
            Core.MinMaxLocResult maxResult = Core.minMaxLoc(output);
            Point point = maxResult.maxLoc;
            Point endPoint = new Point(point.x + template.width(), point.y + template.height());
            Mat rect = new Mat(source, new Rect((int) point.x, (int) point.y, template.width(), template.height()));

            int dims = 1;
            int cool = getNumber(new Mat(rect, new Rect(430 * dims, 41 * dims, 51 * dims, 19 * dims)));
            int great = getNumber(new Mat(rect, new Rect(435 * dims, 58 * dims, 46 * dims, 21 * dims)));
            int good = getNumber(new Mat(rect, new Rect(430 * dims, 80 * dims, 51 * dims, 19 * dims)));
            int bad = getNumber(new Mat(rect, new Rect(430 * dims, 100 * dims, 51 * dims, 19 * dims)));
            Log.d("ocr", "cool:" + cool);
            Log.d("ocr", "great:" + great);
            Log.d("ocr", "good:" + good);
            Log.d("ocr", "bad:" + bad);

            String nl = System.getProperty("line.separator");
            String str = new String("cool: " + cool + nl + "great: " + great + nl + "good: " + good + nl + "bad: " + bad );
            Toast.makeText(this, str, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    TessBaseAPI ocrAPI = null;

    private int getNumber(Mat mat) {
        if (ocrAPI == null) {
            ocrAPI = new TessBaseAPI();
            ocrAPI.init(DATA_PATH, "eng");
        }
        Bitmap bmp = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bmp);

        ocrAPI.setImage(bmp);
        String result = ocrAPI.getUTF8Text();
        try {
            int number = Integer.parseInt(result);
            return number;
        } catch (Exception e){
            e.printStackTrace();
        }

        return -1;
    }

    private int getScore(Mat result) {
        double rate = 274 / result.size().height;
        rate *= 44.0 / 16.0;
        ArrayList<Mat> scoreMatList = getScoreMatList(rate);
        //       Imgproc.resize(result, result, new Size(result.size().width * rate, result.size().height * rate));
        ArrayList<ScorePoint> scorePointList = new ArrayList<>();
        int count = 0;
        double threshold = 0.90;


        LOOP:
        while (threshold > 0) {
            Log.d("score", String.format("threshold = %f", threshold));
            for (int number = 0; number < scoreMatList.size(); number++) {
                Mat mat = scoreMatList.get(number);

                while (true) {
//                    Bitmap score0 = BitmapFactory.decodeResource(getResources(), R.drawable.score_1).copy(Bitmap.Config.ARGB_8888, true);
//                    Mat template = new Mat();
//                    Utils.bitmapToMat(score0, template);

                    Mat output = new Mat();
                    Imgproc.matchTemplate(result, mat, output, Imgproc.TM_CCOEFF_NORMED);
                    Core.MinMaxLocResult matchResult = Core.minMaxLoc(output);
                    Log.d("score", String.format(" %d match = %f", number, matchResult.maxVal));
                    if (matchResult.maxVal > threshold) {
                        Point p1 = matchResult.maxLoc;
                        Point p2 = new Point(p1.x + mat.width(), p1.y + mat.height());
                        Imgproc.rectangle(result, p1, p2, new Scalar(255, 0, 0), -1);
                        scorePointList.add(new ScorePoint((int) p1.x, (int) p1.y, number));
                        Log.d("", "add");
                        if (scorePointList.size() >= 5) break LOOP;
                    } else {
                        break;
                    }
                }

            }
            threshold -= 0.05;
        }

        if (scorePointList.size() != 5) return 0;
        return calScoreFromScorePointList(scorePointList);
    }

    private int calScoreFromScorePointList(ArrayList<ScorePoint> list) {
        int score = 0;
        while (!list.isEmpty()) {
            int min = Integer.MAX_VALUE;
            int index = -1;
            for (int i = 0; i < list.size(); i++) {
                ScorePoint point = list.get(i);
                if (point.x < min) {
                    min = point.x;
                    index = i;
                }
            }

            score += list.get(index).number * Math.pow(10, list.size());
            list.remove(index);
        }
        return score;
    }

    private ArrayList<Mat> getScoreMatList(double rate) {
        ArrayList<Mat> scoreMatList = new ArrayList<Mat>();

        Bitmap score0 = BitmapFactory.decodeResource(getResources(), R.raw.score_0).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap score1 = BitmapFactory.decodeResource(getResources(), R.raw.score_1).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap score2 = BitmapFactory.decodeResource(getResources(), R.raw.score_2).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap score3 = BitmapFactory.decodeResource(getResources(), R.raw.score_3).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap score4 = BitmapFactory.decodeResource(getResources(), R.raw.score_4).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap score5 = BitmapFactory.decodeResource(getResources(), R.raw.score_5).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap score6 = BitmapFactory.decodeResource(getResources(), R.raw.score_6).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap score7 = BitmapFactory.decodeResource(getResources(), R.raw.score_7).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap score8 = BitmapFactory.decodeResource(getResources(), R.raw.score_8).copy(Bitmap.Config.ARGB_8888, true);
        Bitmap score9 = BitmapFactory.decodeResource(getResources(), R.raw.score_9).copy(Bitmap.Config.ARGB_8888, true);

        Mat mat0 = new Mat();
        Utils.bitmapToMat(score0, mat0);
//        Imgproc.resize(mat0, mat0, new Size(mat0.size().width * rate, mat0.size().height * rate));
        scoreMatList.add(mat0);

        Mat mat1 = new Mat();
        Utils.bitmapToMat(score1, mat1);
//        Imgproc.resize(mat1, mat1, new Size(mat1.size().width * rate, mat1.size().height * rate));
        scoreMatList.add(mat1);

        Mat mat2 = new Mat();
        Utils.bitmapToMat(score2, mat2);
//        Imgproc.resize(mat2, mat2, new Size(mat2.size().width * rate, mat2.size().height * rate));
        scoreMatList.add(mat2);

        Mat mat3 = new Mat();
        Utils.bitmapToMat(score3, mat3);
//        Imgproc.resize(mat3, mat3, new Size(mat3.size().width * rate, mat3.size().height * rate));
        scoreMatList.add(mat3);

        Mat mat4 = new Mat();
        Utils.bitmapToMat(score4, mat4);
//        Imgproc.resize(mat4, mat4, new Size(mat4.size().width * rate, mat4.size().height * rate));
        scoreMatList.add(mat4);

        Mat mat5 = new Mat();
        Utils.bitmapToMat(score5, mat5);
//        Imgproc.resize(mat5, mat5, new Size(mat5.size().width * rate, mat5.size().height * rate));
        scoreMatList.add(mat5);

        Mat mat6 = new Mat();
        Utils.bitmapToMat(score6, mat6);
//        Imgproc.resize(mat6, mat6, new Size(mat6.size().width * rate, mat6.size().height * rate));
        scoreMatList.add(mat6);

        Mat mat7 = new Mat();
        Utils.bitmapToMat(score7, mat7);
//        Imgproc.resize(mat7, mat7, new Size(mat7.size().width * rate, mat7.size().height * rate));
        scoreMatList.add(mat7);

        Mat mat8 = new Mat();
        Utils.bitmapToMat(score8, mat8);
//        Imgproc.resize(mat8, mat8, new Size(mat8.size().width * rate, mat8.size().height * rate));
        scoreMatList.add(mat8);

        Mat mat9 = new Mat();
        Utils.bitmapToMat(score9, mat9);
//        Imgproc.resize(mat9, mat9, new Size(mat9.size().width * rate, mat9.size().height * rate));
        scoreMatList.add(mat9);

        return scoreMatList;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.ban.opencvtest/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.ban.opencvtest/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}


class ScorePoint{

    public int x, y;
    public int number;

    public ScorePoint(int x, int y, int number){
        this.x = x;
        this.y = y;
        this.number = number;
    }
}
