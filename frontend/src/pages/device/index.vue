<template>
  <div>
    <div style="display: flex; justify-content: center; align-items: center; margin-bottom:30px;margin-top:30px;">
      <div style="display: flex; flex-direction: row; align-items: center; margin-top: 15px;">
        <el-input v-model="input" placeholder="请输入内容" class="input-with-select">
          <el-select slot="prepend" v-model="select" style="width: 105px;" placeholder="请选择">
            <el-option label="设备名称" :value="1" />
            <el-option label="设备类型" :value="2" />
            <el-option label="设备状态" :value="3" />
          </el-select>
          <el-button slot="append" icon="el-icon-search" @click="handleSearch" />
        </el-input>
        <el-button type="primary" style=" margin-left:20px" @click="dialogFormVisible = true">添加新设备</el-button>
      </div>

      <el-dialog title="设备信息" :visible.sync="dialogFormVisible" :width="width">
        <el-form ref="form" :model="form" :rules="rules">
          <el-form-item label="设备名称" :label-width="formLabelWidth" prop="name">
            <el-input v-model="form.name" autocomplete="off" />
          </el-form-item>
          <el-form-item label="设备类型" :label-width="formLabelWidth" prop="type">
            <el-select v-model="form.type" placeholder="请选择设备类型">
              <el-option label="车载设备" :value="1" />
              <el-option label="智能家居" :value="2" />
              <el-option label="可穿戴设备" :value="3" />
              <el-option label="其他设备" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item label="设备描述" :label-width="formLabelWidth">
            <el-input v-model="form.description" autocomplete="off" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </el-dialog></div>
    <el-dialog title="设备信息" :visible.sync="editformVisible" :width="width">
      <el-form ref="editform" :model="editform" :rules="rules">
        <el-form-item label="设备名称" :label-width="formLabelWidth" prop="name">
          <el-input v-model="editform.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="设备类型" :label-width="formLabelWidth" prop="type">
          <el-select v-model="editform.type" placeholder="请选择设备类型">
            <el-option label="车载设备" :value="1" />
            <el-option label="智能家居" :value="2" />
            <el-option label="可穿戴设备" :value="3" />
            <el-option label="其他设备" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备描述" :label-width="formLabelWidth">
          <el-input v-model="editform.description" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editformVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitEditForm">确 定</el-button>
      </div>
    </el-dialog>
    <el-table
      :data="tableData"
      border
      stripe
      :default-sort="{prop: 'id', order: 'ascending'}"
      style="width: 100% "
      :cell-style="rowStyle"
    >
      <el-table-column
        prop="id"
        label="设备号"
        width="100"
        align="center"
        sortable
      />

      <el-table-column
        prop="name"
        label="设备名称"
        width="120"
        align="center"
        sortable
      />
      <el-table-column
        prop="type"
        label="设备类型"
        width="120"
        :formatter="getTypeText"
        align="center"
        sortable
      />
      <el-table-column
        prop="state"
        label="设备状态"
        width="120"
        :formatter="getStateText"
        align="center"
        sortable
      />
      <el-table-column
        prop="activateTime"
        label="上次激活时间"
        :formatter="formatTime"
        align="center"
        sortable
      />
      <el-table-column
        prop="description"
        label="设备描述"
        align="center"
      />
      <el-table-column
        label="操作"
        width="300"
        align="center"
      >
        <template slot-scope="scope">
          <!-- 编辑按钮 -->
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>

          <!-- 查看信息按钮 -->
          <el-button type="primary" size="mini" @click="handleViewInfo(scope.row)">查看信息</el-button>
          <el-button type="primary" size="mini" @click="handleLocation(scope.row)">查看位置</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import request from '@/utils/request'
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      input: '',
      select: null,
      list: [],
      tableData: [],
      dialogFormVisible: false,
      editformVisible: false,
      form: {
      },
      rules: {
        name: [
          { required: true, message: '设备名称不能为空', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '设备类型不能为空', trigger: 'change' }
        ]
      },
      editform: {
        name: '',
        type: 0,
        description: ''
      },
      formLabelWidth: '120px',
      width: '50%'
    }
  },
  computed: {
    ...mapGetters([
      'id'
    ]),
    getTypeText() {
      return (row) => {
        switch (row.type) {
          case 1:
            return '车载设备'
          case 2:
            return '智能家居'
          case 3:
            return '可穿戴设备'
          default:
            return '其他设备'
        }
      }
    },
    getStateText() {
      return (row) => {
        switch (row.state) {
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
  destroyed() {
    // 在组件销毁前移除监听事件
    window.removeEventListener('resize', this.handleResize)
  },
  created() {
    this.fetchData()
    window.addEventListener('resize', this.handleResize)
    this.handleResize()
  },
  methods: {
    handleSearch() {
      // 根据不同的搜索类型执行不同的搜索逻辑
      if (this.select === 1) {
        this.tableData = this.list.filter(item => item.name.includes(this.input))
      } else if (this.select === 2) {
        const typeMap = {
          '车载设备': 1,
          '智能家居': 2,
          '可穿戴设备': 3,
          '其他设备': 4
        }
        const mappedType = typeMap[this.input]
        this.tableData = this.list.filter(item => item.type === mappedType)
      } else if (this.select === 3) {
        const stateMap = {
          '正常': 0,
          '告警': 1
        }
        const mappedState = stateMap[this.input]
        this.tableData = this.list.filter(item => item.state === mappedState)
      }
    },
    submitForm() {
      // Assuming you're using Vue Resource or Axios for HTTP requests
      // Replace 'axios.post' with your preferred HTTP request method
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.form.id = this.id
          request({
            url: '/device/add',
            method: 'post',
            data: this.form
          })
            .then(response => {
              this.dialogFormVisible = false
              this.fetchData()
            })
            .catch(error => {
              // Handle error if the request fails
              console.error('Error sending form data:', error)
              // You can also handle error feedback to the user
            })
        } else {
          console.log('error submit!!')
        }
      })
    },
    fetchData() {
      const id = this.id
      request({
        url: '/device/getList',
        method: 'get',
        params: { id }
      })
        .then(response => {
          this.list = response
          this.tableData = this.list
        })
        .catch(error => {
          console.error('Error fetching data from backend:', error)
        })
    },
    handleEdit(rowData) {
      // 将编辑的行数据赋值给表单的数据对象 editform
      this.editform.name = rowData.name
      this.editform.type = rowData.type
      this.editform.description = rowData.description
      this.editform.deviceid = rowData.id
      this.editingRowData = rowData
      // 显示编辑对话框
      this.editformVisible = true
    },
    submitEditForm() {
      this.$refs.editform.validate((valid) => {
        if (valid) {
          request({
            url: '/device/modify',
            method: 'post',
            data: this.editform
          })
            .then(response => {
              this.editformVisible = false // Close the dialog after successful submission
              this.editingRowData.name = this.editform.name
              this.editingRowData.type = this.editform.type
              this.editingRowData.description = this.editform.description
            })
            .catch(error => {
              // Handle error if the request fails
              console.error('Error sending form data:', error)
              // You can also handle error feedback to the user
            })
        } else {
          console.log('error submit!!')
        }
      }
      )
    },
    handleViewInfo(rowData) {
    // 点击查看信息后，跳转到带有对应id的信息页面
      this.$router.push({ name: 'info', params: { id: rowData.id }})
    },
    handleLocation(rowData) {
      this.$router.push({ name: 'location', params: { id: rowData.id }})
    },
    formatTime(row, column) {
      if (row.activateTime === null) {
        return '未激活'
      }
      const date = new Date(row.activateTime)
      return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`
    },
    rowStyle() {
      return 'text-align:center'
    },
    handleResize() {
      const isMobile = window.innerWidth <= 768 // 假设小于等于 768px 为移动设备
      if (isMobile) {
        this.width = '80%' // 根据是否是移动设备设置宽度
      }
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}
.el-select .el-input {
    width: 130px;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
  /* 你的组件样式中 */

</style>

