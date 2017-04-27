package chinna.sandyz.com.asyncassignment114;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText enterurl;
    Button download_Button;
    ProgressBar download_progress;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterurl = (EditText)findViewById(R.id.enter_url);
        download_Button = (Button)findViewById(R.id.button);
        download_progress = (ProgressBar)findViewById(R.id.progressbar);
        imageView = (ImageView)findViewById(R.id.imageView);

        final String url = "https://s-media-cache-ak0.pinimg.com/736x/03/06/94/0306943d6477b34a3c59f1d7e3fcdfcb.jpg";

        enterurl.setText(url);

        download_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //handle
                imageHandler handleImage = new imageHandler(download_progress,imageView);
                download_progress.incrementProgressBy(100);
                // DOWNLOAD THREAD
                ImageDownload download = new ImageDownload(handleImage,url);
                download.start();
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();

            }
        });

    }

    class ImageDownload extends Thread{
        imageHandler imageHandler;
        String url;

        public ImageDownload(MainActivity.imageHandler imageHandler, String url) {
            this.imageHandler = imageHandler;
            this.url = url;
        }

        @Override
        public void run() {

            Drawable drawable = imageHandler.DownloadImage(url);
            Message message = imageHandler.obtainMessage(1,drawable);
            imageHandler.sendMessage(message);
            System.out.println("Message sent");


        }
    }
    //HANDLER CLASS
    class imageHandler extends Handler{
        ProgressBar progressBar;
        View imageview;

        public imageHandler(ProgressBar progressBar, View imageview) {
            this.progressBar = progressBar;
            this.imageview = imageview;
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setBackground((Drawable)msg.obj);
            imageView.setVisibility(View.VISIBLE);
        }

        //

        Drawable DownloadImage(String url)  {
            Drawable d =null;
            InputStream inputStream = null;

            try {
                //DOWNLOAD
                inputStream = (InputStream)new URL(url).getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            d=Drawable.createFromStream(inputStream,"srcName");

            return d;

        }
    }

}
