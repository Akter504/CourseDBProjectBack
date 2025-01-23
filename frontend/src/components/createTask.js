import React, {useEffect, useState} from 'react';
import axiosInstance from "../utils/axiosInstance";
import {Link} from "react-router-dom";

const CreateTask = ({ projectId }) => {
    const [taskName, setTaskName] = useState('');
    const [taskDescription, setTaskDescription] = useState('');

    const handleCreateTask = async() => {
        try {
            const response = await axiosInstance.post(`/tasks/new/${projectId}`, {
                nameTask: taskName,
                description: taskDescription,
            });
            window.location.reload();
            console.log(response.data);
        } catch (error) {
            console.error('Ошибка при создании задачи', error);
        }
    }
    return (
        <div>
            <h2>Create Task</h2>
            <input
                type="text"
                value={taskName}
                onChange={(e) => setTaskName(e.target.value)}
                placeholder="Task Name"
            />
            <input
                type="text"
                value={taskDescription}
                onChange={(e) => setTaskDescription(e.target.value)}
                placeholder="Task Description"
            />
            <button onClick={handleCreateTask}>Create Task</button>
            <Link to="/projects">
                <button>Return to projects</button>
            </Link>
        </div>
    );
};
export default CreateTask;