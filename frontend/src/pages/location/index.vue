<template>
  <div class="home-container">
    <div class="amap-title">
      <h2>{{ device.name }}的设备轨迹</h2>
    </div>
    <div class="amap-wrapper">
      <el-amap
        ref="amap"
        :zoom="10"
        :center="calculateCenter()"
        style="width: 100%; height: 500px;"
      >
        <el-amap-marker v-for="(marker, index) in markers" :key="'m'+index" :position="marker.position" :content="marker.content" />
        <el-amap-polyline ref="polyline" :path="getPolylinePath()" :stroke-color="'#009933'" :stroke-weight="3" :stroke-opacity="1" />
      </el-amap>
    </div>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import request from '@/utils/request'
export default {
  name: 'Location',
  data() {
    return {
      list: [],
      center: [119.9, 30.1],
      zoom: 13,
      markers: [
      ],

      device: {
        name: ''
      }
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  mounted() {
    this.fetchData()
    const id = this.$route.params.id
    if (id !== ':id') {
      request({
        url: '/device/getInfo',
        method: 'Get',
        params: { id }
      })
        .then(response => {
          this.device = response
        })
        .catch(error => {
          console.error('Error fetching data from backend:', error)
        })
    }
  },
  methods: {
    fetchData() {
      const id = this.$route.params.id

      const blueContent = '' +
      '<div class="custom-content-marker">' +
      '   <img src="http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png">' +
        '</div>'
      const redContent = '' +
      '<div class="custom-content-marker">' +
      '   <img src="http://webapi.amap.com/theme/v1.3/markers/n/mark_r.png">' +
      '</div>'
      if (id !== ':id') {
        request({
          url: '/device/getLatestMessages',
          method: 'Get',
          params: { id }
        })
          .then(response => {
            this.list = response
            this.markers.splice(0, this.markers.length)
            this.list.forEach(item => {
              if (item.alert === 0) {
                this.markers.push({ position: [item.lng, item.lat], content: blueContent })
              } else {
                this.markers.push({ position: [item.lng, item.lat], content: redContent })
              }
            })
          })
          .catch(error => {
            console.error('Error fetching data from backend:', error)
          })
      }
    }, calculateCenter() {
      if (this.markers.length === 0) {
        // 如果没有标记点，则使用默认中心点
        return this.center
      }
      // 计算所有点的平均纬度和平均经度
      let totalLat = 0
      let totalLng = 0

      this.markers.forEach(marker => {
        totalLng += marker.position[0]
        totalLat += marker.position[1]
      })

      const avgLat = totalLat / this.markers.length
      const avgLng = totalLng / this.markers.length
      // 返回平均位置作为地图中心点
      return [avgLng, avgLat]
    },
    getPolylinePath() {
      // 构造折线路径数组
      return this.list.map(item => [item.lng, item.lat])
    }
  }
}

</script>

<style lang="scss" scoped>

.home-container{
  display: flex;
  height: 600px;
  height: 87vh;
  align-items: center;
  justify-content: center;
}
.amap-wrapper {
  width: 500px;
  height: 500px;

}
.home {
  &-container {
    margin: 20px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
.amap-title {
  text-align: center;
  position: absolute;
  top: 20px;
  left: 0;
  right: 0;
  z-index: 999;
}
.custom-content-marker {
  position: relative;
  width: 25px;
  height: 34px;
}

.custom-content-marker img {
  width: 100%;
  height: 100%;
}

</style>
