<template>
<div>
  <navigation />
  <div id="register" class="text-center">
    <form class="form-register" @submit.prevent="register">

      <h1 id="register-title">Create Account</h1>

      <div id="redirect-login">
        <router-link :to="{ name: 'login' }">
          Already have an account?  Login here!
        </router-link>
      </div>
      <div class="alert alert-danger" role="alert" v-if="registrationErrors">
        There were problems registering this user.
      </div>
      <div id="whole-form">
      <div id="form-block">
      <div class="form-input">
        <label for="username" class="input-box">Username:  </label>
        <input
          type="text"
          id="username"
          class="form-control"
          placeholder="Username"
          v-model="user.username"
          required
          autofocus
        />
      </div>
      <br>

      <div class="form-input">
        <label for="password" class="input-box">Password:  </label>
        <input
          type="password"
          id="password"
          class="form-control"
          placeholder="Password"
          v-model="user.password"
          required
        />
      </div>
      <br>

      <div class="form-input">
        <label for="confirmPassword" class="input-box">Confirm Password:  </label>
        <input
          type="password"
          id="confirmPassword"
          class="form-control"
          placeholder="Confirm Password"
          v-model="user.confirmPassword"
          required
        />
      </div>
      <br>

      <div class="form-input">
        <button id="form-submit-button" type="submit">
          Create Account
        </button>
      </div>
      </div>
      </div>
    </form>
  </div>
  </div>
</template>

<script>
import Navigation from '@/component/Nav'

export default {
  name: 'register',
  data() {
    return {
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role: 'user',
      },
      registrationErrors: false,
    };
  },
  methods: {
    register() {
      fetch(`${process.env.VUE_APP_REMOTE_API}/register`, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.user),
      })
        .then((response) => {
          if (response.ok) {
            this.$router.push({ path: '/login', query: { registration: 'success' } });
          } else {
            this.registrationErrors = true;
          }
        })

        .then((err) => console.error(err));
    },
  },
  components:{
  Navigation
  }
};
</script>

<style>

  #register {
    
    margin: 32px;
    
  }

  #register-title {
    text-align: center;
    margin-bottom: 4px;
  }

  .form-register {
    border: 2px solid lightgray;
    margin: 0 25% 0 25%;
    background-color: rgb(238, 238, 248);
  }

  .form-register h1 {
    text-align: center;
  }

  .form-input {
    display: inline-block;
    margin: 8px 0px 8px 0px;
    text-align: center;
  }

  .input-box {
    display: inline-block;
    border-radius: 4px;
  }

  #redirect-login {
    text-align: center;
    font-size: 12px;
  }

  #form-block {
    display: inline-block;
    text-align: center;
    margin: 32px 0 32px 0;
  }
  #form-submit-button {
    
    border-radius: 4px;
    padding: 7px 20px;
    border: 2px solid rgb(181, 181, 252);
    background-color: rgb(203, 203, 250);
    color: black;
    margin: 6px 0 0 0;  
      
  }

  #form-submit-button:hover {
    display: inline-block;
    box-shadow: 0 12px 16px 0 rgb(80, 80, 250), 0 17px 50px 0 rgb(163, 163, 248);
  }

  #whole-form {
    text-align: center;
  }
</style>
