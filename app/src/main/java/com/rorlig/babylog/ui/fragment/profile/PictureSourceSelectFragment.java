package com.rorlig.babylog.ui.fragment.profile;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.desmond.squarecamera.CameraActivity;
import com.rorlig.babylog.R;
import com.rorlig.babylog.otto.GalleryEvent;
import com.rorlig.babylog.otto.events.camera.CameraStartEvent;
import com.rorlig.babylog.ui.PictureInterface;
import com.rorlig.babylog.ui.fragment.InjectableDialogFragment;
import com.rorlig.babylog.utils.AppUtils;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rorlig on 7/16/14.
 */
public class PictureSourceSelectFragment extends InjectableDialogFragment {
    enum PictureSourceItem {
        CAMERA, GALLERY
    }

    @InjectView(R.id.camera)
    ImageView cameraImageView;

    @InjectView(R.id.gallery)
    ImageView galleryImageView;
    private String TAG = "PictureSourceSelectFragment";
    private SurfaceTexture surface;

    public PictureSourceSelectFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        surface = new SurfaceTexture(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_dialog_picture_source_select, container);


        ButterKnife.inject(this, view);
        return view;
    }

    /*
     * start the camera activity
     */
    @OnClick(R.id.camera_select)
    public void onCameraClick() {
        Log.d(TAG, "onCameraClick");
        PictureInterface pictureInterface = (PictureInterface) getTargetFragment();
        pictureInterface.handleCameraEvent();
        dismiss();
    }

    /*
     * start the gallery application..
     */
    @OnClick(R.id.gallery_select)
    public void onGalleryClick() {
        PictureInterface pictureInterface = (PictureInterface) getTargetFragment();
        pictureInterface.handleGalleryEvent();
        dismiss();


    }

    /*
     * delete the current image
     */
    @OnClick(R.id.delete)
    public void onDeleteImage() {
        PictureInterface pictureInterface = (PictureInterface) getTargetFragment();
        pictureInterface.deleteImage();
        dismiss();
    }
}
