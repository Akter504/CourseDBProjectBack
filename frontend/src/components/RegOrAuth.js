import React from 'react';
import { Link } from 'react-router-dom';

const RegOrAuth = () => {
    return (
        <div>
            <h2>Welcome! Please choose an action:</h2>
            <div>
                {/* Кнопка для перехода на страницу входа */}
                <Link to="/login">
                    <button>Login</button>
                </Link>
            </div>
            <div>
                {/* Кнопка для перехода на страницу регистрации */}
                <Link to="/register">
                    <button>Register</button>
                </Link>
            </div>
        </div>
    );
};

export default RegOrAuth;
