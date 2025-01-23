import axios from 'axios';

export const API_URL = 'http://localhost:8080/api/auth';

export const registerUser = async (userData) => {
    try {
        const response = await axios.post(`${API_URL}/register`, userData);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const loginUser = async (loginData) => {
    try {
        const response = await axios.post(`${API_URL}/login`, loginData);
        return response.data;
    } catch (error) {
        console.error('Ошибка при входе', error);
        throw error;
    }
};

