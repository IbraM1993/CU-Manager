package fl.futuerx.com.mutmanager.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fl.futuerx.com.mutmanager.Asynctask.LoginTask;
import fl.futuerx.com.mutmanager.Helpers.DataManager;
import fl.futuerx.com.mutmanager.Helpers.UIHelper;
import fl.futuerx.com.mutmanager.Interfaces.AsyncCallBack;
import fl.futuerx.com.mutmanager.Interfaces.DelegateCall;
import fl.futuerx.com.mutmanager.Models.Instructor;
import fl.futuerx.com.mutmanager.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView      = (AutoCompleteTextView) findViewById(R.id.user_name);
        mPasswordView   = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                attemptLogin();
                return true;
            }
        });

        Button signInBtn = (Button) findViewById(R.id.login_sign_in_btn);
        signInBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        Instructor instructor = DataManager.getInstance().getCurrent(this);
        if(instructor != null){
            UIHelper.getInstance().ShowMessagePopUp(LoginActivity.this
                    , getString(R.string.action_login)
                    , getString(R.string.sign_up_syncing_completed)
                    , new DelegateCall<String>() {
                        @Override
                        public String Invoke(String returnedObject) {
                            startActivity(new Intent(LoginActivity.this, ClassesActivity.class));
                            finish();
                            return null;
                        }
                    });
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            new LoginTask(LoginActivity.this, mEmailView.getText().toString(), mPasswordView.getText().toString(), new AsyncCallBack<Instructor, String>() {
                @Override
                public void OnStart() {
                    showProgress(true);
                    UIHelper.getInstance().ShowProgressPopUp(LoginActivity.this, getString(R.string.action_signing_in), getString(R.string.sign_up_syncing));
                }

                @Override
                public void OnCompleted(Instructor result) {
                    showProgress(false);
                    if(result != null && result.name != null){
                        DataManager.getInstance().setInstructor(LoginActivity.this, result);
                        UIHelper.getInstance().ShowMessagePopUp(LoginActivity.this
                                , getString(R.string.action_login)
                                , getString(R.string.sign_up_syncing_completed)
                                , new DelegateCall<String>() {
                                    @Override
                                    public String Invoke(String returnedObject) {
                                        startActivity(new Intent(LoginActivity.this, ClassesActivity.class));
                                        finish();
                                        return null;
                                    }
                                });
                    } else {
                        OnFailed(getString(R.string.sign_in_invalid_credential));
                    }

                }

                @Override
                public void OnFailed(String error) {
                    showProgress(false);
                    UIHelper.getInstance().ShowErrorPopUp(LoginActivity.this, getString(R.string.error_sign_iin), error, null);
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
