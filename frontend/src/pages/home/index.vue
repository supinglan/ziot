<template>
  <div class="home-container">
    <div class="home-text">{{ name }},欢迎您!</div>
    <div style="margin-top: 30px; margin-bottom: 80px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <div>
            <el-statistic
              group-separator=","
              :value="totalDevice"
              title="设备总数"
            />
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-statistic
              group-separator=","
              :value="onlineDevice"
              title="在线设备数"
            />
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-statistic
              group-separator=","
              :value="message"
              title="消息总数"
            />
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-statistic
              group-separator=","
              :value="todayMessage"
              title="今日消息数"
            />
          </div>
        </el-col>

      </el-row>
    </div>
    <div class="horizontal-container">
      <echart-1
        id="1"
        :width="width"
        :height="height"
        :car="car"
        :wearable-device="wearableDevice"
        :home="home"
        :other="other"
      />
      <echart-2
        id="2"
        :width="width"
        :height="height"
        :good="good"
        :alert="alert"
      />
    </div>
  </div></template>

<script>
import { mapGetters } from 'vuex'
import echart1 from '@/components/Echart1/index.vue'
import echart2 from '@/components/Echart2/index.vue'

import request from '@/utils/request'
export default {
  name: 'Home',
  components: {
    echart1,
    echart2
  },
  data() {
    return {
      totalDevice: 1314,
      onlineDevice: 520,
      message: 123,
      todayMessage: 456,
      car: 0,
      wearableDevice: 0,
      home: 0,
      other: 0,
      good: 0,
      alert: 0,
      width: '550px',
      height: '450px'

    }
  },
  computed: {
    ...mapGetters([
      'name',
      'id'
    ])
  },
  destroyed() {
    // 在组件销毁前移除监听事件
    window.removeEventListener('resize', this.handleResize)
  },
  created() {
    const id = this.id
    request({
      url: '/device/getStat',
      method: 'get',
      params: { id }
    })
      .then(response => {
        this.totalDevice = response.total
        this.onlineDevice = response.online
        this.message = response.messages
        this.todayMessage = response.todayMessages
        this.car = response.car
        this.wearableDevice = response.wearableDevice
        this.home = response.home
        this.other = response.other
        this.alert = response.alert
        this.good = response.good
      })
      .catch(error => {
        console.error('Error', error)
      })
    window.addEventListener('resize', this.handleResize)
    this.handleResize() // 初始化时调用一次以确保在不同设备上尺寸正确
  },
  methods: {
    handleResize() {
      const isMobile = window.innerWidth <= 768 // 假设小于等于 768px 为移动设备
      if (isMobile) {
        this.width = '380px' // 根据是否是移动设备设置宽度
        this.height = '300px' // 根据是否是移动设备设置高度
      }
    }
  }
}

</script>

<style lang="scss" scoped>
.home {
  &-container {
    margin: 20px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
.like {
    cursor: pointer;
    font-size: 25px;
    display: inline-block;
  }
  /* 默认情况下是水平排列 */
.horizontal-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
}

/* 在小屏幕上（移动设备）切换为垂直排列 */
@media (max-width: 768px) {
  .horizontal-container {
    flex-direction: column;
  align-items: center;
  }

  /* 调整子元素宽度 */
  .horizontal-container echart-1,
  .horizontal-container echart-2 {
    width: 100%;
  }
}
@media (max-width: 768px) {
  .horizontal-container echart-1,
  .horizontal-container echart-2 {
    width: 50%;
  }
}

</style>
