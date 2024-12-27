import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Register from './components/register';
import Login from './components/login';
import RegOrAuth from './components/RegOrAuth';
import ProjectPage from "./components/projectPage";
import PrivateRoute from "./components/PrivateRoute";
import {Switch} from "@mui/material";
import CreateProject from "./components/createProject";
import TaskPage from "./components/taskPage";

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/register" element={<Register />} />
                <Route path="/login" element={<Login />} />
                <Route path="/projects" element={<PrivateRoute element={ProjectPage} />} />
                <Route path="/project/:id" element={<TaskPage />} />
                <Route path="/" element={<CreateProject />} />
            </Routes>
        </Router>
    );
};

export default App;

