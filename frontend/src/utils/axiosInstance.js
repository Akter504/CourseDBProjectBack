import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api/'
});

const isTokenValid = (token) => {
    if (!token) return false;
    try {
        const decodedToken = jwtDecode(token);
        const currentTime = Math.floor(Date.now() / 1000);
        return decodedToken.exp > currentTime;
    } catch (error) {
        console.error('Invalid token:', error);
        return false;
    }
};

axiosInstance.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        if (!isTokenValid(token)) {
            localStorage.removeItem('token');
            window.location.href = '/login';
            return Promise.reject(new Error('Token is invalid'));
        }
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});
export default axiosInstance;