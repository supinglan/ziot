<template>
  <div class="register-container">
    <div class="title-container" style="margin-top: 70px ">
      <h1 class="title" style="font-size: 70px; margin-bottom: 10px; color:white;">ZIOT</h1>
      <h3 class="title" style="color:white;">万物互联 万象新生</h3>
    </div>
    <el-form
      ref="registerForm"
      :model="registerForm"
      :rules="registerRules"
      class="ziot-form"
      auto-complete="on"
      label-position="left"
    >

      <div class="title-container">
        <h3 class="title">注册</h3>
      </div>
      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="registerForm.username"
          placeholder="请输入用户名"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>
      <el-form-item prop="email">
        <span class="svg-container">
          <svg-icon icon-class="email" />
        </span>
        <el-input
          ref="email"
          v-model="registerForm.email"
          placeholder="请输入电子邮件地址"
          name="email"
          type="text"
          tabindex="2"
          auto-complete="on"
        />
      </el-form-item>
      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="registerForm.password"
          :type="passwordType"
          placeholder="请输入密码"
          name="password"
          tabindex="3"
          auto-complete="on"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon
            :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"
          />
        </span>
      </el-form-item>
      <el-form-item prop="checkPassword">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="checkPasswordType"
          ref="checkPassword"
          v-model="registerForm.checkPassword"
          :type="checkPasswordType"
          placeholder="请确认密码"
          name="checkPassword"
          tabindex="4"
          auto-complete="on"
        />
        <span class="show-pwd" @click="showCheckPwd">
          <svg-icon
            :icon-class="checkPasswordType === 'password' ? 'eye' : 'eye-open'"
          />
        </span>
      </el-form-item>
      <el-container style="align-items: center;display: flex;justify-content: center;">
        <el-button
          :loading="loading"
          type="primary"
          style="width: 30%; margin-bottom: 30px"
          @click.native.prevent="handleregister"
        >注册</el-button>
      </el-container>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data() {
    var validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (value.length < 8) {
        callback(new Error('密码长度必须大于等于8位'))
      } else {
        this.$refs.registerForm.validateField('checkPassword')

        callback()
      }
    }
    var validateCheckPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      registerForm: {
        username: '',
        email: '',
        password: '',
        checkPassword: ''
      },
      registerRules: {
        username: { required: true, message: '请输入用户名', trigger: 'blur' },
        email: [
          { required: true, message: '请输入电子邮件地址', trigger: 'blur' },
          {
            type: 'email',
            message: '请输入正确的电子邮件地址',
            trigger: ['blur']
          }
        ],
        password: [
          { validator: validatePassword, trigger: 'blur' }
        ],
        checkPassword: [{ validator: validateCheckPassword, trigger: 'blur' }]
      },
      loading: false,
      passwordType: 'password',
      checkPasswordType: 'password'
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    showCheckPwd() {
      if (this.checkPasswordType === 'password') {
        this.checkPasswordType = ''
      } else {
        this.checkPasswordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.checkPassword.focus()
      })
    },
    handleregister() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          this.loading = true
          this.$store
            .dispatch('user/register', this.registerForm)
            .then(() => {
              this.$router.push('/login')
              this.loading = false
            })
            .catch(() => {
              this.loading = false
            })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
$font: #2d3a4b;
$cursor: #fff;
$light_gray: #eee;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .register-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.register-container {
  .el-input {
    z-index: 1;
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $font;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px white inset !important;
        -webkit-text-fill-color: $font !important;
      }
    }
  }

  .el-form-item {
    z-index: 1;
    border: 1px solid $light_gray;
    background: white;
    border-radius: 5px;
    color: $font;
  }
}
</style>

<style lang="scss" scoped>
$dark_gray: #889aa4;
$font: #2d3a4b;
.register-container {
  background-image: url("../../assets/background.jpg");
  background-size: cover;
  height: 100%;
  width: 100%;
  overflow: hidden;

  .ziot-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 20px 20px 0;
    margin: 0 auto;
    overflow: hidden;
    background-color: white;
    border: 2px solid white;
    border-radius: 8px
  }

  .logo-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100px;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $font;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
