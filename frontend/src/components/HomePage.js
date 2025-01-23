import React from 'react';
import Menu from './Menu';

const HomePage = () => {
    return (
        <div className="welcome-container">
            <h1 className="welcome-message">Welcome to the ToDo list!</h1>
            <p className="welcome-text">Please login or register to get started..</p>
            <Menu />
        </div>
    );
};

export default HomePage;