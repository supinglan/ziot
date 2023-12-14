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
  name: 'Echart1',
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
    car: {
      type: Number,
      required: true
    },
    wearableDevice: {
      type: Number,
      required: true
    },
    home: {
      type: Number,
      required: true
    },
    other: {
      type: Number,
      required: true
    }

  },
  data() {
    return {
      info:
        [
          { value: this.car, name: '车载设备' },
          { value: this.wearableDevice, name: '可穿戴设备' },
          { value: this.home, name: '智能家居' },
          { value: this.other, name: '其他设备' }
        ],
      chart1: null

    }
  },
  watch: {
    car(newValue, oldValue) {
      this.refresh()
    },
    wearableDevice(newValue, oldValue) {
      this.refresh()
    },
    home(newValue, oldValue) {
      this.refresh()
    },
    other(newValue, oldValue) {
      this.refresh()
    }
  },

  mounted() {
    this.drawLine()
  },
  methods: {
    drawLine() {
      this.chart1 = echarts.init(document.getElementById(this.id))
      this.refresh()
    },
    refresh() {
      this.info = []
      if (this.car !== 0) {
        this.info.push({ name: '车载设备', value: this.car })
      }
      if (this.wearableDevice !== 0) {
        this.info.push({ name: '可穿戴设备', value: this.wearableDevice })
      }
      if (this.home !== 0) {
        this.info.push({ name: '智能家居', value: this.home })
      }
      if (this.other !== 0) {
        this.info.push({ name: '其他设备', value: this.other })
      }
      var option = option = {
        title: {
          text: '物联网设备种类',
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
      this.chart1.setOption(option)
    }

  }
}
</script>
