package com.rorlig.babylog.ui.fragment.milestones;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.camera.CropImageIntentBuilder;
import com.desmond.squarecamera.CameraActivity;
import com.gc.materialdesign.views.Button;
import com.j256.ormlite.dao.Dao;
import com.rorlig.babylog.R;
import com.rorlig.babylog.dagger.ForActivity;
import com.rorlig.babylog.dao.MilestonesDao;
import com.rorlig.babylog.db.BabyLoggerORMLiteHelper;
import com.rorlig.babylog.otto.MilestoneItemCreated;
import com.rorlig.babylog.otto.events.ui.FragmentCreated;
import com.rorlig.babylog.ui.PictureInterface;
import com.rorlig.babylog.ui.fragment.InjectableFragment;
import com.rorlig.babylog.ui.fragment.profile.PictureSourceSelectFragment;
import com.rorlig.babylog.ui.widget.DateTimeHeaderFragment;
import com.rorlig.babylog.utils.AppUtils;
import com.rorlig.babylog.utils.transform.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by gaurav
 * Milestone fragment
 */
public class MilestoneFragment extends InjectableFragment implements PictureInterface {

    @ForActivity
    @Inject
    Context context;



    @InjectView(R.id.two_button_layout)
    LinearLayout editDeleteBtn;



    @InjectView(R.id.custom_milestone_text)
    AutoCompleteTextView customMilestonesTextView;



    @InjectView(R.id.save_btn)
    Button saveBtn;


    @InjectView(R.id.notes)
    EditText notes;

    @InjectView(R.id.milestone_pic)
    ImageView mileStoneImageView;

    @Inject
    BabyLoggerORMLiteHelper babyLoggerORMLiteHelper;

    @Inject
    Picasso picasso;




    DateTimeHeaderFragment dateTimeHeader;

    private String TAG = "MilestoneFragment";

    private EventListener eventListener = new EventListener();
    private boolean heightEmpty = true;
    private boolean weightEmpty = true;
    private boolean headMeasureEmpty = true;
    private int id = -1;
    private boolean showEditDelete = false;
    private ArrayAdapter<CharSequence> milestoneAdapter;
    private boolean milestoneEmpty = true;
    private File croppedImageFile;
    private Uri croppedImage;
    private Uri imageUri;
    private PictureSourceSelectFragment pictureSourceSelectFragment;


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);


        scopedBus.post(new FragmentCreated("Milestone Fragment"));



        milestoneAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.milestones,android.R.layout.simple_list_item_1);
        customMilestonesTextView.setAdapter(milestoneAdapter);


        saveBtn.setEnabled(false);



        dateTimeHeader = (DateTimeHeaderFragment)(getChildFragmentManager().findFragmentById(R.id.header));
        Log.d(TAG, " green color " + Integer.toString(R.color.primary_green, 16));
        dateTimeHeader.setColor(DateTimeHeaderFragment.DateTimeColor.ORANGE);

        notes.setOnEditorActionListener(doneActionListener);

        //initialize views if not creating new feed item
        if (getArguments()!=null) {
            Log.d(TAG, "arguments are not null");
            id = getArguments().getInt("id");
            initViews(id);
        }

        setUpTextWatchers();


    }

    private void initViews(int id) {

        Log.d(TAG, "initViews " + id);
        try {
            MilestonesDao milestonesDao = babyLoggerORMLiteHelper.getMilestonesDao().queryForId(id);
            Log.d(TAG, milestonesDao.toString());
            editDeleteBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);

            //convert weight to pound.ounces

//            String.val
//            weightEditText.setText(convertWeightToString(growthDao.getWeight()));
//            heightInchesEditText.setText(growthDao.getHeight().toString());
//            headInchesEditText.setText(growthDao.getHeadMeasurement().toString());
            notes.setText(milestonesDao.getNotes());
            dateTimeHeader.setDateTime(milestonesDao.getDate());
            customMilestonesTextView.setText(milestonesDao.getTitle());

            Log.d(TAG, "imagePath: " + milestonesDao.getImagePath());
            updateImageUri(milestonesDao.getImagePath());
//            Uri imageUri  = Uri.parse(milestonesDao.getImagePath());
//            mileStoneImageView.setImageURI(null);

//            Log.d(TAG, "update the image " + imageUri.toString());

//            picasso.load(imageUri)
//                    .fit()
//                    .transform(new CircleTransform())
//                    .into(mileStoneImageView);

//            mileStoneImageView.setImageURI(imageUri);
//            weightEmpty = false;
//            heightEmpty = false;
//            if (!headInchesEditText.getText().toString().equals("")) {
//                headMeasureEmpty = false;
//            }
            showEditDelete = true;

            milestoneEmpty = false;



            setSaveEnabled();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private EditText.OnEditorActionListener doneActionListener = new EditText.OnEditorActionListener(){

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Log.d(TAG, "onEditorAction view " + v.getText() + " actionId " + actionId + " event " + event);
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createOrEdit();
                return true;
            }
            return false;
        }
    };


    private void setUpTextWatchers() {

        customMilestonesTextView.addTextChangedListener(new TextWatcher() {
            int len = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "beforeTextChanged ");
                String str = customMilestonesTextView.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged ");

                String str = customMilestonesTextView.getText().toString();

                Log.d(TAG, "str " + str + " str length " + str.length() + " len " + len);

                if (str.length() > 0) {
                    milestoneEmpty = false;
                } else {
                    milestoneEmpty = true;
                }
                setSaveEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged ");

            }
        });
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu");
        if (!milestoneEmpty) {
            Log.d(TAG, "disable the action_add");

            menu.findItem(R.id.action_add).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_save_white));

        } else {
            menu.findItem(R.id.action_add).setEnabled(false);
            menu.findItem(R.id.action_add).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_save_gray));

        }
    }

    private void setSaveEnabled() {

        getActivity().invalidateOptionsMenu();

        if (!milestoneEmpty) {
            saveBtn.setEnabled(true);
            notes.setImeOptions(EditorInfo.IME_ACTION_DONE);
        } else {
            saveBtn.setEnabled(false);
            notes.setImeOptions(EditorInfo.IME_ACTION_NONE);

        }

    }


    @OnClick(R.id.save_btn)
    public void saveBtnClicked() {
        createOrEdit();
    }

    public BabyLoggerORMLiteHelper getBabyLoggerORMLiteHelper() {
        return babyLoggerORMLiteHelper;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_milestone, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_add:
                createOrEdit();
                return true;
            case R.id.action_delete:
                onDeleteBtnClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (!showEditDelete) {
            inflater.inflate(R.menu.menu_add_item, menu);
        } else {
            inflater.inflate(R.menu.menu_edit_delete_item, menu);
        }
    }





    private class EventListener {
        public EventListener() {

        }
    }


