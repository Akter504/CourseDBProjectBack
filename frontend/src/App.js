import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Register from './components/register';
import Login from './components/login';
import ProjectPage from "./components/projectPage";
import PrivateRoute from "./components/PrivateRoute";
import TaskPage from "./components/taskPage";
import './styles/styles.css';
import HomePage from "./components/HomePage";
import UserProfile from "./components/UserProfile";
import AdminPanel from './components/AdminPanel';


const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login />} />
                <Route path="/projects" element={<PrivateRoute element={ProjectPage} />} />
                <Route path="/project/:id" element={<PrivateRoute element={TaskPage}  />} />
                <Route path="/profile" element={<PrivateRoute element={UserProfile}  />} />
                <Route path="/admin" element={<AdminPanel />} />
            </Routes>
        </Router>
    );
};

export default App;

