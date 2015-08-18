package com.rorlig.babyapp.ui.fragment.profile;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.rorlig.babyapp.R;
import com.rorlig.babyapp.ui.PictureInterface;
import com.rorlig.babyapp.ui.fragment.InjectableDialogFragment;

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

    @InjectView(R.id.delete_image)
    ImageView deleteImageView;

    @InjectView(R.id.gallery_text)
    TextView galleryTextView;

    @InjectView(R.id.camera_text)
    TextView cameraTextView;


    @InjectView(R.id.delete_text)
    TextView deleteTextView;



    private String TAG = "PictureSourceSelectFragment";
    private SurfaceTexture surface;

    public PictureSourceSelectFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        surface = new SurfaceTexture(1);
        if (getArguments()!=null) {
            int color = getArguments().getInt("color");

            changeImageViewColor(cameraImageView, color);
            changeImageViewColor(galleryImageView, color);
            changeImageViewColor(deleteImageView, color);
            galleryTextView.setTextColor(getResources().getColor(color));
            cameraTextView.setTextColor(getResources().getColor(color));
            deleteTextView.setTextColor(getResources().getColor(color));


        }
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

    private void changeImageViewColor(ImageView imageView, int color) {
        Resources res = getActivity().getResources();
//        final ImageView image = (ImageView) findViewById(R.id.imageId);
        final int newColor = res.getColor(color);
        imageView.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
    }
}
