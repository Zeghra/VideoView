package com.example.david.videoview;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.VideoView;

import com.example.david.videoview.Utils.Valtozok;

public class MainActivity extends FragmentActivity { // extends AppCompatActivity, FragmentActivity kell, hogy teljesképernyőn játsza a videot

    private VideoView videoView;
    private boolean videoEgyLejatszas = false; // az 1-es videot figyeli, hogy lejátszás alatt van vagy vége van

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        videoView = (VideoView) findViewById(R.id.videoView); // összekötöm a layout-tal a videoView-t

        videoNullaLejatszas(videoView); // elinditja a 0. videot

    }

    // gomb lenyomása
    @Override
    public boolean onKeyUp(final int keyCode, final KeyEvent event) {
        switch (keyCode) {
            case Valtozok.VIDEO_EGY_GOMB:
                if (!videoEgyLejatszas) { //ha az 1. videot már nem játsza le, akkor inditja az 1. videot újra
                    videoEgyLejatszas(videoView);  // 1. video inditása
                    Log.e("Video", "1-es gomb felengedve");
                    return true;
                }
                Log.e("Video", "1-es gomb felengedve, de még lejátszik");
                break;

            case Valtozok.VIDEO_KETTO_GOMB:
                videoKettoLejatszas(videoView); // inditja a 2-es videot, itt nem kellett figyelni, hogy a 2-es gombot újra felengedik
                Log.e("Video", "2-es gomb felengedve");
                return true;

            case Valtozok.VIDEO_HAROM_GOMB:
                videoHaromLejatszas(videoView); // inditja a 3-es videot, itt nem kellett figyelni, hogy a 3-es gombot újra felengedik
                Log.e("Video", "3-as gomb felengedve");
                return true;

            default:
                return super.onKeyUp(keyCode, event);
        }
        return true;
    }

    public void videoNullaLejatszas(View v) {  // konstruktor a 0. video inditásához

        videoEgyLejatszas = false; // 1. video nincs lejátszás alatt
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        }); // loopolja a 0. videot
        String videoPath = Valtozok.VIDEOPATH + Valtozok.VIDEO_NULLA; // 0. video elérési útja
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri); // elérési utat beállit
        videoView.start(); // inditja a videot
    }

    public void videoEgyLejatszas(final View v) {  // konstruktor az 1. video inditásához
        attunes(v);
        String videoPath = Valtozok.VIDEOPATH + Valtozok.VIDEO_EGY;  // 1. video elérési útja
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();
        videoEgyLejatszas = true;  // a video lejátszás igaz
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  // figyeli ha vége a videonak
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!mp.isPlaying()) {
                    videoEgyLejatszas = false;  // ha már nem játsza a videot, akkor false
                }

                mp.stop();  // a video végén leáll
                videoNullaLejatszas(videoView); //és elinditja a 0. videot
            }
        });

    }

    public void videoKettoLejatszas(final View v) {
        attunes(v);
        videoEgyLejatszas = false; // itt nem kell figyelni, hogy játsza-e az 1. videot
        String videoPath = Valtozok.VIDEOPATH + Valtozok.VIDEO_KETTO;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                videoNullaLejatszas(videoView);
            }
        });

    }

    public void videoHaromLejatszas(final View v) {
        attunes(v);
        videoEgyLejatszas = false;
        String videoPath = Valtozok.VIDEOPATH + Valtozok.VIDEO_HAROM;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //attunesSotet(v);
                mp.stop();
                videoNullaLejatszas(videoView);
            }
        });

    }

    public void attunes(View v) {
        //0.0f invisible, 1.0f visible
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        Log.e("Video", "háttér: " + animation.getBackgroundColor());
        animation.setDuration(Valtozok.ATTUNESI_IDO * 1000); // időtartam millisec.
        v.startAnimation(animation);
        v.setVisibility(View.VISIBLE);

    }

    public void attunesSotet(View v) { //  a lejátszott videot elsötétiti
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(Valtozok.ATTUNESI_IDO * 1000); // időtartam millisec.
        v.startAnimation(animation);
        v.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        MediaPlayer mp = new MediaPlayer();
        mp.release(); // ezt találtam, nem tudom, hogy kell-e, azt irta, jó ha lekapcsoljuk, ha már nem használjuk a Media Playert
        super.onStop();     // akkumlátor használat miatt
    }
}
