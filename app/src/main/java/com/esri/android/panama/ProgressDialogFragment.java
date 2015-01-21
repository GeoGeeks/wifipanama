package com.esri.android.panama;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;

/**
 * Implements a dialog that shows a progress indicator and a message. The dialog host (Activity or Fragment) needs to
 * implement OnCancelListener if it wants to be notified when the progress dialog is canceled.
 */
public class ProgressDialogFragment extends DialogFragment {
    private static final String KEY_PROGRESS_MESSAGE = "KEY_PROGRESS_MESSAGE";

    private OnCancelListener mOnCancelListener;

     /**
     * Creates a new instance of ProgressDialogFragment.
     *
     * @param message the progress message
     * @return an instance of ProgressDialogFragment
     */
    public static ProgressDialogFragment newInstance(String message) {
        ProgressDialogFragment dlg = new ProgressDialogFragment();

        Bundle args = new Bundle();
        args.putString(KEY_PROGRESS_MESSAGE, message);

        dlg.setArguments(args);
        return dlg;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String msg = args.getString(KEY_PROGRESS_MESSAGE);

        ProgressDialog progressDlg = new ProgressDialog(getActivity());
        progressDlg.setIndeterminate(true);
        progressDlg.setMessage(msg);
        progressDlg.setCanceledOnTouchOutside(false);

        return progressDlg;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // check if the host activity or fragment implements OnCancelListener
        //
        Fragment targetFragment = getTargetFragment();
        if (targetFragment instanceof OnCancelListener) {
            mOnCancelListener = (OnCancelListener) targetFragment;
        } else if (activity instanceof OnCancelListener) {
            mOnCancelListener = (OnCancelListener) activity;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        if (mOnCancelListener != null) {
            mOnCancelListener.onCancel(dialog);
        }
    }

    /**
     * Helper method to show a progress dialog with a message.
     *
     * @param activity
     * @param message
     * @param tag
     */
    public static void showDialog(Activity activity, String message, String tag) {
        showDialog(activity, message, tag, true);
    }

    /**
     * Helper method to show a progress dialog with a message.
     *
     * @param activity
     * @param message
     * @param tag
     * @param cancelable
     */
    public static void showDialog(Activity activity, String message, String tag, boolean cancelable) {
        ProgressDialogFragment progressDlg = new ProgressDialogFragment();

        Bundle args = new Bundle();
        args.putString(ProgressDialogFragment.KEY_PROGRESS_MESSAGE, message);
        progressDlg.setArguments(args);
        progressDlg.setCancelable(cancelable);

        progressDlg.show(activity.getFragmentManager(), tag);
    }

    /**
     * Helper method to hide a progress dialog.
     *
     * @param activity
     * @param tag
     */
    public static void hideDialog(Activity activity, String tag) {
        FragmentManager fM = activity.getFragmentManager();
        DialogFragment dlg = (DialogFragment) fM.findFragmentByTag(tag);
        if (dlg != null) {
            dlg.dismiss();
        }
    }
}
