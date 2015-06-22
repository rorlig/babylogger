package com.rorlig.babylog.ui.fragment.profile;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.desmond.squarecamera.CameraActivity;
import com.rorlig.babylog.R;
import com.rorlig.babylog.otto.events.camera.CameraStartEvent;
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

    @OnClick(R.id.camera)
    public void onCameraClick() {
        Log.d(TAG, "onCameraClick");
//        EasyCamera camera = DefaultEasyCamera.open();
//        EasyCamera.CameraActions actions = null;
//        try {
//            actions = camera.startPreview(surface);
//            EasyCamera.PictureCallback callback = new EasyCamera.PictureCallback() {
//                public void onPictureTaken(byte[] data, EasyCamera.CameraActions actions) {
//                    // store picture
//                }
//            };
//            actions.takePicture(EasyCamera.Callbacks.create().withJpegCallback(callback));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        long callTime = System.currentTimeMillis();
        String dir = AppUtils.getCameraDirectory();
        File file = new File(dir, callTime + ".jpg");
        Uri imageUri = Uri.fromFile(file);
        Log.d(TAG, "imageUri " + imageUri);
        scopedBus.post(new CameraStartEvent(imageUri));
        Intent startCustomCameraIntent = new Intent(getActivity(), CameraActivity.class);
        startCustomCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        dismiss();
        getActivity().startActivityForResult(startCustomCameraIntent, AppUtils.RESULT_CAMERA_IMAGE_CAPTURE);
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        dismiss();
//
//        getActivity().startActivityForResult(intent, AppUtils.RESULT_CAMERA_IMAGE_CAPTURE);
    }

    @OnClick(R.id.gallery)
    public void onGalleryClick() {

//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setClassName("com.android.gallery3d", "com.android.camera.CropImage");
//        long callTime = System.currentTimeMillis();
//        String dir = AppUtils.getCameraDirectory();
//        File file = new File(dir, callTime + ".jpg");
//        Uri uri = Uri.fromFile(file);
//        intent.setData(uri);
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 96);
//        intent.putExtra("outputY", 96);
//        intent.putExtra("noFaceDetection", true);
//        intent.putExtra("return-data", true);
//        getActivity().startActivityForResult(intent,AppUtils.RESULT_LOAD_IMAGE);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        dismiss();

        getActivity().startActivityForResult(intent, AppUtils.RESULT_LOAD_IMAGE);
    }
}
