package com.example.biblite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImgAdapter extends BaseAdapter {
    ArrayList<PodcastEpisode> eps = new ArrayList<PodcastEpisode>();
    private Context mContext;
    int imageCount;
    public ImgAdapter(Context c, ArrayList<PodcastEpisode> z) {
        eps = z;
        imageCount = eps.size();
        thumbImages = new Integer[imageCount];

        for(int i = 0; i<imageCount; i++)
            thumbImages[i] = R.drawable.bg;

        mContext = c;
    }
    public int getCount() {
        return thumbImages.length;
    }
    public Object getItem(int position) {
        return null;
    }
    public long getItemId(int position) {
        return 0;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
       // imageView.setLayoutParams(new GridView.LayoutParams(300, 300);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setPadding(8, 8, 8, 8);

        imageView.setImageBitmap(writeTextOnDrawable(thumbImages[position], eps.get(position).title, Integer.toString(position+1), eps.get(position).ready).getBitmap());
        return imageView;
    }
    // Add all our images to arraylist
    public Integer[] thumbImages;

    private BitmapDrawable writeTextOnDrawable(int drawableId, String text, String unlocked, boolean ready) {


        if (PodcastEpisode.opens +1 == PodcastEpisode.open )
        {
            ready = true;
            PodcastEpisode.opens ++;

        }
        else {
            ready = false;
            PodcastEpisode.opens ++;

        }
       // 5 -- strike on 7
        //6 -- strike on 8th call, and 8th call only
if (!ready)
    drawableId = R.drawable.bgr;

        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(mContext, 30));



        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);


        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        //if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
           // paint.setTextSize(convertToPixels(mContext, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;
        if (!ready) {
           paint.setColor(Color.RED);

        }
        else{
            paint.setColor(Color.GREEN);
        }

        paint.setAlpha(80);
        canvas.drawText("Week " + unlocked, xPos, (yPos/2)+10, paint);
        paint.setAlpha(1000);
        paint.setTextSize(convertToPixels(mContext, 17));
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create("Times New Roman",Typeface.BOLD));
        /*if (text.length() < 10){
            text = text.replace('-', ' ');
        canvas.drawText(text, xPos, yPos-50, paint);
        ;}
        else*/
        {
            text = text.replace('-', ' ');
            String[] texts = text.split(" ");

            String t1 = "";
            String t2 = "";
            String t3 = "";
            if (texts.length==1) t1 = texts[0];
            if (texts.length == 2)
            {
                t1 = texts[0];
                t2 = texts[1];
            }

            if (texts.length>2) {
                for (int i = 0; i < texts.length / 3; i++) {
                    t1 += texts[i] + " ";
                }
                for (int i = texts.length / 3; i < (texts.length / 3) + (texts.length / 3); i++) {
                    t2 += texts[i] + " ";
                }
                for (int i = (texts.length / 3) + (texts.length / 3); i < texts.length; i++) {
                    t3 += texts[i] + " ";
                }
            }
            canvas.drawText(t1, xPos, yPos-60, paint);
            canvas.drawText(t2, xPos, yPos , paint);
            canvas.drawText(t3, xPos, yPos + 60, paint);
        }

        bm = resizeDrawable(bm);

        return new BitmapDrawable(mContext.getResources(), resizeDrawable(bm));
    }
    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }

    private Bitmap resizeDrawable(Bitmap bm) {
        Bitmap b = bm;
        return Bitmap.createScaledBitmap(b, MainActivity.width, (int)(MainActivity.width*1.5), false);

    }


    public Bitmap makeTransparent(Bitmap src, int value) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap transBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        // config paint
        final Paint paint = new Paint();
        paint.setAlpha(value);
        canvas.drawBitmap(src, 0, 0, paint);
        return transBitmap;
    }
}