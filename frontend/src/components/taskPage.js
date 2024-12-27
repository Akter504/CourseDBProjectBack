import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axiosInstance from "../utils/axiosInstance";
import CreateTask from './createTask';

const TaskPage = () => {
    const [tasks, setTasks] = useState([]);
    const { id: projectId } = useParams();

    useEffect(() => {
        fetchTasks();
    }, []);

    const fetchTasks = async () => {
        try {
            const response = await axiosInstance.get(`/tasks/${projectId}`);
            setTasks(response.data);
        } catch (error) {
            console.error('Error fetching tasks', error);
        }
    };

    return (
        <div>
            <h2>Task Management</h2>
            <CreateTask projectId={projectId} onTaskCreated={fetchTasks} />
            <h2>Task List</h2>
            <ul>
                {tasks.map((task) => (
                    <li key={task.id}>{task.nameTask}: {task.description}</li>
                ))}
            </ul>
        </div>
    );
};

export default TaskPage;