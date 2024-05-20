package com.example.androidnote.fragment.data;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidnote.R;
import com.example.androidnote.db.helper.ResponseInfoHelper;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.manager.DataCollectionManager;
import com.example.androidnote.model.ResponseInfo;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.shangyizhou.develop.helper.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LineChartFragment extends Fragment {

    public LineChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_chart, container, false);
        initView(view);
        return view;
    }

    LineChart lineChart;

    private void getData() {
        List<ResponseInfo> responseInfoList = ResponseInfoHelper.getInstance().getInfoByUserName(BmobManager.getInstance().getObjectId());

    }
    private void initView(View view) {
        lineChart = view.findViewById(R.id.line_chart);
        // 准备数据. 比如我们创建一个有10个数据点的图表
        // int[] invokeData = DataCollectionManager.getYesterdayInvokeInfo();
        Random random = new Random(); // 创建Random实例

        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            // values.add(new Entry(i, invokeData[i]));
            values.add(new Entry(i, random.nextInt(100)));
        }

        // 创建数据集
        LineDataSet set1 = new LineDataSet(values, "24h 调用情况");
        set1.setFillAlpha(110);

        // 使用RGB颜色填充
        set1.setColor(Color.BLUE);
        set1.setValueTextColor(Color.BLUE);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 设置曲线模式
        set1.setDrawFilled(true); // 设置曲线填充的颜色，这里以起点和终点为基准，进行渐变。

        // 创建数据集的一个包装器
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        // 创建一个数据对象，与图表对象关联
        LineData lineData = new LineData(dataSets);
        lineChart.setDrawGridBackground(false);
        lineChart.setData(lineData);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);
        // 是否可以双指缩放--是
        lineChart.setPinchZoom(false);
        // 是否可以双击放大--是
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                ToastUtil.getInstance().showToast(e.toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // 设置y轴左侧坐标
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setDrawGridLines(false);

        float axisInterval = 20f;      // 你需要的轴间隔

        float axisMinimum = 0f;    // 假设的最小值
        float axisMaximum = 100f;  // 假设的最大值

        lineChart.getAxisLeft().setAxisMinimum(axisMinimum);    // 左轴最小值
        lineChart.getAxisLeft().setAxisMaximum(axisMaximum);    // 左轴最大值

        int labelCount = (int) ((axisMaximum - axisMinimum) / axisInterval) + 1;
        lineChart.getAxisLeft().setLabelCount(labelCount, true);  // 设置左轴的标签数，true表示强制的标签数目，即使有时候不能够完美的均匀分布

        // 关闭右侧y轴和顶部x轴的显示
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // 创建一个空的Description对象
        Description description = new Description();
        description.setText("");

        // 将空的Description对象设置到图表
        lineChart.setDescription(description);

        // 无数据时显示
        lineChart.setNoDataText("没有获取到数据哦~");

        // 更新图表
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }
}