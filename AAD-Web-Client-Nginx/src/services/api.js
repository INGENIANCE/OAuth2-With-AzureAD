import axios from 'axios';

export default axios.create({
    baseURL: '/web-app',
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
 });