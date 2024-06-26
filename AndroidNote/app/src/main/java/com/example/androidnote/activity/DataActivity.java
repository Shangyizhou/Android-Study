package com.example.androidnote.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.androidnote.R;
import com.example.androidnote.fragment.chat.ChatStartFragment;
import com.example.androidnote.fragment.chat.HistoryFragment;
import com.example.androidnote.fragment.data.BarChartFragment;
import com.example.androidnote.fragment.data.LineChartFragment;
import com.example.androidnote.fragment.data.PieChartFragment;
import com.example.androidnote.manager.DataCollectionManager;
import com.example.androidnote.manager.SessionManager;
import com.example.androidnote.model.Message;
import com.example.androidnote.model.MessageExtern;
import com.example.androidnote.model.Session;
import com.example.androidnote.model.TagResponse;
import com.example.androidnote.net.YiYanHandler;
import com.example.androidnote.view.OtherToServer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.IResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DataActivity extends BaseActivity {
    private static final String TAG = DataActivity.class.getSimpleName();
    public static void startUp(Context context) {
        SLog.i(TAG, "[DataActivity] startUp");
        Intent intent = new Intent(context, DataActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreateChildren(Bundle bundle) {
        // setContentView(R.layout.activity_data);
        setContentView(R.layout.activity_data_start);
        initView();
    }

    LineChart lineChart;
    BarChart barChart;
    TextView keyWord;

    PieChart pieChart;

    // viewpager
    ViewPager viewPager;
    TabLayout tabLayout;
    private List<String> titleList;
    private List<Fragment> fragmentList;
    private void initViewFragment() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView == null) {
                    tab.setCustomView(R.layout.tab_text_layout);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(DataActivity.this, R.style.TabLayoutTextSelected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView == null) {
                    tab.setCustomView(R.layout.tab_text_layout);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(DataActivity.this, R.style.TabLayoutTextUnSelected);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        titleList = new ArrayList<>();
        titleList.add("折线图");
        titleList.add("柱状图");
        titleList.add("饼图");

        fragmentList = new ArrayList<>();
        fragmentList.add(new LineChartFragment());
        fragmentList.add(new BarChartFragment());
        fragmentList.add(new PieChartFragment());
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        //给viewPager设置adapter
        viewPager.setAdapter(adapter);
        //tabLayout与viewPager绑定
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return fragmentList.size();//页卡数
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);//页卡标题
        }
    }

    private void initView() {
        initViewFragment();
        // // 找到图表控件
        // lineChart = findViewById(R.id.line_chart);
        // barChart = findViewById(R.id.bar_chart);
        // keyWord = findViewById(R.id.tv_key_list);
        //
        // initLineChart();
        // initBarChart();
        // initKeyWord();
        // initPieChart();
    }

    // private void initLineChart() {
    //     // 准备数据. 比如我们创建一个有10个数据点的图表
    //     int[] invokeData = DataCollectionManager.getYesterdayInvokeInfo();
    //     Random random = new Random(); // 创建Random实例
    //
    //     ArrayList<Entry> values = new ArrayList<>();
    //     for (int i = 0; i < 12; i++) {
    //         // values.add(new Entry(i, invokeData[i]));
    //         values.add(new Entry(i, random.nextInt(100)));
    //     }
    //
    //     // 创建数据集
    //     LineDataSet set1 = new LineDataSet(values, "24h 调用情况");
    //     set1.setFillAlpha(110);
    //
    //     // 使用RGB颜色填充
    //     set1.setColor(Color.BLUE);
    //     set1.setValueTextColor(Color.BLUE);
    //     set1.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 设置曲线模式
    //     set1.setDrawFilled(true); // 设置曲线填充的颜色，这里以起点和终点为基准，进行渐变。
    //
    //     // 创建数据集的一个包装器
    //     ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    //     dataSets.add(set1);
    //
    //     Legend legend = lineChart.getLegend();
    //     legend.setEnabled(false);
    //
    //     // 创建一个数据对象，与图表对象关联
    //     LineData lineData = new LineData(dataSets);
    //     lineChart.setDrawGridBackground(false);
    //     lineChart.setData(lineData);
    //     lineChart.setDragEnabled(true);
    //     lineChart.setScaleEnabled(false);
    //     lineChart.setScaleXEnabled(false);
    //     lineChart.setScaleYEnabled(false);
    //     // 是否可以双指缩放--是
    //     lineChart.setPinchZoom(false);
    //     // 是否可以双击放大--是
    //     lineChart.setDoubleTapToZoomEnabled(false);
    //     lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
    //         @Override
    //         public void onValueSelected(Entry e, Highlight h) {
    //             ToastUtil.getInstance().showToast(e.toString());
    //         }
    //
    //         @Override
    //         public void onNothingSelected() {
    //
    //         }
    //     });
    //
    //     // 设置y轴左侧坐标
    //     YAxis leftAxis = lineChart.getAxisLeft();
    //     leftAxis.setAxisMinimum(0f);
    //     leftAxis.setGranularity(1f);
    //     leftAxis.setGranularityEnabled(true);
    //     leftAxis.setDrawGridLines(false);
    //
    //     float axisInterval = 20f;      // 你需要的轴间隔
    //
    //     float axisMinimum = 0f;    // 假设的最小值
    //     float axisMaximum = 100f;  // 假设的最大值
    //
    //     lineChart.getAxisLeft().setAxisMinimum(axisMinimum);    // 左轴最小值
    //     lineChart.getAxisLeft().setAxisMaximum(axisMaximum);    // 左轴最大值
    //
    //     int labelCount = (int) ((axisMaximum - axisMinimum) / axisInterval) + 1;
    //     lineChart.getAxisLeft().setLabelCount(labelCount, true);  // 设置左轴的标签数，true表示强制的标签数目，即使有时候不能够完美的均匀分布
    //
    //     // 关闭右侧y轴和顶部x轴的显示
    //     lineChart.getAxisRight().setEnabled(false);
    //     lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
    //
    //     // 创建一个空的Description对象
    //     Description description = new Description();
    //     description.setText("");
    //
    //     // 将空的Description对象设置到图表
    //     lineChart.setDescription(description);
    //
    //     // 无数据时显示
    //     lineChart.setNoDataText("没有获取到数据哦~");
    //
    //     // 更新图表
    //     lineChart.notifyDataSetChanged();
    //     lineChart.invalidate();
    // }
    //
    // private void initBarChart() {
    //     ArrayList<BarEntry> entries = new ArrayList<>();
    //     /**
    //      * x轴位置和y轴高度
    //      */
    //     entries.add(new BarEntry(1f, 30f));
    //     entries.add(new BarEntry(2f, 80f));
    //     entries.add(new BarEntry(3f, 60f));
    //
    //     // add other entries as you want
    //     BarDataSet dataSet = new BarDataSet(entries, "Bar data");
    //     BarData barData = new BarData(dataSet);
    //
    //     /**
    //      * y轴
    //      */
    //     YAxis yAxis = barChart.getAxisRight();
    //     yAxis.setEnabled(false); // 禁用右侧Y轴
    //
    //     /**
    //      * x轴
    //      */
    //     XAxis xAxis = barChart.getXAxis();
    //     xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 将X轴的位置设置为底部
    //     xAxis.setDrawGridLines(false); // 去除X轴网格线
    //     // xAxis.setCenterAxisLabels(true); // 设置为 true 让 labels 居中
    //     // 禁止缩放
    //     barChart.setAutoScaleMinMaxEnabled(false);
    //     xAxis.setAxisMinimum(0f);
    //     xAxis.setGranularity(1f); // 设置 labels 之间的最小间隔
    //
    //     // 设置x轴的标签
    //     List<String> labels = new ArrayList<>();
    //     labels.add("");
    //     labels.add("会话1");
    //     labels.add("会话2");
    //     labels.add("会话3");
    //
    //     ValueFormatter formatter = new ValueFormatter() {
    //         @Override
    //         public String getAxisLabel(float value, AxisBase axis) {
    //             int index = (int) value;
    //             if (index < 0 || index >= labels.size()) {
    //                 return "";
    //             }
    //             return labels.get(index);
    //         }
    //     };
    //     xAxis.setValueFormatter(formatter);
    //
    //     Legend legend = barChart.getLegend(); // 获取图例
    //     legend.setEnabled(false); // 禁用或隐藏图例
    //
    //     /**
    //      * 设置形容
    //      */
    //     Description description = new Description();
    //     description.setEnabled(false);
    //     barChart.setDescription(description);
    //
    //     // 允许用户通过在图表上做拖动手势来水平滚动图表
    //     barChart.setDragEnabled(true);
    //     // 仅允许水平方向滚动
    //     barChart.setScaleYEnabled(false);
    //
    //     // dataSet.setBarBorderWidth(0.9f);  //这里可以设置每个柱体相对大小的空闲部分（如果你有多个并列的 barEntry）
    //     // barData.setBarWidth(0.6f);  //这里可以设置每个柱体的宽度
    //
    //     // 去除Y轴网格线
    //     barChart.getAxisLeft().setDrawGridLines(false);
    //     barChart.getAxisRight().setDrawGridLines(false);
    //     barChart.setData(barData);
    //     barChart.invalidate(); // refresh
    // }
    //
    // private void initKeyWord() {
    //     OtherToServer.callKeyWords("", new IResponse() {
    //         @Override
    //         public void onSuccess(String originJson) {
    //             TagResponse tagResponse = new Gson().fromJson(originJson, TagResponse.class);
    //             SLog.i(TAG, "Gson after translate" + tagResponse);
    //             List<String> keyWords = new ArrayList<>();
    //             String key = "";
    //             for (TagResponse.Results res : tagResponse.getResults()) {
    //                 // keyWords.add(res.getWord());
    //                 key += res.getWord() + ", ";
    //             }
    //             String finalKey = key;
    //             runOnUiThread(new Runnable() {
    //                 @Override
    //                 public void run() {
    //                     keyWord.setText(finalKey);
    //                 }
    //             });
    //         }
    //
    //         @Override
    //         public void onFailure(String errorMsg) {
    //
    //         }
    //     });
    // }
    //
    // private HashMap<String, Integer> intentMap = new HashMap<>();
    // private void getIntentData() {
    //     Gson json = new Gson();
    //     for (Session session : SessionManager.getInstance().getSessionList()) {
    //         List<Message> messageList = SessionManager.getInstance().getSessionMessages(session);
    //         for (Message message : messageList) {
    //             String ext = message.getExt();
    //             MessageExtern messageExtern = json.fromJson(ext, MessageExtern.class);
    //             if (messageExtern != null) {
    //                 String intent = messageExtern.getIntent();
    //                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    //                     intentMap.put(intent, intentMap.getOrDefault(intent, 0) + 1);
    //                 }
    //             }
    //         }
    //     }
    // }
    //
    // private void initPieChart() {
    //     getIntentData();
    //     SLog.i(TAG, "initPieChart");
    //
    //     pieChart = findViewById(R.id.pie_chart);
    //     // 禁用图表上半部分的绘制
    //     pieChart.setUsePercentValues(true);
    //     pieChart.getDescription().setEnabled(false);
    //     pieChart.setExtraOffsets(5, 10, 5, 5);
    //
    //     pieChart.setDragDecelerationFrictionCoef(0.95f);
    //
    //     ArrayList<PieEntry> yValues = new ArrayList<>();
    //     for (Map.Entry<String, Integer> entry : intentMap.entrySet()) {
    //         if (entry == null || TextUtils.isEmpty(entry.getKey())) {
    //             continue;
    //         }
    //         System.out.println(entry.getKey() + ": " + entry.getValue());
    //         yValues.add(new PieEntry(entry.getValue(), entry.getKey())); // 添加数据和对应的标签
    //     }
    //
    //     PieDataSet dataSet = new PieDataSet(yValues, "询问类型");
    //     dataSet.setSliceSpace(3f);
    //     dataSet.setSelectionShift(5f);
    //
    //     ArrayList<Integer> colors = new ArrayList<>();
    //     colors.add(ColorTemplate.VORDIPLOM_COLORS[0]);
    //     colors.add(ColorTemplate.VORDIPLOM_COLORS[1]);
    //     colors.add(ColorTemplate.VORDIPLOM_COLORS[2]);
    //     colors.add(ColorTemplate.VORDIPLOM_COLORS[3]);
    //
    //     dataSet.setColors(colors);
    //
    //     PieData data = new PieData(dataSet);
    //     pieChart.setData(data);
    //
    //     // 刷新图表
    //     pieChart.invalidate();
    // }
}