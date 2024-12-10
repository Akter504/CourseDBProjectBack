import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axiosInstance from "../utils/axiosInstance";
import CreateProject from './createProject';

const ProjectPage = () => {
    const [projects, setProjects] = useState([]);

    useEffect(() => {
        fetchProjects();
    }, []);

    const fetchProjects = async () => {
        try {
            const response = await axiosInstance.get('/projects');
            setProjects(response.data);
        } catch (error) {
            console.error('Error fetching projects', error);
        }
    };

    return (
        <div>
            <h1>Project Management</h1>
            <CreateProject onProjectCreated={fetchProjects} />
            <h2>Project List</h2>
            <ul>
                {projects.map((project) => (
                    <li key={project.id}>
                        <Link to={`/project/${project.id}`}>{project.nameProject}: {project.description}</Link>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ProjectPage;