import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from "../utils/axiosInstance";
import CreateProject from './createProject';
import Menu from "./Menu";

const ProjectPage = ({ showMenu = true, showTitle = true, showCreateProject = true, fetchUrl = '/projects' }) => {
    const [projects, setProjects] = useState([]);
    const [hoveredProjectId, setHoveredProjectId] = useState(null);
    const [editingProjectId, setEditingProjectId] = useState(null);
    const [projectName, setProjectName] = useState('');
    const [projectDescription, setProjectDescription] = useState('');
    const history = useNavigate();

    useEffect(() => {
        fetchProjects(fetchUrl);
    }, [fetchUrl]);

    const fetchProjects = async (url) => {
        try {
            const response = await axiosInstance.get(url);
            setProjects(response.data);
        } catch (error) {
            console.error('Error fetching projects', error);
        }
    };
    const handleMouseEnter = (projectId) => {
        setHoveredProjectId(projectId);
    };

    const handleMouseLeave = () => {
        setHoveredProjectId(null);
    };

    const handleDeleteProject = async (projectId) => {
        try {
            await axiosInstance.delete(`/projects/delete/${projectId}`);
            fetchProjects(fetchUrl);
        } catch (error) {
            console.error('Error deleting project', error);
        }
    };

    const handleUpdateProject = async (projectId, currCreatedBy) => {
        try {
            const updatedProject = {
                nameProject: projectName,
                description: projectDescription,
                createdBy: currCreatedBy
            };
            const response = await axiosInstance.put(`/projects/${projectId}`, updatedProject);
            fetchProjects(fetchUrl);
            const updatedProjects = projects.map(project =>
                project.id === projectId ? response.data : project
            );
            setProjects(updatedProjects);
            setEditingProjectId(null);
        } catch (error) {
            console.error('Error updating project', error);
        }
    };

    const handleProjectClick = (projectId, event) => {
        if (event.target.tagName !== 'BUTTON' && event.target.tagName !== 'INPUT' && fetchUrl !== '/projects/admin') {
            history(`/project/${projectId}`);
        }
    };

    const handleEditProject = (project) => {
        setEditingProjectId(project.id);
        setProjectName(project.nameProject);
        setProjectDescription(project.description);
    };

    const handleCancelEdit = () => {
        setEditingProjectId(null);
        setProjectName('');
        setProjectDescription('');
    };
    return (
        <div className="container">
            {showMenu && <Menu />}
            {showTitle && <h2>Project Management</h2>}
            {showCreateProject && <CreateProject onProjectCreated={fetchProjects} />}
            <ul>
                {projects.map((project, index) => (
                    <li
                        key={project.id || index}
                        className="project-item"
                        onMouseEnter={() => handleMouseEnter(project.id)}
                        onMouseLeave={handleMouseLeave}
                        onClick={(event) => handleProjectClick(project.id, event)}
                    >
                        {editingProjectId === project.id ? (
                            <div>
                                <input
                                    type="text"
                                    value={projectName}
                                    onChange={(e) => setProjectName(e.target.value)}
                                    placeholder="Project Name"
                                    onClick={(e) => e.stopPropagation()}
                                />
                                <input
                                    type="text"
                                    value={projectDescription}
                                    onChange={(e) => setProjectDescription(e.target.value)}
                                    placeholder="Project Description"
                                    onClick={(e) => e.stopPropagation()}
                                />
                                <button onClick={() => handleUpdateProject(project.id, project.createdBy)}>Save</button>
                                <button onClick={handleCancelEdit}>Cancel</button>
                            </div>
                        ) : (
                            <div>
                                {project.nameProject}: {project.description}
                                {hoveredProjectId === project.id && (
                                    <div className="project-menu">
                                        <button onClick={() => handleDeleteProject(project.id)}>Delete</button>
                                        <button onClick={() => handleEditProject(project)}>Edit</button>
                                    </div>
                                )}
                            </div>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ProjectPage;