import React from 'react';
import Menu from './Menu';

const HomePage = () => {
    return (
        <div className="welcome-container">
            <h1 className="welcome-message">Добро пожаловать в ToDo лист!</h1>
            <p className="welcome-text">Пожалуйста, авторизуйтесь или зарегистрируйтесь, чтобы начать.</p>
            <Menu />
        </div>
    );
};

export default HomePage;