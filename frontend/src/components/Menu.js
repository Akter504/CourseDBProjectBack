import React from 'react';
import { Link } from 'react-router-dom';

const Menu = ({ currentPage }) => {
    const token = localStorage.getItem('token');

    return (
        <div className="menu-container">
            {token ? (
                <>
                    <Link to="/projects" className="menu-item">Go to Projects</Link>
                    {currentPage !== 'profile' && (
                        <Link to="/profile" className="menu-item">Profile</Link>
                    )}
                    <Link to="/" className="menu-item">HomePage</Link>
                    <button className="menu-item" onClick={() => {
                        localStorage.removeItem('token');
                        window.location.reload();
                    }}>Выйти</button>
                </>
            ) : (
                <>
                    <Link to="/login" className="menu-item">Login</Link>
                    <Link to="/register" className="menu-item">Register</Link>
                </>
            )}
        </div>
    );
};

export default Menu;