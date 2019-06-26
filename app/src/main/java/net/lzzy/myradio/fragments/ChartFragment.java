package net.lzzy.myradio.fragments;



import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import net.lzzy.myradio.R;
import net.lzzy.myradio.models.Radio;
import net.lzzy.myradio.models.Region;
import net.lzzy.myradio.models.UserCookies;
import net.lzzy.myradio.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

import androidx.core.util.Pair;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;



public class ChartFragment extends BaseFragment {
    private PieChart pChart;
    private LineChart lChart;
    private BarChart b1Chart;
    private BarChart b2Chart;
    private Chart[] charts;
    private String[] titles =new String[]{"App访问次数","各地区访问比例(%)","最受欢迎电台","最受欢迎节目",""};
    private View[] dots;
    private float touchX1;
    private int chartIndex=0;
    private TextView tvTitle;
    private RadioGroup rg;
    private RadioButton today;
    private RadioButton week;
    private RadioButton month;
    private RadarChart radarView;

    @Override
    protected void populate() {
        initCharts();
        rg = find(R.id.fragment_chart_rg);
        today = find(R.id.fragment_chart_rb_today);
        week = find(R.id.fragment_chart_rb_week);
        month = find(R.id.fragment_chart_rb_month);

        configBarLineChart(lChart);
        configBarLineChart(b1Chart);
        configBarLineChart(b2Chart);
        configPieChart();
        hebdomaddisplayLineChart();
        hebdomaddisplayPieChart1();
        hebdomaddisplayBarChart();
        hebdomaddisplayBarChart1();
        hebdomaddisplayRadarChart();

        //region  导航点滑动切换
        lChart.setVisibility(View.VISIBLE);
        View dot1=find(R.id.fragment_chart_dot1);
        View dot2=find(R.id.fragment_chart_dot2);
        View dot3=find(R.id.fragment_chart_dot3);
        View dot4=find(R.id.fragment_chart_dot4);
        View dot5=find(R.id.fragment_chart_dot5);
        dots = new View[]{dot1,dot2,dot3,dot4,dot5};
        find(R.id.fragment_chart_container).setOnTouchListener(new ViewUtils.AbstractTouchHandler() {
            private static final float MIN_DISTANCE = 100;

            @Override
            public boolean handleTouch(MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    touchX1 = event.getX();
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    float touchX2=event.getX();
                    if (Math.abs(touchX2-touchX1)>MIN_DISTANCE){
                        if (touchX2<touchX1){
                            if (chartIndex<charts.length-1){
                                chartIndex++;
                            }else {
                                chartIndex=0;
                            }
                        }else {
                            if (chartIndex>0){
                                chartIndex--;
                            }else {
                                chartIndex=charts.length-1;
                            }
                        }
                        switchChart();
                    }
                }
                return true;
            }

            private void switchChart() {
                for (int i=0;i<charts.length;i++){
                    if (chartIndex==i){
                        tvTitle.setText(titles[i]);
                        charts[i].setVisibility(View.VISIBLE);
                        dots[i].setBackgroundResource(R.drawable.dot_fill_style);
                    }else {
                        charts[i].setVisibility(View.GONE);
                        dots[i].setBackgroundResource(R.drawable.dot_style);
                    }
                }
            }
        });
        //endregion

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.fragment_chart_rb_today:
                        /** 当天的地区访问次数饼图 **/
                        todaydisplayPieChart();

                        /** 当天的电台访问次数柱状图 **/
                        todaydisplayBarChart();

                        /** 当天的节目访问次数柱状图 **/
                        todaydisplayBarChart1();

                        /** 当天电台类型锋向雷达图 **/
                        todaydisplayRadarChart();

                        break;
                    case R.id.fragment_chart_rb_week:
                        /** 一周的App访问次数折线图 **/
                        hebdomaddisplayLineChart();

                        /** 一周的地区访问次数饼图 **/
                        hebdomaddisplayPieChart1();

                        /** 一周的电台访问次数柱状图 **/
                        hebdomaddisplayBarChart();

                        /** 一周的节目访问次数柱状图 **/
                        hebdomaddisplayBarChart1();

