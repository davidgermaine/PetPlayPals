<template>
<div>
  <navigation />
  <div id="login" class="text-center">
    <form class="form-signin" @submit.prevent="login">

      <h1 id="login-title">Sign In</h1>

      <div id="redirect-register">
        <router-link :to="{ name: 'register' }">
          Need an account?  Register here!
        </router-link>
      </div>
      <div class="alert alert-danger" role="alert" v-if="invalidCredentials">
        Invalid username and password!
      </div>

      <div class="alert alert-success" role="alert" v-if="this.$route.query.registration">
        Thank you for registering, please sign in.
      </div>
      <div id="whole-login-form">
      <div id="form-block">
        <div class="form-input">
      <label for="username" class="sr-only">Username:  </label>
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
      <label for="password" class="sr-only">Password:  </label>
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
      <button type="submit" id="signin-button">Sign in</button>
        </div>
      </div>
      </div>
    </form>
    
  </div>
  </div>
</template>

<script>
import auth from '../auth';
import Navigation from '@/component/Nav'

export default {
  name: 'login',
  components: {
    Navigation
  },
  data() {
    return {
      user: {
        username: '',
        password: '',
      },
      invalidCredentials: false,
    };
  },
  methods: {
    login() {
      fetch(`${process.env.VUE_APP_REMOTE_API}/login`, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.user),
      })
        .then((response) => {
          if (response.ok) {
            return response.text();
          } else {
            this.invalidCredentials = true;
          }
        })
        .then((token) => {
          if (token != undefined) {
            if (token.includes('"')) {
              token = token.replace(/"/g, '');
            }
            auth.saveToken(token);
            this.$router.push('/profile');
          }
        })
        .catch((err) => console.error(err));
    },

  },
};
</script>

<style>
  #login {
    margin: 32px;
  }

  #login-title {
    margin-bottom: 4px;
  }

  .form-signin {
    border: 2px solid lightgray;
    margin: 0 25% 0 25%;
    background-color: rgb(238, 238, 248);
  }

  .form-signin h1 {
    
    text-align: center;
  }

  .form-input {
    display: inline-block;
    margin: 8px 0px 8px 0px;
    text-align: center;
  }

  #redirect-register {
    text-align: center;
    font-size: 12px;
  }

  #form-block {
    display: inline-block;
    text-align: center;
    margin: 32px 0 32px 0;
  }

  .alert-danger{
    text-align: center;
  }
  #signin-button {
    border-radius: 4px;
    padding: 7px 20px;
    border: 2px solid rgb(181, 181, 252);
    background-color: rgb(203, 203, 250);
    color: black;
    margin: 6px 0 0 0;    
  }

  #signin-button:hover {
    box-shadow: 0 12px 16px 0 rgb(80, 80, 250), 0 17px 50px 0 rgb(163, 163, 248);
  }
  .form-input {
    margin: 8px 0px 8px 0px;
    text-align: center;
  }

  .input-box {
    border-radius: 4px;
  }
  #whole-login-form {
    text-align: center;
  }
</style>
