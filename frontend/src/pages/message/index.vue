<template>
  <div class="app-container">
    <div style="display: flex; justify-content: center; align-items: center; margin-bottom:30px;margin-top:10px;">
      <div style="display: flex; flex-direction: row; align-items: center; margin-top: 10px;">
        <el-input v-model="input" placeholder="请输入内容" class="input-with-select">
          <el-select slot="prepend" v-model="select" style="width: 120px;" placeholder="请选择">
            <el-option label="消息内容" :value="1" />
            <el-option label="消息数据" :value="2" />
            <el-option label="消息状态" :value="3" />
          </el-select>
          <el-button slot="append" icon="el-icon-search" @click="handleSearch" />
        </el-input>
      </div>
    </div>
    <el-table
      :data="tableData"
      border
      stripe
      :default-sort="{prop: 'time', order: 'descending'}"
      style="width: 100% "
    >
      <el-table-column
        prop="name"
        label="设备名称"
        width="120"
        align="center"
      />
      <el-table-column
        prop="info"
        label="消息内容"
        sortable
        align="center"
      />
      <el-table-column
        prop="value"
        label="消息数据"
        width="120"
        sortable
        align="center"
      />
      <el-table-column
        prop="alert"
        label="消息状态"
        width="120"
        :formatter="getStateText"
        sortable
        align="center"
      />
      <el-table-column
        prop="time"
        label="消息时间"
        width="160"
        sortable
        align="center"
      />
    </el-table></div>
</template>

<script>
import request from '@/utils/request'
export default {
  data() {
    return {
      input: '',
      list: [],
      tableData: [],
      select: null
    }
  },
  computed: {
    getStateText() {
      return (row) => {
        switch (row.alert) {
          case 0:
            return '正常'
          case 1:
            return '告警'
          default:
            return '未知状态'
        }
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    handleSearch() {
      // 根据不同的搜索类型执行不同的搜索逻辑
      if (this.select === 1) {
        this.tableData = this.list.filter(item => item.info.includes(this.input))
      } else if (this.select === 2) {
        this.tableData = this.tableData = this.list.filter(item => item.value.toString() === this.input)
      } else if (this.select === 3) {
        const stateMap = {
          '正常': 0,
          '告警': 1
        }
        const mappedState = stateMap[this.input]
        this.tableData = this.list.filter(item => item.alert === mappedState)
      }
    },
    fetchData() {
      const id = this.$route.params.id
      this.listLoading = true
      if (id !== ':id') {
        request({
          url: '/device/getMessages',
          method: 'Get',
          params: { id }
        })
          .then(response => {
            this.list = response
            this.tableData = this.list
          })
          .catch(error => {
            console.error('Error fetching data from backend:', error)
          })
      }
      this.listLoading = false
    },
    rowStyle() {
      return 'text-align:center'
    }
  }
}
</script>