                        /**  一周的电台类型锋向雷达图 **/
                        hebdomaddisplayRadarChart();

                        break;
                    case R.id.fragment_chart_rb_month:
                        /** 一个月的App访问次数折线图 **/
                        monthdisplayLineChart();

                        /** 一个月的地区访问次数饼图 **/
                        monthdisplayPieChart1();

                        /** 一个月的电台访问次数柱状图 **/
                        monthdisplayBarChart();

                        /** 一个月的节目访问次数柱状图 **/
                        monthdisplayBarChart1();

                        /** 一个月的电台类型锋向雷达图 **/
                        monthdisplayRadarChart();

                        break;
                    default:
                        break;
                }
            }
        });



    }

    private void monthdisplayLineChart() {
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH)+1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        ValueFormatter xFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int i=30;
                int day1=(int) value+day-28;
                int month1=month;
                if (day1<1){
                    month1=month-1;
                    day1=i-Math.abs(day1);
                }
                return month1+"."+day1+"  ";
            }
        };
        lChart.getXAxis().setValueFormatter(xFormatter);
        lChart.getXAxis().setLabelRotationAngle(-75);
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 29; i>=0; i--){
            int times= UserCookies.getInstance().GetAppVisitCount(year+"-"+(month<10?"0"+month:month)+"-"+(day-i));
            entries.add(new Entry(28-i,times));

        }

        LineDataSet dataSet=new LineDataSet(entries,"查看次数");
        dataSet.setColor(Color.BLACK);
        dataSet.setLineWidth(1f);
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setValueTextSize(9f);
        LineData data = new LineData(dataSet);
        lChart.setData(data);
        lChart.invalidate();
    }

    private void monthdisplayRadarChart() {

    }

    private void monthdisplayBarChart1() {
        List<Pair<String,Integer>> items=UserCookies.getInstance().getProgramsFrequency(new Date(),29);
        Collections.sort(items,(o1, o2) -> Objects.requireNonNull(o2.second)
                .compareTo(Objects.requireNonNull(o1.second)));
        ValueFormatter xAxisFormatter=new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return items.get((int) value).first;
            }
        };
        b2Chart.getXAxis().setValueFormatter(xAxisFormatter);
        b2Chart.getXAxis().setLabelRotationAngle(-75);
        b2Chart.getXAxis().setLabelCount(6);
        List<BarEntry> entries=new ArrayList<>();
        int i=0;
        for (Pair<String,Integer> item:items){
            if (i == 6){
                break;
            }
            entries.add(new BarEntry(i,Objects.requireNonNull(item.second)));
            i++;
        }


        BarDataSet dataSet=new BarDataSet(entries,"最受欢迎节目");
        dataSet.setColors(Color.GREEN,Color.GRAY,Color.DKGRAY,Color.LTGRAY,Color.RED,Color.BLUE,Color.CYAN,Color.MAGENTA);
        ArrayList<IBarDataSet> dataSets=new ArrayList<>();
        dataSets.add(dataSet);
        BarData data = new BarData(dataSets);
        data.setBarWidth(0.8f);
        b2Chart.setData(data);
        b2Chart.invalidate();
    }

    private void monthdisplayBarChart() {
        List<Pair<String,Integer>> items=UserCookies.getInstance().getRadioFrequency(new Date(),29);
        Collections.sort(items,(o1, o2) -> Objects.requireNonNull(o2.second)
                .compareTo(Objects.requireNonNull(o1.second)));
        ValueFormatter xAxisFormatter=new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return items.get((int) value).first;
            }
        };
        b1Chart.getXAxis().setValueFormatter(xAxisFormatter);
        b1Chart.getXAxis().setLabelRotationAngle(-75);
        b1Chart.getXAxis().setLabelCount(6);
        List<BarEntry> entries=new ArrayList<>();
        int i=0;
        for (Pair<String,Integer> item:items){
            if (i == 6){
                break;
            }
            entries.add(new BarEntry(i,Objects.requireNonNull(item.second)));
            i++;
        }

        BarDataSet dataSet=new BarDataSet(entries,"最受欢迎电台");
        dataSet.setColors(Color.GREEN,Color.GRAY,Color.DKGRAY,Color.LTGRAY,Color.RED,Color.BLUE);
        ArrayList<IBarDataSet> dataSets=new ArrayList<>();
        dataSets.add(dataSet);
        BarData data = new BarData(dataSets);
        data.setBarWidth(0.8f);
        b1Chart.setData(data);
        b1Chart.invalidate();
    }

    private void monthdisplayPieChart1() {
        List<Pair<String,Integer>> items=UserCookies.getInstance().getRegionsFrequency(new Date(),29);
        Collections.sort(items,(o1, o2) -> Objects.requireNonNull(o2.second)
                .compareTo(Objects.requireNonNull(o1.second)));
        List<PieEntry> entries=new ArrayList<>();
        int i=0;
        for (Pair<String,Integer> item:items){
            if (i == 8){
                break;
            }
            entries.add(new PieEntry(Objects.requireNonNull(item.second),item.first));
            i++;
        }

        PieDataSet dataSet = new PieDataSet(entries, "各地区的访问比例（%）");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(8f);
        List<Integer> colors=new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.LTGRAY);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.DKGRAY);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pChart.setData(data);
        pChart.invalidate();

    }

    private void todaydisplayRadarChart() {

    }

    private void hebdomaddisplayRadarChart() {

        List<String> xAxisValue = new ArrayList<>();//X轴数据源


        radarView.getDescription().setEnabled(false);

        XAxis xAxis = radarView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(10);
        xAxis.setLabelCount(xAxisValue.size());
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValue));

        List<RadarEntry> radarEntries = new ArrayList<>();
        radarEntries.add(new RadarEntry(80));
        radarEntries.add(new RadarEntry(85));
        radarEntries.add(new RadarEntry(90));
        radarEntries.add(new RadarEntry(70));
        radarEntries.add(new RadarEntry(95));

        RadarDataSet radarDataSet = new RadarDataSet(radarEntries, "电台类锋向图");
        // 实心填充区域颜色
        radarDataSet.setFillColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        // 是否实心填充区域
        radarDataSet.setDrawFilled(true);
        RadarData radarData = new RadarData(radarDataSet);
        radarView.setData(radarData);

    }

    private void todaydisplayBarChart1(){
        List<Pair<String,Integer>> items=UserCookies.getInstance().getProgramsFrequency(new Date(),0);
        Collections.sort(items,(o1, o2) -> Objects.requireNonNull(o2.second)
                .compareTo(Objects.requireNonNull(o1.second)));
        ValueFormatter xAxisFormatter=new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return items.get((int) value).first;
            }
        };
        b2Chart.getXAxis().setValueFormatter(xAxisFormatter);
        b2Chart.getXAxis().setLabelRotationAngle(-75);
        b2Chart.getXAxis().setLabelCount(6);
        List<BarEntry> entries=new ArrayList<>();
        int i=0;
        for (Pair<String,Integer> item:items){
            if (i == 6){
                break;
            }
            entries.add(new BarEntry(i,Objects.requireNonNull(item.second)));
            i++;
        }


        BarDataSet dataSet=new BarDataSet(entries,"最受欢迎节目");
        dataSet.setColors(Color.GREEN,Color.GRAY,Color.DKGRAY,Color.LTGRAY,Color.RED,Color.BLUE,Color.CYAN,Color.MAGENTA);
        ArrayList<IBarDataSet> dataSets=new ArrayList<>();
        dataSets.add(dataSet);
        BarData data = new BarData(dataSets);
        data.setBarWidth(0.8f);
        b2Chart.setData(data);
        b2Chart.invalidate();

    }

    private void hebdomaddisplayBarChart1() {
        List<Pair<String,Integer>> items=UserCookies.getInstance().getProgramsFrequency(new Date(),6);
        Collections.sort(items,(o1, o2) -> Objects.requireNonNull(o2.second)
                .compareTo(Objects.requireNonNull(o1.second)));
        ValueFormatter xAxisFormatter=new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return items.get((int) value).first;
            }
        };
        b2Chart.getXAxis().setValueFormatter(xAxisFormatter);
        b2Chart.getXAxis().setLabelRotationAngle(-75);
        b2Chart.getXAxis().setLabelCount(6);
        List<BarEntry> entries=new ArrayList<>();
        int i=0;
        for (Pair<String,Integer> item:items){
            if (i == 6){
                break;
            }
            entries.add(new BarEntry(i,Objects.requireNonNull(item.second)));
            i++;
        }


        BarDataSet dataSet=new BarDataSet(entries,"最受欢迎节目");
        dataSet.setColors(Color.GREEN,Color.GRAY,Color.DKGRAY,Color.LTGRAY,Color.RED,Color.BLUE,Color.CYAN,Color.MAGENTA);
        ArrayList<IBarDataSet> dataSets=new ArrayList<>();
        dataSets.add(dataSet);
        BarData data = new BarData(dataSets);
        data.setBarWidth(0.8f);
        b2Chart.setData(data);
        b2Chart.invalidate();

    }

    private void todaydisplayBarChart(){
        List<Pair<String,Integer>> items=UserCookies.getInstance().getRadioFrequency(new Date(),0);
        Collections.sort(items,(o1, o2) -> Objects.requireNonNull(o2.second)
                .compareTo(Objects.requireNonNull(o1.second)));
        ValueFormatter xAxisFormatter=new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return items.get((int) value).first;
            }
        };
        b1Chart.getXAxis().setValueFormatter(xAxisFormatter);
        b1Chart.getXAxis().setLabelRotationAngle(-75);
        b1Chart.getXAxis().setLabelCount(6);
        List<BarEntry> entries=new ArrayList<>();
        int i=0;
        for (Pair<String,Integer> item:items){
            if (i == 6){
                break;
            }
            entries.add(new BarEntry(i,Objects.requireNonNull(item.second)));
            i++;
        }

        BarDataSet dataSet=new BarDataSet(entries,"最受欢迎电台");
        dataSet.setColors(Color.GREEN,Color.GRAY,Color.DKGRAY,Color.LTGRAY,Color.RED,Color.BLUE);
        ArrayList<IBarDataSet> dataSets=new ArrayList<>();
        dataSets.add(dataSet);
        BarData data = new BarData(dataSets);
        data.setBarWidth(0.8f);
        b1Chart.setData(data);
        b1Chart.invalidate();

    }

    private void hebdomaddisplayBarChart() {

        List<Pair<String,Integer>> items=UserCookies.getInstance().getRadioFrequency(new Date(),6);
        Collections.sort(items,(o1, o2) -> Objects.requireNonNull(o2.second)
                .compareTo(Objects.requireNonNull(o1.second)));
        ValueFormatter xAxisFormatter=new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return items.get((int) value).first;
            }
        };
        b1Chart.getXAxis().setValueFormatter(xAxisFormatter);
        b1Chart.getXAxis().setLabelRotationAngle(-75);
        b1Chart.getXAxis().setLabelCount(6);
        List<BarEntry> entries=new ArrayList<>();
        int i=0;
        for (Pair<String,Integer> item:items){
            if (i == 6){
                break;
            }
            entries.add(new BarEntry(i,Objects.requireNonNull(item.second)));
            i++;
        }

        BarDataSet dataSet=new BarDataSet(entries,"最受欢迎电台");
        dataSet.setColors(Color.GREEN,Color.GRAY,Color.DKGRAY,Color.LTGRAY,Color.RED,Color.BLUE);
        ArrayList<IBarDataSet> dataSets=new ArrayList<>();
        dataSets.add(dataSet);
        BarData data = new BarData(dataSets);
        data.setBarWidth(0.8f);
        b1Chart.setData(data);
        b1Chart.invalidate();
    }

    private void hebdomaddisplayLineChart() {
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH)+1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        ValueFormatter xFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int day1=(int) value+day-5;
                return month+"."+day1+"  ";
            }
        };
        lChart.getXAxis().setValueFormatter(xFormatter);
        lChart.getXAxis().setLabelRotationAngle(-75);
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 6; i>=0; i--){
            int times= UserCookies.getInstance().GetAppVisitCount(year+"-"+(month<10?"0"+month:month)+"-"+(day-i));
            entries.add(new Entry(5-i,times));

        }

        LineDataSet dataSet=new LineDataSet(entries,"查看次数");
        dataSet.setColor(Color.BLACK);
        dataSet.setLineWidth(1f);
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setValueTextSize(9f);
        LineData data = new LineData(dataSet);
        lChart.setData(data);
        lChart.invalidate();
    }

    private void configBarLineChart(BarLineChartBase chart) {
        /**  X 轴 **/
        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);
        xAxis.setGranularity(1f);

        /**  Y 轴 **/
        YAxis yAxis=chart.getAxisLeft();
        yAxis.setLabelCount(8,false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setTextSize(12f);
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0);

        /** chart属性 **/
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.setPinchZoom(false);


        // // Create Limit Lines // //
        LimitLine llXAxis = new LimitLine(9f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);


        LimitLine ll1 = new LimitLine(150f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);


        LimitLine ll2 = new LimitLine(0f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);


        // draw limit lines behind data instead of on top
        yAxis.setDrawLimitLinesBehindData(true);
        xAxis.setDrawLimitLinesBehindData(false);

        // add limit lines
        yAxis.addLimitLine(ll1);
        yAxis.addLimitLine(ll2);
        //xAxis.addLimitLine(llXAxis);


    }

    private void configPieChart() {
        pChart.setUsePercentValues(true);
        pChart.setDrawHoleEnabled(false);
        pChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        pChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
    }

    private void todaydisplayPieChart() {

        List<Pair<String,Integer>> items=UserCookies.getInstance().getRegionsFrequency(new Date(),0);
        Collections.sort(items,(o1, o2) -> Objects.requireNonNull(o2.second)
                .compareTo(Objects.requireNonNull(o1.second)));
        List<PieEntry> entries=new ArrayList<>();
        int i=0;
        for (Pair<String,Integer> item:items){
            if (i == 8){
                break;
            }
            entries.add(new PieEntry(Objects.requireNonNull(item.second),item.first));
            i++;
        }

        PieDataSet dataSet = new PieDataSet(entries, "各地区的访问比例（%）");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(8f);
        List<Integer> colors=new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.LTGRAY);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.DKGRAY);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pChart.setData(data);
        pChart.invalidate();

    }

    private void hebdomaddisplayPieChart1() {
        List<Pair<String,Integer>> items=UserCookies.getInstance().getRegionsFrequency(new Date(),6);
        Collections.sort(items,(o1, o2) -> Objects.requireNonNull(o2.second)
                .compareTo(Objects.requireNonNull(o1.second)));
        List<PieEntry> entries=new ArrayList<>();
        int i=0;
        for (Pair<String,Integer> item:items){
            if (i == 8){
                break;
            }
            entries.add(new PieEntry(Objects.requireNonNull(item.second),item.first));
            i++;
        }

        PieDataSet dataSet = new PieDataSet(entries, "各地区的访问比例（%）");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(8f);
        List<Integer> colors=new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.LTGRAY);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.DKGRAY);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pChart.setData(data);
        pChart.invalidate();

    }

    private void initCharts() {
        tvTitle = find(R.id.fragment_chart_tv_title);
        pChart = find(R.id.fragment_chart_pie);
        lChart = find(R.id.fragment_chart_line);
        b1Chart = find(R.id.fragment_chart_bar1);
        b2Chart = find(R.id.fragment_chart_bar2);
        radarView = find(R.id.fragment_chart_radar);
        charts = new Chart[]{lChart,pChart,b1Chart,b2Chart,radarView};

        for (Chart chart: charts){
            chart.setTouchEnabled(false);
            chart.setVisibility(View.GONE);
            Description desc = new Description();
            chart.setDescription(desc);
            chart.setNoDataText("数据获取中.....");
            chart.setExtraOffsets(5, 10, 5, 5);
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_chart;
    }

    @Override
    public void search(String kw) {

    }
}
