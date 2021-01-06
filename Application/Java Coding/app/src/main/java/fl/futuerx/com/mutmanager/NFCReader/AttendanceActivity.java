/*
 * Copyright (C) 2010 The Android Open Source Project
 * Copyright (C) 2011 Adam Nybäck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fl.futuerx.com.mutmanager.NFCReader;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fl.futuerx.com.mutmanager.Adapter.AttendanceAdapter;
import fl.futuerx.com.mutmanager.Asynctask.ExportCSVTask;
import fl.futuerx.com.mutmanager.Asynctask.GetAttendanceTask;
import fl.futuerx.com.mutmanager.Asynctask.StartClassesTask;
import fl.futuerx.com.mutmanager.Asynctask.UpdateAttendanceTask;
import fl.futuerx.com.mutmanager.BackEnd.MUT_ATTENDANCE_API;
import fl.futuerx.com.mutmanager.BackEnd.apiResponseObject;
import fl.futuerx.com.mutmanager.Helpers.Formatter;
import fl.futuerx.com.mutmanager.Helpers.UIHelper;
import fl.futuerx.com.mutmanager.Interfaces.AsyncCallBack;
import fl.futuerx.com.mutmanager.Interfaces.DelegateCall;
import fl.futuerx.com.mutmanager.Models.Attendance;
import fl.futuerx.com.mutmanager.R;

/**
 * An {@link Activity} which handles a broadcast of a new tag that the device just discovered.
 */
public class AttendanceActivity extends AppCompatActivity {

    //    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
//    private LinearLayout mTagContent;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
    private AlertDialog mDialog;
    private TextView currentTime;
    private Runnable periodHandler;
//    private List<Tag>       mTags = new ArrayList<>();

    public static final String CLASS_ID = "CLASS_ID";
    public static final String CLASS_START_TIME = "CLASS_START_TIME";
    public static final String CLASS_END_TIME = "CLASS_END_TIME";
    public static final String CLASS_CODE = "CLASS_CODE";
    public static final String CLASS_DAY_OF_WEEK = "CLASS_DAY_OF_WEEK";
    public static final String CLASS_DAY_OF_WEEK_SELECTED = "CLASS_DAY_OF_WEEK_SELECTED";
    private int classId;
    private ArrayList<Integer> classDaysOfWeek;
    private String classTimeStart;
    private String classTimeEnd;
    private String classCode;
    private String classDate;
    private int currentAttended = 0;
    private int selectedDay = 0;
    private int totalStudent = 0;

    private AttendanceAdapter adapter;

    private int REQUEST_PERMISSION = 10;
//    private List<Student>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tag_reader);
        setContentView(R.layout.activity_attendance);
//        mTagContent = (LinearLayout) findViewById(R.id.list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        resolveIntent(getIntent());
        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            finish();
            return;
        }
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[]{newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true)});
        currentTime = findViewById(R.id.attendance_current_time);


        periodHandler = new Runnable() {
            private boolean firstRun = true;

            @Override
            public void run() {
                if (!AttendanceActivity.this.isDestroyed()) {
                    try {
                        if (currentTime != null) {
                            String currentTimeS = Formatter.getInstance().formatTime(new Date());
                            currentTime.setText(currentTimeS);
                            if (currentTimeS.equals(classTimeEnd)) {
                                UIHelper.getInstance().ShowErrorPopUp(AttendanceActivity.this
                                        , "Class Ended"
                                        , "Class time has run out with " + currentAttended + " Attended out of " + totalStudent
                                        , null);
                            }
                            currentTime.setTextColor(getResources().getColor(currentTimeS.compareTo(classTimeEnd) < 0 ? R.color.colorPrimary : R.color.popup_error));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("Attendance", "is destroyed : " + AttendanceActivity.this.isDestroyed());

                    if (firstRun) {

                        new Handler().postDelayed(periodHandler, (60 - Calendar.getInstance().get(Calendar.SECOND)) * 1000);
                        firstRun = false;
                    } else {

                        new Handler().postDelayed(periodHandler, 60 * 1000);
                    }
                } else {
                    currentTime = null;
                }
            }
        };
        readExtra();
        periodHandler.run();

    }

    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
            mAdapter.disableForegroundNdefPush(this);
        }
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
//                mTags.add(tag);
                HandleTag(tag);
            }
            // Setup the views
