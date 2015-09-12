package com.rorlig.babyapp.ui.fragment.milestones;

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
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.dagger.ForActivity;
import com.rorlig.babyapp.otto.events.growth.ItemCreatedOrChanged;
import com.rorlig.babyapp.otto.events.ui.FragmentCreated;
import com.rorlig.babyapp.parse_dao.Milestones;
import com.rorlig.babyapp.ui.PictureInterface;
import com.rorlig.babyapp.ui.fragment.BaseCreateLogFragment;
import com.rorlig.babyapp.ui.fragment.profile.PictureSourceSelectFragment;
import com.rorlig.babyapp.ui.widget.DateTimeHeaderFragment;
import com.rorlig.babyapp.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by gaurav
 * Milestone fragment
 */
public class MilestoneFragment extends BaseCreateLogFragment implements PictureInterface {

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

    @InjectView(R.id.add_image_button)
    android.widget.Button addImageButton;


    @Inject
    Picasso picasso;



    private String TAG = "MilestoneFragment";

    private String id;
    private boolean showEditDelete = false;
    private ArrayAdapter<CharSequence> milestoneAdapter;
    private boolean milestoneEmpty = true;
    private File croppedImageFile;
    private Uri croppedImage;
    private Uri imageUri;
    private PictureSourceSelectFragment pictureSourceSelectFragment;
    private boolean resetImage;
    private Milestones milestone;
    private String uuid;

    public MilestoneFragment() {
        super("Milestone");
    }


    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);


        scopedBus.post(new FragmentCreated("Milestone Fragment"));


        milestoneAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.milestones, android.R.layout.simple_list_item_1);
        customMilestonesTextView.setAdapter(milestoneAdapter);


        saveBtn.setEnabled(false);





        notes.setOnEditorActionListener(doneActionListener);

        //initialize views if not creating new feed item
        if (getArguments() != null) {
            Log.d(TAG, "arguments are not null");
            uuid = getArguments().getString("uuid");
            initViews(uuid);
        }
//        mileStoneImageView.setBackgroundColor(Color.CYAN);

        setUpTextWatchers();


    }

    private void initViews(String uuid) {

        Log.d(TAG, "initViews " + uuid);
        editDeleteBtn.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.GONE);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Milestone");
        query.fromLocalDatastore();
        query.whereEqualTo("uuid", uuid);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                milestone = (Milestones) objects.get(0);
                Log.d(TAG, milestone.toString());
                notes.setText(milestone.getNotes());
                dateTimeHeader.setDateTime(milestone.getLogCreationDate());
                customMilestonesTextView.setText(milestone.getTitle());
                if (milestone.getParseFile()!=null && milestone.getParseFile().getUrl()!=null){
                    picasso.with(context).load(milestone.getParseFile().getUrl()).into(mileStoneImageView);
                }
                imageUri = Uri.parse(milestone.getImagePath());
//                Log.d(TAG, "imagePath: " + milestone.getImagePath());
//                updateImageUri(milestone.getImagePath());
                showEditDelete = true;

                milestoneEmpty = false;


                setSaveEnabled();
            }
        });

//        query.getInBackground(id, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                milestone = (Milestones) object;
//                Log.d(TAG, milestone.toString());
//                notes.setText(milestone.getNotes());
//                dateTimeHeader.setDateTime(milestone.getLogCreationDate());
//                customMilestonesTextView.setText(milestone.getTitle());
//                if (milestone.getParseFile()!=null && milestone.getParseFile().getUrl()!=null){
//                    picasso.with(context).load(milestone.getParseFile().getUrl()).into(mileStoneImageView);
//                }
//                imageUri = Uri.parse(milestone.getImagePath());
////                Log.d(TAG, "imagePath: " + milestone.getImagePath());
////                updateImageUri(milestone.getImagePath());
//                showEditDelete = true;
//
//                milestoneEmpty = false;
//
//
//                setSaveEnabled();
//            }
//        });

