<template>
  <div style="text-align: center">
    <a-row>
      <a-col :span="24">
        <a-card>
          <a-row>
            <a-col :span="8">
              <a-statistic title="总阅读量" :value="statistic.viewCount">
                <template #prefix>
                  <UserOutlined/>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic title="总点赞量" :value="statistic.voteCount">
                <template #prefix>
                  <like-outlined/>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="8">
              <a-statistic title="点赞率" :value="statistic.voteCount / statistic.viewCount * 100"
                           :precision="2"
                           prefix="%"
                           :value-style="{ color: '#cf1322' }">
                <template #prefix>
                  <like-outlined/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    <br>
    <a-row :gutter="16">
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic title="今日阅读" :value="statistic.todayViewCount" style="margin-right: 50px">
                <template #prefix>
                  <UserOutlined/>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic title="今日点赞" :value="statistic.todayVoteCount">
                <template #prefix>
                  <like-outlined/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card>
          <a-row>
            <a-col :span="12">
              <a-statistic
                  title="预计今日阅读"
                  :value="statistic.todayViewIncrease"
                  :value-style="{ color: '#0000ff' }"
              >
                <template #prefix>
                  <UserOutlined/>
                </template>
              </a-statistic>
            </a-col>
            <a-col :span="12">
              <a-statistic
                  title="预计今日阅读增长"
                  :value="statistic.todayViewIncreaseRateAbs"
                  :precision="2"
                  suffix="%"
                  class="demo-class"
                  :value-style="statistic.todayViewIncreaseRate < 0 ? { color: '#3f8600' } : { color: '#cf1322' }"
              >
                <template #prefix>
                  <arrow-down-outlined v-if="statistic.todayViewIncreaseRate < 0"/>
                  <arrow-up-outlined v-if="statistic.todayViewIncreaseRate >= 0"/>
                </template>
              </a-statistic>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
    <br>
    <a-row>
      <a-col :span="24" id="main-col">
        <div id="main" style="width: 100%;height:300px;"></div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";

declare let echarts: any;

export default defineComponent({
  name: 'the-welcome',
  setup() {
    const statistic = ref();
    statistic.value = {};

    const getStatistic = () => {
      axios.get("/ebook-snapshot/get-statistic").then((response) => {
        const data = response.data;
        if (data.success) {
          const statisticResp = data.content;
          statistic.value.viewCount = statisticResp[1].viewCount;
          statistic.value.voteCount = statisticResp[1].voteCount;
          statistic.value.todayViewCount = statisticResp[1].viewIncrease;
          statistic.value.todayVoteCount = statisticResp[1].voteIncrease;

          const now = new Date();
          // 按分钟计算当前时间点占一天的百分比
          const nowRate = (now.getHours() * 60 + now.getMinutes()) / (60 * 24);
          // 预计今日总阅读数 = 当前今日阅读数 / 当前时间点百分比
          statistic.value.todayViewIncrease = parseInt(String(statistic.value.todayViewCount / nowRate));
          // 预计今日增长率 = (今日预计总阅读数 - 昨天阅读数) / 昨天阅读数
          statistic.value.todayViewIncreaseRate = (statistic.value.todayViewIncrease - statisticResp[0].viewCount) / (statisticResp[0].viewCount);
          // 预计今日增长率可正可负，取其绝对值，为正则使用向上的箭头，为负则使用向下的箭头
          statistic.value.todayViewIncreaseRateAbs = Math.abs(statistic.value.todayViewIncreaseRate);
        }
      });
    };

    const init30DayEcharts = (list: any) => {
      const myChart = echarts.init(document.getElementById('main'));
      const xAxis = [];
      const seriesView = [];
      const seriesVote = [];

      for (let i = 0; i < list.length; i++) {
        const record = list[i];
        xAxis.push(record.date);
        seriesView.push(record.viewIncrease);
        seriesVote.push(record.voteIncrease);
      }

      const option = {
        title: {
          text: '30天趋势图'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['阅读量', '点赞量']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {}
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: xAxis
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '阅读量',
            type: 'line',
            smooth: true,
            data: seriesView
          },
          {
            name: '点赞量',
            type: 'line',
            smooth: true,
            data: seriesVote
          }
        ]
      }
      myChart.setOption(option);
    };

    const get30DatStatistic = () => {
      axios.get("/ebook-snapshot/get-30statistic").then((response) => {
        const data = response.data;
        if (data.success) {
          init30DayEcharts(data.content);
        }
      })
    };

    onMounted(() => {
      getStatistic();
      get30DatStatistic();
    });

    return {
      statistic
    }
  }
})
</script>