package com.bnotions.testactivityanimation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

public class MainActivity extends Activity {

    public static final int THUMBNAIL_HEIGHT = 150; // in px
    public static final String PACKAGE = "com.bnotions.testactivityanimation";

    private GridLayout grid_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid_layout = (GridLayout) findViewById(R.id.grid_layout);
        grid_layout.setColumnCount(3);
        grid_layout.setUseDefaultMargins(true);

        Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.picture);
        float ratio = (float) original.getWidth() / original.getHeight();
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(original, (int) (THUMBNAIL_HEIGHT *ratio), THUMBNAIL_HEIGHT);
        BitmapDrawable thumbnail_drawable = new BitmapDrawable(getResources(), thumbnail);

        for (int i=0; i < 12; i++) {
            ImageView image_view = new ImageView(this);
            image_view.setImageDrawable(thumbnail_drawable);
            image_view.setOnClickListener(thumbnail_click_listener);
            grid_layout.addView(image_view);
        }
    }

    View.OnClickListener thumbnail_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int[] screen_location = new int[2];
            view.getLocationOnScreen(screen_location);

            Intent new_intent = new Intent(MainActivity.this, DetailActivity.class);
            new_intent.putExtra(PACKAGE + ".left", screen_location[0]).
                    putExtra(PACKAGE + ".top", screen_location[1]).
                    putExtra(PACKAGE + ".width", view.getWidth()).
                    putExtra(PACKAGE + ".height", view.getHeight());

            startActivity(new_intent);
            overridePendingTransition(0, 0);

        }
    };


}