//    /*
//   * creates a temporary growth item from the local view values...
//   */
//    private GrowthDao createLocalGrowthDao() {
//
//        Date date = dateTimeHeader.getEventTime();
//
//        String weight = weightEditText.getText().toString();
//
//
//        int indexOfDot = weight.indexOf(".");
//
//        Integer weightPounds = Integer.parseInt(weight.substring(0, indexOfDot==-1? weight.length(): indexOfDot));
//
//        Double totalWeight =  weightPounds.doubleValue();
//
//
//        if (weight.length()>3) {
//            Integer weightOunces = Integer.parseInt(weight.substring(3));
//            totalWeight+=weightOunces.doubleValue()/16;
//        }
//
//
//
//        Double height  = Double.parseDouble(heightInchesEditText.getText().toString());
//
//        Double headMeasure = -1.0;
//
//        if (!headInchesEditText.getText().toString().equals("")) {
//            headMeasure =  Double.parseDouble(headInchesEditText.getText().toString());
//        }
//
//
//
//        return new GrowthDao(totalWeight, height, headMeasure, notesContentTextView.getText().toString(), date);
//    }

    @OnClick(R.id.edit_btn)
    public void onEditBtnClicked(){
        Log.d(TAG, "edit btn clicked");
        createOrEdit();
    }

    /*
     */
    private void createOrEdit() {
        Dao<MilestonesDao, Integer> milestonesDao;
        try {

            milestonesDao = createGrowthDao();
            MilestonesDao daoObject = createLocalObject();

            if (daoObject!=null) {
                if (id!=-1) {
                    Log.d(TAG, "updating it");
                    daoObject.setId(id);
                    milestonesDao.update(daoObject);
                } else {
                    Log.d(TAG, "creating it");
                    milestonesDao.create(daoObject);
                }

                Log.d(TAG, "created objected " + daoObject);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        scopedBus.post(new MilestoneItemCreated());

    }

    private MilestonesDao createLocalObject() {
        Date date = dateTimeHeader.getEventTime();

        String imagePath = imageUri==null || imageUri.getPath() ==null ? "": imageUri.getPath();
//        String milestone = customMilestonesTextView.getText().toString();
        return new MilestonesDao(customMilestonesTextView.getText().toString(),
                                notes.getText().toString(),
                                date, imagePath);
    }

    /*
     * deletes the feed item...
     */
    @OnClick(R.id.delete_btn)
    public void onDeleteBtnClicked(){
        Log.d(TAG, "delete btn clicked");
        Dao<MilestonesDao, Integer> daoObject;
        try {
            daoObject = createGrowthDao();
            if (id!=-1) {
                Log.d(TAG, "updating it");
                daoObject.deleteById(id);
            }
            scopedBus.post(new MilestoneItemCreated());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Dao<MilestonesDao, Integer> createGrowthDao() throws SQLException {
        return babyLoggerORMLiteHelper.getMilestonesDao();
    }


    public void handleGalleryEvent() {
        Log.d(TAG, "onGalleryEvent");
        Log.d(TAG, " getActivity " + getActivity());
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(intent, AppUtils.RESULT_LOAD_IMAGE);
    }

    public void handleCameraEvent() {
        Log.d(TAG, "onCameraEvent");
        long callTime = System.currentTimeMillis();
        String dir = AppUtils.getCameraDirectory();
        File file = new File(dir, callTime + ".jpg");
        Uri imageUri = Uri.fromFile(file);
        Log.d(TAG, "imageUri " + imageUri);
//            scopedBus.post(new CameraStartEvent(imageUri));
        Log.d(TAG, " getActivity " + getActivity());
        Intent startCustomCameraIntent = new Intent(getActivity(), CameraActivity.class);
        startCustomCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            dismiss();
        startActivityForResult(startCustomCameraIntent, AppUtils.RESULT_CAMERA_IMAGE_CAPTURE);
    }

    @Override
    public void deleteImage() {
//        preferences.edit().putString("imageUri", "").apply();
        imageUri = null;
        resetImageView();

    }

    private void resetImageView() {
        mileStoneImageView.setImageURI(null);
        mileStoneImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.boy_normal));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        Log.d("on result:", "onActivityResult:" + resultCode + " request:" + requestCode + " data " + data);
        //Request was successful
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AppUtils.RESULT_LOAD_IMAGE:
                    imagePicked(data);
                    break;
                case AppUtils.RESULT_CAMERA_IMAGE_CAPTURE:
                    cameraImageCaptured(data);
                    break;
                case AppUtils.RESULT_CROP_IMAGE:
                    Log.d(TAG, "croppedImage URI " + croppedImage);
                    updateImageUri(croppedImage.toString());
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }


    @OnClick(R.id.milestone_pic)
    public void setBabyPicImageViewClicked(){
        pictureSourceSelectFragment = new PictureSourceSelectFragment();
        pictureSourceSelectFragment.setTargetFragment(this, 1);
        pictureSourceSelectFragment.show(getActivity().getSupportFragmentManager(), "picture_select_fragment");
    }

    private void cameraImageCaptured(Intent data) {

        Log.d(TAG, "cameraImageCaptured : " + data.getData());

        Uri returnedUri;

        if(data != null) {
            returnedUri = data.getData();

            if(returnedUri != null) {

                Log.d(TAG, "imageUri " + imageUri);

                croppedImageFile = new File(getActivity().getFilesDir(), "test_" + System.currentTimeMillis() + "_.jpg");

                croppedImage = Uri.fromFile(croppedImageFile);

                CropImageIntentBuilder cropImage = new CropImageIntentBuilder(200, 200, croppedImage);
                cropImage.setOutlineColor(0xFF03A9F4);
                cropImage.setSourceImage(returnedUri);

                Log.d(TAG, "cropping image");
                startActivityForResult(cropImage.getIntent(getActivity()), AppUtils.RESULT_CROP_IMAGE);



            }
        }



//        updateImageUri(imageUri.toString());

    }


    private void imagePicked(Intent data) {
        Log.d(TAG, "imagePicked");
//            InCallAnalyticsData.getInstance().trackAnalyticsData(AnalyticsStatEvent.UIActionShare.SHARE_GALLERY);
        if(data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                if(selectedImage != null)
                    cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if(cursor != null)
                    cursor.moveToFirst();



                Log.d(TAG, "selectedImage " + selectedImage);

                croppedImageFile = new File(getActivity().getFilesDir(), "test_" + System.currentTimeMillis() + "_.jpg");

                croppedImage = Uri.fromFile(croppedImageFile);

                CropImageIntentBuilder cropImage = new CropImageIntentBuilder(200, 200, croppedImage);
                cropImage.setOutlineColor(0xFF03A9F4);
                cropImage.setSourceImage(selectedImage);

                Log.d(TAG, "cropping image");
                startActivityForResult(cropImage.getIntent(getActivity()), AppUtils.RESULT_CROP_IMAGE);


//                imageUri = selectedImage;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally {
                if(cursor != null)
                    cursor.close();
            }
        }
    }

    private void updateImageUri(String imageString){
        Log.d(TAG, "updateImageUri " + imageString);
        if (!imageString.equals("")){

            imageUri = Uri.parse(imageString);
            Log.d(TAG, "update the image " + imageUri.toString());
//            mileStoneImageView.setImageDrawable(null);
//            babyPicImageView.set
//            picasso.with(getActivity())
//                    .load(imageUri)
//                    .fit()
////                    .transform(new CircleTransform())
//                    .into(mileStoneImageView);
            mileStoneImageView.setImageURI(null);
            mileStoneImageView.setImageURI(imageUri);
        }
    }



}
