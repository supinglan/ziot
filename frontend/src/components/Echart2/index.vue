<template>
  <div>
    <div :id="id" :style="{ height: height, width: width }" class="chart-container" />
  </div>
</template>
<script>
const echarts = require('echarts/lib/echarts')
require('echarts/lib/component/tooltip')
require('echarts/lib/component/title')
require('echarts/lib/component/grid')
import 'echarts/lib/component/grid'
export default {
  name: 'Echart2',
  props: {
    height: {
      type: String,
      default: '500px'
    },
    width: {
      type: String,
      default: '500px'
    },
    id: {
      type: String,
      default: 'demo'
    },
    good: {
      type: Number,
      required: true
    },
    alert: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      info:
        [
          { value: this.good, name: '正常设备' },
          { value: this.alert, name: '告警设备' }
        ],
      chart2: null

    }
  },
  watch: {
    good(newValue, oldValue) {
      this.refresh()
    },
    alert(newValue, oldValue) {
      this.refresh()
    }
  },
  mounted() {
    this.drawLine()
  },
  methods: {
    drawLine() {
      this.chart2 = echarts.init(document.getElementById(this.id))
      this.refresh()
    },
    refresh() {
      this.info = []
      if (this.good !== 0) {
        this.info.push({ name: '正常设备', value: this.good })
      }
      if (this.alert !== 0) {
        this.info.push({ name: '告警设备', value: this.alert })
      }
      var option = option = {
        title: {
          text: '设备状态',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '',
            type: 'pie',
            radius: '50%',
            data: this.info,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      // 绘制图表
      this.chart2.setOption(option)
    }
  }
}
</script>

