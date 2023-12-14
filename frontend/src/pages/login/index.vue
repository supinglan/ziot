<template>
  <div class="login-container">
    <div class="title-container" style="margin-top: 70px ">
      <h1 class="title" style="font-size: 70px; margin-bottom: 10px; color:white;">ZIOT</h1>
      <h3 class="title" style="color:white;">万物互联 万象新生</h3>
    </div>
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
      auto-complete="on"
      label-position="left"
    >

      <div class="title-container">
        <h3 class="title" style="color: $black;">登录</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="用户名"
          name="username"
          type="text"
          tabindex="1"
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
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="密码"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>
      <el-container style="align-items: center;display: flex;justify-content: center;">
        <el-button
          :loading="loading"
          type="primary"
          style="width:30%;margin-bottom:30px;margin-right:60px;"
          @click.native.prevent="handleLogin"
        >登录</el-button>
        <el-button
          :loading="loading"
          style="width:30%;margin-bottom:30px;"
          @click.native.prevent="redirectRegister"
        >注册</el-button>
      </el-container>
    </el-form>
  </div>
</template>

<script>

export default {

  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (value.trim() === '') {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 8) {
        callback(new Error('请输入正确的密码'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
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
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm)
            .then(() => {
              this.$router.push({ path: this.redirect || '/' })
              this.loading = false
            })
            .catch(() => {
              console.error('Login failed')
              this.loading = false
            })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    redirectRegister() {
      this.loading = true
      this.$router.push('/register')
      this.loading = false
    }
  }
}

</script>

<style lang="scss">
$font: #2d3a4b;
$cursor: #fff;
$light_gray: #eee;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {

  .el-input {
    z-index: 1;
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: black;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px white  inset !important;
        -webkit-text-fill-color: black !important;
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
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;
$font: #2d3a4b;
.login-container {
  background-image: url('../../assets/background.jpg');
  background-size: cover;
  height: 100%;
  width: 100%;
  overflow: hidden;

  .login-form {
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
    margin-top: 10px;

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
