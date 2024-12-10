import React, { useState } from 'react';
import axios from 'axios';
import axiosInstance from "../utils/axiosInstance";

const CreateTask = ({ projectId }) => {
    const [taskName, setTaskName] = useState('');
    const [taskDescription, setTaskDescription] = useState('');

    const handleCreateTask = async() => {
        try {
            const response = await axiosInstance.post(`/tasks/new/${projectId}`, {
                nameTask: taskName,
                description: taskDescription,
            });
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
        </div>
    );
};
export default CreateTask;