import {defineStore} from "pinia";
import axios from "axios";

export const userStore= defineStore('general',{
    state:()=>{
        return{
            user:{
                username:'',
                email:'',
                role:'',
                avatar:'',
                registerTime:null
            }
        }
    },getters:{//请求获取minio中图片
        avatarUrl(){
            if(this.user.avatar){
                return `${axios.defaults.baseURL}/images${this.user.avatar}`
            }
            else
                return "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        }
    }
})