//        try {
//            MilestonesDao milestonesDao = babyLoggerORMLiteHelper.getMilestonesDao().queryForId(id);
//            Log.d(TAG, milestonesDao.toString());
//            editDeleteBtn.setVisibility(View.VISIBLE);
//            saveBtn.setVisibility(View.GONE);
//
//            notes.setText(milestonesDao.getNotes());
//            dateTimeHeader.setDateTime(milestonesDao.getDate());
//            customMilestonesTextView.setText(milestonesDao.getTitle());
//
//            Log.d(TAG, "imagePath: " + milestonesDao.getImagePath());
//            updateImageUri(milestonesDao.getImagePath());
//            showEditDelete = true;
//
//            milestoneEmpty = false;
//
//
//
//            setSaveEnabled();
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }


    private EditText.OnEditorActionListener doneActionListener = new EditText.OnEditorActionListener() {

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



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateTimeHeader.setColor(DateTimeHeaderFragment.DateTimeColor.ORANGE);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_milestone, null);
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


    @OnClick(R.id.edit_btn)
    public void onEditBtnClicked() {
        Log.d(TAG, "edit btn clicked");
        createOrEdit();
    }

    /*
     */
    public void createOrEdit() {


        final Milestones tempMilestoneObject = createLocalObject();
        ParseFile file=null;
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Milestone");
        query.fromLocalDatastore();

        if (imageUri != null) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis;
            try {
                fis = new FileInputStream(new File(imageUri.getPath()));
                byte[] buf = new byte[1024];
                int n;
                while (-1 != (n = fis.read(buf)))
                    baos.write(buf, 0, n);

                byte[] bbytes = baos.toByteArray();

                file = new ParseFile(imageUri.getLastPathSegment(), bbytes);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (id != null) {
            if (file!=null) {
                final ParseFile finalFile = file;
                file.saveInBackground().onSuccess(new Continuation<Void, Object>() {
                    @Override
                    public Object then(Task<Void> task) throws Exception {
                        Log.d(TAG, "file saved successfully");
                        query.getInBackground(id).onSuccess(new Continuation<ParseObject, Object>() {
                            @Override
                            public Object then(Task<ParseObject> task) throws Exception {
                                Log.d(TAG, "found the parseobject successfully");
                                Milestones milestone = (Milestones) task.getResult();
                                milestone.setTitle(tempMilestoneObject.getTitle());
                                milestone.setImagePath(tempMilestoneObject.getImagePath());
                                milestone.setLogCreationDate(tempMilestoneObject.getLogCreationDate());
                                milestone.setNotes(tempMilestoneObject.getNotes());
                                milestone.setParseFile(finalFile);
                                saveEventually(milestone);
                                return null;
                            }
                        });
                        return null;
                    }
                });
            } else {
                query.getInBackground(id).onSuccess(new Continuation<ParseObject, Object>() {
                    @Override
                    public Object then(Task<ParseObject> task) throws Exception {
                        Log.d(TAG, "found the parseobject successfully");
                        Milestones milestone = (Milestones) task.getResult();
                        milestone.setTitle(tempMilestoneObject.getTitle());
                        milestone.setImagePath(tempMilestoneObject.getImagePath());
                        milestone.setLogCreationDate(tempMilestoneObject.getLogCreationDate());
                        milestone.setNotes(tempMilestoneObject.getNotes());
//                        milestone.setParseFile(finalFile);
                        saveEventually(milestone);
                        return null;
                    }
                });
            }
        } else {
            if (file != null) {
                final ParseFile finalFile1 = file;
                file.saveInBackground().onSuccess(new Continuation<Void, Object>() {
                    @Override
                    public Object then(Task<Void> task) throws Exception {
                        Log.d(TAG, "creating it");
                        tempMilestoneObject.setParseFile(finalFile1);
                        saveEventually(tempMilestoneObject);
                        return null;
                    }
                });

            } else {
                saveEventually(tempMilestoneObject);
            }

        }
        closeSoftKeyBoard();

    }

    private void saveEventually(final Milestones milestones) {
        milestones.pinInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d(TAG, "pinning new object");
                milestones.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d(TAG, "saving locally");
                        scopedBus.post(new ItemCreatedOrChanged("Milestone"));
                    }
                });
            }
        });
    }

    private Milestones createLocalObject() {
        Date date = dateTimeHeader.getEventTime();

        String imagePath = imageUri == null || imageUri.getPath() == null ? "" : imageUri.getPath();
//        String milestone = customMilestonesTextView.getText().toString();
        return new Milestones(customMilestonesTextView.getText().toString(),
                notes.getText().toString(),
                date, imagePath, null);
    }

    /*
     * deletes the feed item...
     */
    @OnClick(R.id.delete_btn)
    public void onDeleteBtnClicked() {
        delete(milestone);
    }

//    private Dao<MilestonesDao, Integer> createGrowthDao() throws SQLException {
//        return babyLoggerORMLiteHelper.getMilestonesDao();
//    }


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
        addImageButton.setText("Add Picture");
        mileStoneImageView.setImageURI(null);
        mileStoneImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_action_profile));
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
                    updateImageUri(croppedImage.toString(), true);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }


    @OnClick(R.id.milestone_pic)
    public void setBabyPicImageViewClicked() {
        addPicture();
    }

    @OnClick(R.id.add_image_button)
    public void addImageButtonClicked() {
        addPicture();
    }

    private void addPicture() {
        pictureSourceSelectFragment = new PictureSourceSelectFragment();
        Bundle args = new Bundle();
        args.putInt("color", R.color.orange_transparent);
        pictureSourceSelectFragment.setArguments(args);
        pictureSourceSelectFragment.setTargetFragment(this, 1);
        pictureSourceSelectFragment.show(getActivity().getSupportFragmentManager(), "picture_select_fragment");
    }

    private void cameraImageCaptured(Intent data) {

        Log.d(TAG, "cameraImageCaptured : " + data.getData());

        Uri returnedUri;

        if (data != null) {
            returnedUri = data.getData();

            if (returnedUri != null) {

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
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                if (selectedImage != null)
                    cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null)
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
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
    }

    private void updateImageUri(String imageString) {
        Log.d(TAG, "updateImageUri " + imageString);
        if (!imageString.equals("")) {

            picasso.load(imageString).into(mileStoneImageView);
//            imageUri = Uri.parse(imageString);
//            Log.d(TAG, "update the image " + imageUri.toString());
////            babyPicImageView.set
////            picasso.load(imageUri)
////                    .fit()
////                    .transform(new CircleTransform())
////                            .into(babyPicImageView);
//            babyPicImageView.setImageURI(null);
//            babyPicImageView.setImageURI(imageUri);
            addImageButton.setText("Change Picture");
//            imageUri = Uri.parse(imageString);
//            Log.d(TAG, "update the image " + imageUri.toString());
////            mileStoneImageView.setColorFilter(Color.CYAN);
//            mileStoneImageView.setImageURI(null);
//            mileStoneImageView.setImageURI(imageUri);
            addImageButton.setText("Change Picture");
        }
    }

    private void updateImageUri(String imageString, boolean storeValue){
        if (storeValue) {
            imageUri = Uri.parse(imageString);
        }
        resetImage = false;

        updateImageUri(imageString);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


}
