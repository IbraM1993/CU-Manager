package fl.futuerx.com.mutmanager.Asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.util.List;
import java.util.Locale;

import fl.futuerx.com.mutmanager.Helpers.IOUtils;
import fl.futuerx.com.mutmanager.Interfaces.AsyncCallBack;
import fl.futuerx.com.mutmanager.Models.Attendance;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by aladd on 25/2/2018.
 */
//new async task for file export to csv
public class ExportCSVTask extends AsyncTask<Void, Void, Void> {
    private AsyncCallBack<String, String> callBack;
    private String exportedFile;
    private String error;
    private List<Attendance> data;
    private String classCode;
    private String classDate;

    public ExportCSVTask(List<Attendance> data, String classCode, String classDate, AsyncCallBack<String, String> callBack) {
        this.callBack = callBack;
        this.data = data;
        this.classCode = classCode;
        this.classDate = classDate;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            String exportFolder = Environment.getExternalStorageDirectory().getAbsolutePath() +  IOUtils.getInstance().EXPORT_FOLDER;
            IOUtils.getInstance().CheckIfDirectoryExist(exportFolder);
            File exportFile = IOUtils.getInstance().createFile(new File(exportFolder), classDate+".xls", classCode);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(exportFile, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Attendance", 0);

            sheet.addCell(new Label(0, 0, "id")); // column and row
            sheet.addCell(new Label(1, 0, "Student id"));
            sheet.addCell(new Label(2, 0, "Student Name"));
            sheet.addCell(new Label(3, 0, "Date"));
            sheet.addCell(new Label(4, 0, "Status"));

            if (data.size() > 0) {
                int i = 1;
                for(Attendance current : data){
                    sheet.addCell(new Label(0, i, i+""));
                    sheet.addCell(new Label(1, i, current.student_id+""));
                    sheet.addCell(new Label(2, i, current.student_name));
                    sheet.addCell(new Label(3, i, classDate));
                    sheet.addCell(new Label(4, i, current.status+""));
                    i++;
                }
            }
            workbook.write();
            workbook.close();
            error = null;
            exportedFile = exportFile.getAbsolutePath();
        } catch (Exception ex){
            ex.printStackTrace();
            error = ex.getMessage();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(callBack != null){
            if(exportedFile != null){
                callBack.OnCompleted(exportedFile);
            } else {
                callBack.OnFailed(error);
            }

        }
    }
}