package fl.futuerx.com.mutmanager.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.text.TextUtils;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.marcoscg.dialogsheet.DialogSheet;
import com.nineoldandroids.animation.Animator;

import fl.futuerx.com.mutmanager.Interfaces.DelegateCall;
import fl.futuerx.com.mutmanager.R;


public class UIHelper {
    private static final UIHelper ourInstance = new UIHelper();
    private static final long DISMISS_DELAY = 3000;
    public static final long UI_SETUP_DELAY = 500;

    private final long ANIMATOR_ACTION_DURATION = 500;
    private final long ANIMATOR_ERROR_DURATION  = 500;
    public static UIHelper getInstance() {
        return ourInstance;
    }

    private UIHelper() {
    }


    public void ShowErrorPopUp(Context context, String title, String message
            , final DelegateCall<String> onDismissCall){
//        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
//        View sbView = snackbar.getView();
//        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.popup_error));
//        snackbar.show();

        ShowDialog(context, title, message,  R.color.popup_error, null,null, null, null, null, onDismissCall);
    }

    public void ShowProgressPopUp(Context context, String title, String message) {
//        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
//        View sbView = snackbar.getView();
//        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.popup_progress));
//        snackbar.show();

        ShowDialog(context, title, message,  R.color.popup_progress, null,null, null,null, null, null);
    }

    public void ShowMessagePopUp(final Context context, String title, String message, final DelegateCall<String> callback) {
//        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
//            @Override
//            public void onDismissed(Snackbar transientBottomBar, int event) {
//                super.onDismissed(transientBottomBar, event);
//                if(callback != null)
//                    callback.Invoke(null);
//            }
//        });
//        View sbView = snackbar.getView();
//        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.popup_message));
//        snackbar.show();
        ShowDialog(context, title, message, R.color.popup_message,null, null, null, null, null, callback);

    }

    public void ShowDialog (Context context, String title, String message, @ColorRes int colorRes
                            , final String positiveLabel
                            , final String negativeLabel
                            , final String imageUrl
            , final DelegateCall<Integer> onPositiveCall
            , final DelegateCall<Integer> onNegativeCall
            , final DelegateCall<String> onDismissCall
    ) {
        final DialogSheet d = new DialogSheet(context)
                .setTitle(title)
                .setMessage(message)
                .setImageHolder(imageUrl)
                .setButtonsColorRes(colorRes)
                .setTitleColorRes(colorRes);

        if(onPositiveCall != null){
            d.setPositiveButton(TextUtils.isEmpty(positiveLabel) ? context.getString(android.R.string.ok) : positiveLabel, new DialogSheet.OnPositiveClickListener() {
                @Override
                public void onClick(View v) {
                    // Your action
                    onPositiveCall.Invoke(1);
                }
            });
        }
        if(onNegativeCall != null){
            d.setNegativeButton(TextUtils.isEmpty(negativeLabel) ? context.getString(android.R.string.cancel) : negativeLabel, new DialogSheet.OnNegativeClickListener() {
                @Override
                public void onClick(View v) {
                    // Your action
                    onNegativeCall.Invoke(1);
                }
            });
        }
        if(onDismissCall != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(d != null){
                        d.dismiss();
                    }
                    onDismissCall.Invoke("");
                }
            }, DISMISS_DELAY);
        }
        d.show();
    }

    public Bitmap bitmapSizeByScall(Bitmap bitmapIn, float scale_zero_to_one_f) {

        Bitmap bitmapOut = Bitmap.createScaledBitmap(bitmapIn,
                Math.round(bitmapIn.getWidth() * scale_zero_to_one_f),
                Math.round(bitmapIn.getHeight() * scale_zero_to_one_f), false);

        return bitmapOut;
    }

    public int ParseColorFromString(String colorS) {

        if(TextUtils.isEmpty(colorS)){
            return 0;
        }
        return Color.parseColor(colorS);
    }


    public void ClickAnimator(View view, final DelegateCall<Void> onClick){

        YoYo.with(Techniques.FlipInX)
                .duration(ANIMATOR_ACTION_DURATION)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(onClick != null){
                            onClick.Invoke(null);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(view);
    }

    public void ErrorAnimator(View view, final DelegateCall<Void> onClick){

        YoYo.with(Techniques.Wobble)
                .duration(ANIMATOR_ERROR_DURATION)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(onClick != null){
                            onClick.Invoke(null);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(view);
    }

}
