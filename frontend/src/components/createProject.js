import React, {useEffect, useState} from 'react';
import axiosInstance from "../utils/axiosInstance";

const CreateProject = () => {
    const [projects, setProjects] = useState([]);
    const [projectName, setProjectName] = useState('');
    const [projectDescription, setProjectDescription] = useState('');

    useEffect(() => {
        fetchProjects();
    }, []);

    const fetchProjects = async () => {
        try {
            const response = await axiosInstance.get('/projects');
            setProjects(response.data);
        } catch (error) {
            console.error('Error can`t get projects.', error);
        }

    };

    const handleCreateProject = async () => {
        try {
            await axiosInstance.post('/projects/new', {
                nameProject: projectName,
                description: projectDescription,
            });

            window.location.reload();
        } catch (error) {
            console.error('Error can`t create project.', error);
        }
    };

    return (
        <div>
            <h2>Create Project</h2>
            <input
                type="text"
                value={projectName}
                onChange={(e) => setProjectName(e.target.value)}
                placeholder="Project Name"
            />
            <input
                type="text"
                value={projectDescription}
                onChange={(e) => setProjectDescription(e.target.value)}
                placeholder="Project Description"
            />
            <button onClick={handleCreateProject}>Create Project</button>

        </div>
    );
};

export default CreateProject;