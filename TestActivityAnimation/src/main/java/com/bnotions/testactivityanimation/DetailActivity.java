package com.bnotions.testactivityanimation;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Observer;

/**
 * Created by camillebossoutrot on 13-05-21.
 */
public class DetailActivity extends Activity {

    public static final int DURATION = 500; // in ms
    public static final String PACKAGE = "com.bnotions.testactivityanimation";

    private ImageView image_view;
    private RelativeLayout bg_layout;

    private ColorDrawable bg_drawable;
    private int delta_top;
    private int delta_left;
    private float scale_width;
    private float scale_height;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        image_view = (ImageView) findViewById(R.id.image_view);
        image_view.setImageResource(R.drawable.picture);
        bg_layout = (RelativeLayout) findViewById(R.id.bg_layout);


        if (savedInstanceState == null) {

            Bundle bundle = getIntent().getExtras();

            final int top = bundle.getInt(PACKAGE + ".top");
            final int left = bundle.getInt(PACKAGE + ".left");
            final int width = bundle.getInt(PACKAGE + ".width");
            final int height = bundle.getInt(PACKAGE + ".height");

            bg_drawable = new ColorDrawable(Color.BLACK);
            bg_layout.setBackground(bg_drawable);

            ViewTreeObserver observer = image_view.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    image_view.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screen_location = new int[2];
                    image_view.getLocationOnScreen(screen_location);

                    delta_left = left - screen_location[0];
                    delta_top = top - screen_location[1];

                    scale_width = (float) width / image_view.getWidth();
                    scale_height = (float) height / image_view.getHeight();

                    runEnterAnimation();

                    return true;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        runExitAnimation(new Runnable() {
            @Override
            public void run() {

                finish();
            }
        });

    }

    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(0, 0);
    }

    private void runEnterAnimation() {

        image_view.setPivotX(0.f);
        image_view.setPivotY(0.f);
        image_view.setScaleX(scale_width);
        image_view.setScaleY(scale_height);
        image_view.setTranslationX(delta_left);
        image_view.setTranslationY(delta_top);

        image_view.animate().
                setDuration(DURATION).
                scaleX(1.f).
                scaleY(1.f).
                translationX(0.f).
                translationY(0.f).
                setInterpolator(new DecelerateInterpolator());

        ObjectAnimator bg_anim = ObjectAnimator.ofInt(bg_drawable, "alpha", 0, 255);
        bg_anim.setDuration(DURATION);
        bg_anim.start();

    }

    private void runExitAnimation(Runnable end_action) {

        image_view.setPivotX(0.f);
        image_view.setPivotY(0.f);
        image_view.setScaleX(1.f);
        image_view.setScaleY(1.f);
        image_view.setTranslationX(0.f);
        image_view.setTranslationY(0.f);

        image_view.animate().
                setDuration(DURATION).
                scaleX(scale_width).
                scaleY(scale_height).
                translationX(delta_left).
                translationY(delta_top).
                setInterpolator(new DecelerateInterpolator()).
                withEndAction(end_action);

        ObjectAnimator bg_anim = ObjectAnimator.ofInt(bg_drawable, "alpha", 255, 0);
        bg_anim.setDuration(DURATION);
        bg_anim.start();

    }
}