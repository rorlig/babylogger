package com.rorlig.babyapp.ui.fragment.auth;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.ProgressDialog;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.rorlig.babyapp.R;
import com.rorlig.babyapp.otto.auth.ForgotBtnClickedEvent;
import com.rorlig.babyapp.otto.auth.LoginSkippedEvent;
import com.rorlig.babyapp.otto.auth.LoginSuccessEvent;
import com.rorlig.babyapp.otto.auth.SignupBtnClickedEvent;
import com.rorlig.babyapp.ui.activity.LoginActivity;
import com.rorlig.babyapp.ui.fragment.InjectableFragment;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
//import it.sephiroth.android.library.tooltip.TooltipManager;

/**
 * @author gaurav gupta
 */
public class LoginFragment extends InjectableFragment {
    @InjectView(R.id.btn_forgot_password)
    Button forgotPasswordBtn;

    @InjectView(R.id.btn_signup)
    Button signupBtn;
    private String TAG = "LoginFragment";


    @InjectView(R.id.skip_message)
    TextView skipMessageTextView;

    @InjectView(R.id.email)
    TextView emailTextView;

    @InjectView(R.id.email_text_input_layout)
    TextInputLayout emailTextInputLayout;

    @InjectView(R.id.password_text_input_layout)
    TextInputLayout passwordTextInputLayout;

    @InjectView(R.id.password)
    TextView passwordTextView;

    @InjectView(R.id.txt_why_signup)
    TextView signupTextView;

    private ProgressDialog dialog;

    private String termAndConditions = "terms and conditions";

    private String privacy = "privacy policy";

    Pattern pattern2 = Pattern.compile("[a-zA-Z]+");


    Linkify.MatchFilter termsAndConditionMatchFilter = new Linkify.MatchFilter() {
        @Override
        public boolean acceptMatch(CharSequence cs, int start, int end) {
            int startIndex = getString(R.string.skip_message).indexOf(termAndConditions);
            int endIndex = startIndex + termAndConditions.length();
            return start>=startIndex&&end<=endIndex;
        }
    };

    Linkify.MatchFilter privacyMatchFilter = new Linkify.MatchFilter() {
        @Override
        public boolean acceptMatch(CharSequence cs, int start, int end) {
            int startIndex = getString(R.string.skip_message).indexOf(privacy);
            int endIndex = startIndex + privacy.length();
            return start>=startIndex&&end<=endIndex;
        }
    };


    Linkify.TransformFilter blankTransform = new Linkify.TransformFilter() {
        @Override
        public String transformUrl(Matcher match, String url) {
            return ""; //remove the $ sign
        }
    };
//    TooltipManager manager = TooltipManager.getInstance();

    @Override
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
//        manager.create(getActivity(), 0)
//                .anchor(signupTextView, TooltipManager.Gravity.RIGHT)
//                .closePolicy(TooltipManager.ClosePolicy.TouchOutside, 3000)
//                .text(R.string.hello_world)
//                .toggleArrow(true)
//                .maxWidth(400)
//                .showDelay(300)
//                .withCallback(this)
//                .show();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailTextInputLayout.setErrorEnabled(true);
        passwordTextInputLayout.setErrorEnabled(true);

        Linkify.addLinks(skipMessageTextView, pattern2, getString(R.string.toc_link), termsAndConditionMatchFilter, blankTransform);
        Linkify.addLinks(skipMessageTextView, pattern2, getString(R.string.privacy_link), privacyMatchFilter, blankTransform);

//        skipMessageTextView.setText(AppUtils.getSpannable(skipMessageTextView.getText().toString()));
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.btn_forgot_password)
    public void forgotBtnClicked(){
        Log.d(TAG, "forgotBtnClicked");
        scopedBus.post(new ForgotBtnClickedEvent());
    }

    @OnClick(R.id.btn_signup)
    public void btnSignUpClicked(){
        Log.d(TAG, "btnSignUpClicked");

        scopedBus.post(new SignupBtnClickedEvent());


    }

    @OnClick(R.id.btn_login)
    public void btnLoginClicked(){
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        if (email.length() == 0) {
            emailTextInputLayout.setError(getString(R.string.no_email));
            return;
        }
        if (!isValidEmail(email)){
            emailTextInputLayout.setError(getString(R.string.not_valid_email));
            return;
        }

        if (password.length()==0) {
            passwordTextInputLayout.setError(getString(R.string.no_password));
            return;
        }


        // Set up a progress dialog
//        dialog = new ProgressDialog(getActivity(), getString(R.string.title_login));
////        dialog.se(getString(R.string.progress_login));
//        dialog.show();


        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
//                dialog.dismiss();
                if (e != null) {
                    // Show the error message
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
                    scopedBus.post(new LoginSuccessEvent());
//                    Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                }
            }
        });
    }

//    @OnClick(R.id.btn_skip)
//    public void btnSkipClicked(){
//        scopedBus.post(new LoginSkippedEvent());
//
//    }

    protected void showToast(int id) {
        Toast.makeText(getActivity(), getString(id), Toast.LENGTH_SHORT).show();

    }



}