//            buildTagViews(msgs);
        }
    }

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";
                try {
                    MifareClassic mifareTag;
                    try {
                        mifareTag = MifareClassic.get(tag);
                    } catch (Exception e) {
                        // Fix for Sony Xperia Z3/Z5 phones
                        tag = cleanupTag(tag);
                        mifareTag = MifareClassic.get(tag);
                    }
                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    private Tag cleanupTag(Tag oTag) {
        if (oTag == null)
            return null;

        String[] sTechList = oTag.getTechList();

        Parcel oParcel = Parcel.obtain();
        oTag.writeToParcel(oParcel, 0);
        oParcel.setDataPosition(0);

        int len = oParcel.readInt();
        byte[] id = null;
        if (len >= 0) {
            id = new byte[len];
            oParcel.readByteArray(id);
        }
        int[] oTechList = new int[oParcel.readInt()];
        oParcel.readIntArray(oTechList);
        Bundle[] oTechExtras = oParcel.createTypedArray(Bundle.CREATOR);
        int serviceHandle = oParcel.readInt();
        int isMock = oParcel.readInt();
        IBinder tagService;
        if (isMock == 0) {
            tagService = oParcel.readStrongBinder();
        } else {
            tagService = null;
        }
        oParcel.recycle();

        int nfca_idx = -1;
        int mc_idx = -1;
        short oSak = 0;
        short nSak = 0;

        for (int idx = 0; idx < sTechList.length; idx++) {
            if (sTechList[idx].equals(NfcA.class.getName())) {
                if (nfca_idx == -1) {
                    nfca_idx = idx;
                    if (oTechExtras[idx] != null && oTechExtras[idx].containsKey("sak")) {
                        oSak = oTechExtras[idx].getShort("sak");
                        nSak = oSak;
                    }
                } else {
                    if (oTechExtras[idx] != null && oTechExtras[idx].containsKey("sak")) {
                        nSak = (short) (nSak | oTechExtras[idx].getShort("sak"));
                    }
                }
            } else if (sTechList[idx].equals(MifareClassic.class.getName())) {
                mc_idx = idx;
            }
        }

        boolean modified = false;

        if (oSak != nSak) {
            oTechExtras[nfca_idx].putShort("sak", nSak);
            modified = true;
        }

        if (nfca_idx != -1 && mc_idx != -1 && oTechExtras[mc_idx] == null) {
            oTechExtras[mc_idx] = oTechExtras[nfca_idx];
            modified = true;
        }

        if (!modified) {
            return oTag;
        }

        Parcel nParcel = Parcel.obtain();
        nParcel.writeInt(id.length);
        nParcel.writeByteArray(id);
        nParcel.writeInt(oTechList.length);
        nParcel.writeIntArray(oTechList);
        nParcel.writeTypedArray(oTechExtras, 0);
        nParcel.writeInt(serviceHandle);
        nParcel.writeInt(isMock);
        if (isMock == 0) {
            nParcel.writeStrongBinder(tagService);
        }
        nParcel.setDataPosition(0);

        Tag nTag = Tag.CREATOR.createFromParcel(nParcel);

        nParcel.recycle();

        return nTag;
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
//            if (i > 0) {
//                sb.append(" ");
//            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
//            if (i > 0) {
//                sb.append(" ");
//            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_export:
                exportData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void exportData() {

        if(PackageManager.PERMISSION_GRANTED == PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new ExportCSVTask( adapter.getAttendanceList(), classCode, classDate, new AsyncCallBack<String, String>() {
                @Override
                public void OnStart() {
                    UIHelper.getInstance().ShowProgressPopUp(AttendanceActivity.this, "Exporting Data", "Process Data");
                }

                @Override
                public void OnCompleted(final String result) {

                    UIHelper.getInstance().ShowMessagePopUp(AttendanceActivity.this
                            , "Exporting Data"
                            , "Process completed"
                            , new DelegateCall<String>() {
                                @Override
                                public String Invoke(String returnedObject) {
                                    ShareViaEmail(result);
                                    return null;
                                }
                            });
                }

                @Override
                public void OnFailed(String error) {

                    UIHelper.getInstance().ShowErrorPopUp(AttendanceActivity.this
                            , "Exporting Data Failed"
                            , "Error Occured while Exporting data : "+error
                            , null);
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }
//        if(ActivityCompat.checkSelfPermission(this, PermissionChecker.))

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        exportData();
    }

    private void ShareViaEmail(String file) {
        try {

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            String message = "Export " + classCode + " Attendance.";
            intent.putExtra(Intent.EXTRA_SUBJECT, "Export " + classCode);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse( "file://" + file));
            intent.putExtra(Intent.EXTRA_TEXT, message);
//            intent.setData(Uri.parse("mailto:xyz@gmail.com"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        } catch(Exception e)  {
            System.out.println("is exception raises during sending mail"+e);
        }
    }

/*
    void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
//        LinearLayout content = mTagContent;

        // Parse the first message in the list
        // Build views for all of the sub records
        Date now = new Date();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        for (int i = 0; i < size; i++) {
            TextView timeView = new TextView(this);
            timeView.setText(TIME_FORMAT.format(now));
//            content.addView(timeView, 0);
            ParsedNdefRecord record = records.get(i);
//            content.addView(record.getView(this, inflater, content, i), 1 + i);
//            content.addView(inflater.inflate(R.layout.tag_divider, content, false), 2 + i);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if (mTags.size() == 0) {
//            Toast.makeText(this, R.string.nothing_scanned, Toast.LENGTH_LONG).show();
//            return true;
//        }

        switch (item.getItemId()) {
        case R.id.menu_main_clear:
            clearTags();
            return true;
        case R.id.menu_copy_hex:
            copyIds(getIdsHex());
            return true;
        case R.id.menu_copy_reversed_hex:
            copyIds(getIdsReversedHex());
            return true;
        case R.id.menu_copy_dec:
            copyIds(getIdsDec());
            return true;
        case R.id.menu_copy_reversed_dec:
            copyIds(getIdsReversedDec());
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void clearTags() {
        mTags.clear();
//        for (int i = mTagContent.getChildCount() -1; i >= 0 ; i--) {
//            View view = mTagContent.getChildAt(i);
//            if (view.getId() != R.id.tag_viewer_text) {
//                mTagContent.removeViewAt(i);
//            }
//        }
    }

    private void copyIds(String text) {
        Log.d("Tag NFC", text);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("NFC IDs", text);
        clipboard.setPrimaryClip(clipData);
        Toast.makeText(this, mTags.size() + " IDs copied", Toast.LENGTH_SHORT).show();
    }

    private String getIdsHex() {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : mTags) {
            builder.append(toHex(tag.getId()));
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1); // Remove last new line
        return builder.toString().replace(" ", "");
    }

    private String getIdsReversedHex() {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : mTags) {
            builder.append(toReversedHex(tag.getId()));
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1); // Remove last new line
        return builder.toString().replace(" ", "");
    }

    private String getIdsDec() {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : mTags) {
            builder.append(toDec(tag.getId()));
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1); // Remove last new line

        return builder.toString();
    }

    private String getIdsReversedDec() {
        StringBuilder builder = new StringBuilder();
        for (Tag tag : mTags) {
            builder.append(toReversedDec(tag.getId()));
            builder.append('\n');
        }
        builder.setLength(builder.length() - 1); // Remove last new line
        return builder.toString();
    }*/

    @Override
    public void onNewIntent(Intent intent) {
        if(isOnCall){
            return;
        }
        if(Formatter.getInstance().isNow(classTimeStart, classTimeEnd, classDaysOfWeek, selectedDay)){
            setIntent(intent);
            resolveIntent(intent);
        } else {
            UIHelper.getInstance().ShowErrorPopUp(AttendanceActivity.this, "Unable To take attendance", "", null);
        }
    }

    private void readExtra() {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            classId = extra.getInt(CLASS_ID, -1);
            classTimeStart = extra.getString(CLASS_START_TIME, null);
            classTimeEnd = extra.getString(CLASS_END_TIME, null);
            classCode = extra.getString(CLASS_CODE, null);
            classDaysOfWeek= extra.getIntegerArrayList(CLASS_DAY_OF_WEEK);
            selectedDay = extra.getInt(CLASS_DAY_OF_WEEK_SELECTED, -1);
        }
        if (classId == -1) {
            UIHelper.getInstance().ShowErrorPopUp(this, "No Class Id", "Please make sure to pass class id.", new DelegateCall<String>() {
                @Override
                public String Invoke(String returnedObject) {
                    finish();
                    return null;
                }
            });
        }

        fetchData();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, selectedDay);
        c.getTimeInMillis();

        classDate = Formatter.getInstance().formatDate(c.getTime());
        ((TextView) findViewById(R.id.attendance_class_time)).setText(Formatter.getInstance().formatDate(c.getTime())
                + "  " +classTimeStart + " - " + classTimeEnd);
        ((TextView) findViewById(R.id.attendance_class_code)).setText(classCode);
    }

    private void fetchData() {
        if(!Formatter.getInstance().isNow(classTimeStart, classTimeEnd, classDaysOfWeek, selectedDay) ) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_WEEK, selectedDay);
            c.getTimeInMillis();
            new GetAttendanceTask(AttendanceActivity.this, classId, Formatter.getInstance().formatDate(c.getTime()),new AsyncCallBack<List<Attendance>, String>() {
                @Override
                public void OnStart() {
                    showProgress(true);
                }

                @Override
                public void OnCompleted(List<Attendance> result) {
                    showProgress(false);
                    if(result != null && result.size() == 0) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.DAY_OF_WEEK, selectedDay);
                        c.getTimeInMillis();
                        new StartClassesTask(AttendanceActivity.this, classId
                                , Formatter.getInstance().formatDate(c.getTime())
                                , new AsyncCallBack<List<Attendance>, String>() {

                            @Override
                            public void OnStart() {
                                showProgress(true);
                            }

                            @Override
                            public void OnCompleted(List<Attendance> result) {
                                showProgress(false);

                                publishData(result);

                            }

                            @Override
                            public void OnFailed(String error) {
                                showProgress(false);

                            }
                        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        publishData(result);
                    }

                }

                @Override
                public void OnFailed(String error) {
                    showProgress(false);

                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new StartClassesTask(this, classId, Formatter.getInstance().formatDate(new Date()), new AsyncCallBack<List<Attendance>, String>() {
            @Override
            public void OnStart() {
                showProgress(true);
            }

            @Override
            public void OnCompleted(List<Attendance> result) {
                showProgress(false);

                publishData(result);

            }

            @Override
            public void OnFailed(String error) {
                showProgress(false);

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void publishData(List<Attendance> result) {

        RecyclerView recycler = findViewById(R.id.recycler);
        adapter = new AttendanceAdapter(this, result, new DelegateCall<Attendance>() {
            @Override
            public Attendance Invoke(final Attendance returnedObject) {
                UIHelper.getInstance().ShowDialog(AttendanceActivity.this
                        , returnedObject.student_name + " is " + (returnedObject.status == 1 ? "Attending " : " Absent")
                        , " Toggle " + returnedObject.student_name + " ? "
                        , returnedObject.status == 1 ? R.color.colorPrimary : R.color.colorAccent
                        , "Yes"
                        , "Cancel"
                        , MUT_ATTENDANCE_API.MUT_ATTENDANCE_SERVER_PRODUCTION+returnedObject.student_icon
                        , new DelegateCall<Integer>() {
                            @Override
                            public Integer Invoke(Integer r) {
                                new UpdateAttendanceTask(AttendanceActivity.this, returnedObject.id, returnedObject.status == 1 ? 0 : 1, new AsyncCallBack<apiResponseObject, String>() {
                                    @Override
                                    public void OnStart() {
                                        showProgress(true);
                                    }

                                    @Override
                                    public void OnCompleted(apiResponseObject result) {
                                        showProgress(false);
                                        returnedObject.status = returnedObject.status == 1 ? 0 : 1;
                                        adapter.updateItem(returnedObject);
                                        currentAttended++;
                                        updateHeader();
                                        UIHelper.getInstance().ShowMessagePopUp(AttendanceActivity.this, "Attendance Updated", returnedObject.student_name + " has registered successfully!", null);
                                    }

                                    @Override
                                    public void OnFailed(String error) {
                                        showProgress(false);
                                        UIHelper.getInstance().ShowErrorPopUp(AttendanceActivity.this, "Student Not Found", "Student tag not found in current class", null);
                                    }
                                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                return null;
                            }
                        }
                        , null
                        , null);
                return null;
            }
        });
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setAdapter(adapter);
        totalStudent = result.size();
        for (Attendance a : result) {
            if (a.status == 1) {
                currentAttended++;
            }
        }
        updateHeader();
    }

    private void showProgress(boolean b) {
        findViewById(R.id.activity_progress).setVisibility(b ? View.VISIBLE : View.GONE);
        isOnCall = b;
    }

    private boolean isOnCall = false;
    private void HandleTag(Tag record) {
//        Log.d("TAG", "Tag Read toReversedHex: " + toReversedHex(record.getId()));
//        Log.d("TAG", "Tag Read toHex: " + toHex(record.getId()));
        final Attendance a = adapter.getItemByTag(toHex(record.getId()));
        if (a == null) {
            UIHelper.getInstance().ShowErrorPopUp(this, "Student Not Found", "Student tag not found in current class", null);
        } else {
            if (a.status == 1) {
                UIHelper.getInstance().ShowErrorPopUp(this, a.student_name + " already in class!", "Student is already in class!", null);
            } else {
//                UIHelper.getInstance().ShowMessagePopUp(this, "Student Found", a.student_name + " has registered successfully!", null);
                UIHelper.getInstance().ShowDialog(this, "Student Found"
                        , "Do you want to set " + a.student_name + " as Attended ?"
                        , -1
                        , "yes"
                        , "No"
                        , MUT_ATTENDANCE_API.MUT_ATTENDANCE_SERVER_PRODUCTION + a.student_icon
                        , new DelegateCall<Integer>() {
                            @Override
                            public Integer Invoke(Integer returnedObject) {

                                new UpdateAttendanceTask(AttendanceActivity.this, a.id, 1, new AsyncCallBack<apiResponseObject, String>() {
                                    @Override
                                    public void OnStart() {
                                        showProgress(true);
                                    }

                                    @Override
                                    public void OnCompleted(apiResponseObject result) {
                                        showProgress(false);
                                        a.status = 1;
                                        adapter.updateItem(a);
                                        currentAttended++;
                                        updateHeader();
                                        UIHelper.getInstance().ShowMessagePopUp(AttendanceActivity.this, "Attendance Updated", a.student_name + " has registered successfully!", null);
                                    }

                                    @Override
                                    public void OnFailed(String error) {
                                        showProgress(false);
                                        UIHelper.getInstance().ShowErrorPopUp(AttendanceActivity.this, "Student Not Found", "Student tag not found in current class", null);
                                    }
                                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                return null;
                            }
                        }
                        , new DelegateCall<Integer>() {
                            @Override
                            public Integer Invoke(Integer returnedObject) {

                                return null;
                            }
                        }
                        , null);
            }
        }
    }

    private void updateHeader() {

        ((TextView) findViewById(R.id.attendance_class_attendance)).setText(" Class Attendance : " + currentAttended + " out of " + totalStudent);
        adapter.notifyDataSetChanged();
    }
}