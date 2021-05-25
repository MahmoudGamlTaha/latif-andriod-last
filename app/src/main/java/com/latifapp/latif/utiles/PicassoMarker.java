package com.latifapp.latif.utiles;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Picasso target - load image into marker (Google map icon on map)
 * Created by Tomas Krabac[tomas.krabac@ackee.cz] on {16. 7. 2015}
 **/
public class PicassoMarker implements Target {
    Marker mMarker;
    ImageView mImgBike;
    IconGenerator mIconGenerator;

    public PicassoMarker(Marker marker, IconGenerator iconGenerator, ImageView imgBike) {
        mMarker = marker;
        mImgBike = imgBike;
        mIconGenerator = iconGenerator;
    }

    @Override
    public int hashCode() {
        return mMarker.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PicassoMarker) {
            Marker marker = ((PicassoMarker) o).mMarker;
            return mMarker.equals(marker);
        } else {
            return false;
        }
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        mImgBike.setImageBitmap(bitmap);
        Bitmap icon = mIconGenerator.makeIcon();
        try {
            mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
        } catch (IllegalArgumentException exception) {
            //just in case that marker is dead, it caused crash
            Log.d("nvnvnvnvnvnvn55555java", exception.getMessage());
        }
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
        Log.d("nvnvnvnvnvnvn55555java4", e.getMessage());

    }


    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
