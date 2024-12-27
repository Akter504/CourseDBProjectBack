import React from 'react';
import { Link } from 'react-router-dom';

const Menu = ({ currentPage }) => {
    const token = localStorage.getItem('token');

    return (
        <div className="menu-container">
            {token ? (
                <>
                    <Link to="/projects" className="menu-item">Перейти к проектам</Link>
                    {currentPage !== 'profile' && (
                        <Link to="/profile" className="menu-item">Профиль</Link>
                    )}
                    <Link to="/" className="menu-item">Главная</Link>
                    <button className="menu-item" onClick={() => {
                        localStorage.removeItem('token');
                        window.location.reload();
                    }}>Выйти</button>
                </>
            ) : (
                <>
                    <Link to="/login" className="menu-item">Авторизоваться</Link>
                    <Link to="/register" className="menu-item">Зарегистрироваться</Link>
                </>
            )}
        </div>
    );
};

export default Menu;