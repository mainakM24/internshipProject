package com.example.myapplication;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;


public class CheckReportFragment extends Fragment {


    CardView cardView;
    Bitmap bitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_report, container, false);
        cardView = (CardView) view.findViewById(R.id.cardView);
        Toast.makeText(getActivity(), "Fetching data", Toast.LENGTH_SHORT).show();




////////////////////////////////////////////////////////////////////////////////////////////////////

        DataService dataService = new DataService(getActivity());
        dataService.getHeath(new DataService.ResponseListner() {
            @Override
            public void onResponse(HashMap<Integer, String>[] Users) {
                for (int i = 2; i <= 10; i += 2 ){
                    TableLayout tableLayout = view.findViewById(R.id.tableLayout);
                    TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    for (int j = 1; j<= 5 ; j++){
                        TextView textView = (TextView) tableRow.getChildAt(j);
                        String data = Users[(i/2)-1].get(j);
                        textView.setText(data);
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });

////////////////////////////////////////////////////////////////////////////////////////////////////


        dataService.getBlood(new DataService.Listner() {
            @Override
            public void onResponse(int[] total) {
                String[] label = {"A+","B+","AB+","O+","A−","B−","AB−","O−"};
                int[] color = {Color.BLUE, Color.GREEN, Color.MAGENTA, Color.GRAY, Color.YELLOW, Color.CYAN, Color.RED, Color.LTGRAY};
                PieChartView pieChartView = view.findViewById(R.id.chart);
                List<SliceValue> pieData = new ArrayList<>();
                for (int i = 0; i < 8 ; i++) {
                    if(i==2) continue;
                    else pieData.add(new SliceValue(total[i], color[i]).setLabel(label[i]));
                }

                PieChartData pieChartData = new PieChartData(pieData);
                pieChartData.setHasLabels(true).setValueLabelBackgroundEnabled(false);
                //pieChartData.setHasCenterCircle(true).setCenterText1("abc");
                pieChartView.setPieChartData(pieChartData);
            }
        });

////////////////////////////////////////////////////////////////////////////////////////////////////

        dataService.getAge(new DataService.VolleyListner() {
            @Override
            public void onResponse(float[] count) {
                ColumnChartView columnChartView = view.findViewById(R.id.chart2);
                List<Column> columns = new ArrayList<>();
                List<SubcolumnValue> values = new ArrayList<>();
                values.add(new SubcolumnValue(count[0], ChartUtils.pickColor()));
                values.add(new SubcolumnValue(count[1], ChartUtils.pickColor()));
                values.add(new SubcolumnValue(count[2], ChartUtils.pickColor()));
//                No values in following category
//                values.add(new SubcolumnValue(count[3], ChartUtils.pickColor()));
//                values.add(new SubcolumnValue(count[4], ChartUtils.pickColor()));

                Column column = new Column(values);
                column.setHasLabels(true);
                columns.add(column);
                ColumnChartData columnChartData = new ColumnChartData(columns);
                Axis axis = new Axis();
                axis.setHasTiltedLabels(true);
                List<AxisValue> axisValues = new ArrayList<>();
                String[] labels = {"20-30", "31-40", "41-50"};
                axisValues.add(new AxisValue(-0.3f).setLabel(labels[0]));
                axisValues.add(new AxisValue(0).setLabel(labels[1]));
                axisValues.add(new AxisValue(0.3f).setLabel(labels[2]));


                axis.setValues(axisValues);
                axis.setName("age");
                columnChartData.setAxisXBottom(axis);
                columnChartView.setColumnChartData(columnChartData);
            }
        });

////////////////////////////////////////////////////////////////////////////////////////////////////
        TextView r1, r2, r3, r4, r5;
        r1 = view.findViewById(R.id.report1);
        r2 = view.findViewById(R.id.report2);
        r3 = view.findViewById(R.id.report3);
        r4 = view.findViewById(R.id.report4);
        r5 = view.findViewById(R.id.report5);

        TableLayout tableLayout = view.findViewById(R.id.tableLayout);
        TableRow tableRow1 = (TableRow) tableLayout.getChildAt(2);
        TableRow tableRow2 = (TableRow) tableLayout.getChildAt(4);
        TableRow tableRow3 = (TableRow) tableLayout.getChildAt(6);
        TableRow tableRow4 = (TableRow) tableLayout.getChildAt(8);
        TableRow tableRow5 = (TableRow) tableLayout.getChildAt(10);


        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView nameTv = (TextView) tableRow1.getChildAt(1);
                TextView heightTv = (TextView) tableRow1.getChildAt(2);
                TextView weightTv = (TextView) tableRow1.getChildAt(3);
                TextView bloodTv =  (TextView) tableRow1.getChildAt(4);
                TextView eyeColorTv = (TextView) tableRow1.getChildAt(5);

                String name = nameTv.getText().toString();
                String height = heightTv.getText().toString();
                String weight = weightTv.getText().toString();
                String blood = bloodTv.getText().toString();
                String eyeColor = eyeColorTv.getText().toString();

                try {
                    createPdf(name, height, weight, blood, eyeColor, "1");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView nameTv = (TextView) tableRow2.getChildAt(1);
                TextView heightTv = (TextView) tableRow2.getChildAt(2);
                TextView weightTv = (TextView) tableRow2.getChildAt(3);
                TextView bloodTv =  (TextView) tableRow2.getChildAt(4);
                TextView eyeColorTv = (TextView) tableRow2.getChildAt(5);

                String name = nameTv.getText().toString();
                String height = heightTv.getText().toString();
                String weight = weightTv.getText().toString();
                String blood = bloodTv.getText().toString();
                String eyeColor = eyeColorTv.getText().toString();

                try {
                    createPdf(name, height, weight, blood, eyeColor, "2");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView nameTv = (TextView) tableRow3.getChildAt(1);
                TextView heightTv = (TextView) tableRow3.getChildAt(2);
                TextView weightTv = (TextView) tableRow3.getChildAt(3);
                TextView bloodTv =  (TextView) tableRow3.getChildAt(4);
                TextView eyeColorTv = (TextView) tableRow3.getChildAt(5);

                String name = nameTv.getText().toString();
                String height = heightTv.getText().toString();
                String weight = weightTv.getText().toString();
                String blood = bloodTv.getText().toString();
                String eyeColor = eyeColorTv.getText().toString();

                try {
                    createPdf(name, height, weight, blood, eyeColor, "3");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView nameTv = (TextView) tableRow4.getChildAt(1);
                TextView heightTv = (TextView) tableRow4.getChildAt(2);
                TextView weightTv = (TextView) tableRow4.getChildAt(3);
                TextView bloodTv =  (TextView) tableRow4.getChildAt(4);
                TextView eyeColorTv = (TextView) tableRow4.getChildAt(5);

                String name = nameTv.getText().toString();
                String height = heightTv.getText().toString();
                String weight = weightTv.getText().toString();
                String blood = bloodTv.getText().toString();
                String eyeColor = eyeColorTv.getText().toString();

                try {
                    createPdf(name, height, weight, blood, eyeColor, "4");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        r5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView nameTv = (TextView) tableRow5.getChildAt(1);
                TextView heightTv = (TextView) tableRow5.getChildAt(2);
                TextView weightTv = (TextView) tableRow5.getChildAt(3);
                TextView bloodTv =  (TextView) tableRow5.getChildAt(4);
                TextView eyeColorTv = (TextView) tableRow5.getChildAt(5);

                String name = nameTv.getText().toString();
                String height = heightTv.getText().toString();
                String weight = weightTv.getText().toString();
                String blood = bloodTv.getText().toString();
                String eyeColor = eyeColorTv.getText().toString();

                try {
                    createPdf(name, height, weight, blood, eyeColor, "5");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });


        return view;
    }



    private void createPdf(String name,  String height, String weight,  String blood, String eyeColor, String  i ) throws IOException {
        Toast.makeText(getActivity(), "PDF Downloading", Toast.LENGTH_SHORT).show();
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint card = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1030, 1920, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();


        cardView.draw(canvas);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(40);

        canvas.drawText("Name : " + name, 540, 960, paint);
        canvas.drawText("Height(cm) : " + height, 540, 1005, paint);
        canvas.drawText("Weight(KG) : " + weight, 540, 1045, paint);
        canvas.drawText("Blood Group : " + blood, 540, 1085, paint);
        canvas.drawText("Eye Color : " + eyeColor, 540, 1125, paint);






        pdfDocument.finishPage(page);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/first"+i+".pdf");
        try {

            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(getActivity(), "Download Completed", Toast.LENGTH_SHORT).show();
        } catch (IOException e){
            e.printStackTrace();
        }
        pdfDocument.close();
    }

}

