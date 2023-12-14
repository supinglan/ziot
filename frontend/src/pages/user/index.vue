<template>
  <div class="app-container">
    <el-descriptions class="margin-top" title="个人信息" :column="column" border :content-style="CS" :label-style="LS ">
      <el-descriptions-item style="display: contents;">
        <template slot="label">
          <i class="el-icon-user" />
          用户名
        </template>
        {{ form.name }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets" />
          性别
        </template>
        {{ form.sex }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-message" />
          电子邮箱
        </template>
        {{ form.email }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone" />
          手机号
        </template>
        {{ form.phone }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline" />
          居住地
        </template>
        {{ form.location }}
      </el-descriptions-item>

      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building" />
          联系地址
        </template>
        {{ form.address }}
      </el-descriptions-item>
    </el-descriptions>
    <div style="display: flex; justify-content: center; align-items: center; " @click="edit">
      <el-button type="primary" size="small" style="margin-top: 30px;">修改个人信息</el-button>
    </div>
    <el-dialog title="个人信息" :visible.sync="dialogFormVisible" :width="width">
      <el-form ref="editform" :model="editform" :rules="rules">
        <el-form-item label="用户名" :label-width="formLabelWidth">
          <el-input v-model="editform.name" autocomplete="off" :disabled="true" />
        </el-form-item>
        <el-form-item label="性别" :label-width="formLabelWidth">
          <el-select v-model="editform.sex" placeholder="请选择性别">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
            <el-option label="保密" value="保密" />
          </el-select>
        </el-form-item>
        <el-form-item label="电子邮箱" :label-width="formLabelWidth">
          <el-input v-model="editform.email" autocomplete="off" :disabled="true" />
        </el-form-item>
        <el-form-item label="手机号" :label-width="formLabelWidth" prop="phone">
          <el-input v-model="editform.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="居住地" :label-width="formLabelWidth">
          <el-input v-model="editform.location" autocomplete="off" />
        </el-form-item>
        <el-form-item label="联系地址" :label-width="formLabelWidth">
          <el-input v-model="editform.address" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import request from '@/utils/request'
export default {
  data() {
    return {
      form: {
        name: '',
        sex: '保密',
        email: '',
        phone: '',
        location: '',
        address: '',
        id: -1
      },
      editform: {
        name: '',
        sex: '保密',
        email: '',
        phone: '',
        location: '',
        address: '',
        id: -1
      },
      rules: {
        phone: [
          { validator: this.validatePhone, message: '请输入正确的手机号', trigger: ['blur', 'change'] }
        ]
      },
      dialogFormVisible: false,
      formLabelWidth: '80px',
      column: 2,
      width: '50%',
      CS: {
        'text-align': 'center', // 文本居中
        'min-width': '250px', // 最小宽度
        'word-break': 'break-all' // 过长时自动换行
      },
      LS: {
        'color': '#000',
        'text-align': 'center',
        'font-weight': '600',
        'height': '40px',
        'min-width': '110px',
        'word-break': 'keep-all'
      }
    }
  },
  computed: {
    ...mapGetters([
      'id'
    ])
  },
  destroyed() {
    // 在组件销毁前移除监听事件
    window.removeEventListener('resize', this.handleResize)
  },
  created() {
    this.fetchdata()
    window.addEventListener('resize', this.handleResize)
    this.handleResize()
  },
  methods: {
    submitForm() {
      this.$refs.editform.validate((valid) => {
        if (valid) {
          request({
            url: '/user/modify',
            method: 'post',
            data: this.editform
          })
            .then(response => {
              this.form = { ...this.editform }
              this.dialogFormVisible = false
            })
            .catch(error => {
              console.error('Error sending form data:', error)
            })
        } else {
          console.log('error submit!!')
        }
      })
    },
    validatePhone(rule, value, callback) {
      const phoneRegex = /^1\d{10}$/ // Regular expression to match a valid phone number
      if (!value) {
        callback()
      } else if (!phoneRegex.test(value)) {
        callback(new Error('请输入正确的手机号'))
      } else {
        callback() // Pass the validation
      }
    },
    cancel() {
      this.dialogFormVisible = false
      this.fetchdata()
    },
    fetchdata() {
      const id = this.id
      this.form.id = this.id
      // 发送请求从后端获取数据
      request({
        url: '/user/getInfo',
        method: 'get',
        params: { id }
      })
        .then(response => {
          this.form.name = response.username
          this.form.sex = response.sex
          this.form.email = response.email
          this.form.phone = response.phone
          this.form.location = response.location
          this.form.address = response.address
        })
        .catch(error => {
          console.error('Error fetching data from backend:', error)
        })
    },
    edit() {
      this.editform = { ...this.form }
      this.dialogFormVisible = true
    },
    handleResize() {
      const isMobile = window.innerWidth <= 768 // 假设小于等于 768px 为移动设备
      if (isMobile) {
        this.column = 1
        this.width = '80%'
      }
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}

</style>

