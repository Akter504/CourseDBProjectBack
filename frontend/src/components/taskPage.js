import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axiosInstance from "../utils/axiosInstance";
import CreateTask from './createTask';
import Comments from "./Comments";
import Menu from "./Menu";


const TaskPage = ({ showMenu = true, showTitle = true, showCreateProject = true }) => {
    const [tasks, setTasks] = useState([]);
    const [hoveredTaskId, setHoveredTaskId] = useState(null);
    const [editingTaskId, setEditingTaskId] = useState(null);
    const [taskName, setTaskName] = useState('');
    const [taskDescription, setTaskDescription] = useState('');
    const [taskStatus, setTaskStatus] = useState('');
    const [activeTaskId, setActiveTaskId] = useState(null);
    const [statusFilter, setStatusFilter] = useState('all');
    const { id: projectId } = useParams();
    const [fetchUrl, setFetchUrl] = useState('');

    useEffect(() => {
        if (projectId) {
            setFetchUrl(`/tasks/${projectId}`);
        } else {
            setFetchUrl('/tasks/admin');
        }
    }, [projectId]);

    useEffect(() => {
        if (fetchUrl) {
            fetchTasks(fetchUrl);
        }
    }, [fetchUrl]);

    useEffect(() => {
        updateTaskColors();
    }, [tasks, statusFilter]);

    const fetchTasks = async (url) => {
        try {
            const response = await axiosInstance.get(url);
            const tasks = response.data
            console.log(url);
            setTasks(tasks);
            tasks.forEach(task => {
                const taskElement = document.getElementById(`task-${task.id}`);
                if (taskElement) {
                    taskElement.style.backgroundColor = task.taskStatus.color;
                }
            });
        } catch (error) {
            console.error('Error fetching tasks', error);
        }
    };

    const updateTaskColors = () => {
        tasks.forEach(task => {
            const taskElement = document.getElementById(`task-${task.id}`);
            if (taskElement && task.taskStatus) {
                taskElement.style.backgroundColor = task.taskStatus.color;
            }
        });
    };

    const handleMouseEnter = (taskId) => {
        setHoveredTaskId(taskId);
    };

    const handleMouseLeave = () => {
        setHoveredTaskId(null);
    };

    const handleDeleteTask = async (taskId) => {
        try {
            await axiosInstance.delete(`/tasks/delete/${taskId}`);
            fetchTasks(fetchUrl);
        } catch (error) {
            console.error('Error deleting task', error);
        }
    };

    const handleEditTask = (task) => {
        if (task && task.taskStatus) {
            setEditingTaskId(task.id);
            setTaskName(task.nameTask);
            setTaskDescription(task.description);
            setTaskStatus(task.taskStatus.name);
        } else {
            console.error('Task is null or undefined');
        }
    };

    const handleUpdateTask = async (taskId, taskName, taskDescription, taskStatus) => {
        console.log('Updating task:', { taskId, taskName, taskDescription });
    try {
        console.log('Task status:', taskStatus);
        const response = await axiosInstance.put(`/tasks/update/${taskId}/${taskStatus}`,
            {nameTask: taskName, description: taskDescription});
        const updatedTask = response.data;
        setTasks(tasks.map(task => task.id === taskId ? updatedTask : task));
        setEditingTaskId(null);
        document.getElementById(`task-${taskId}`).style.backgroundColor = updatedTask.color;
        fetchTasks(fetchUrl);
        } catch (error) {
            console.error('Error updating task', error);
        }

    }

    const handleCancelEdit = () => {
        setEditingTaskId(null);
        setTaskName('');
        setTaskDescription('');
        setTaskStatus('');
    };

    const toggleComments = (taskId) => {
        setActiveTaskId(activeTaskId === taskId ? null : taskId);
    };

    const filteredTasks = tasks.filter(task => {
        if (statusFilter === 'all') return true;
        return task.taskStatus && task.taskStatus.name === statusFilter;
    });

    const handleStatusFilterChange = (e) => {
        setStatusFilter(e.target.value);
    };


    return (
        <div className="container">
            {showMenu && <Menu />}
            {showTitle && <h2>Project Management</h2>}
            {showCreateProject && <CreateTask projectId={projectId} />}
            <div className="filter-section">
                <label htmlFor="statusFilter">Filter by status:</label>
                <select id="statusFilter" value={statusFilter} onChange={handleStatusFilterChange}>
                    <option value="all">All</option>
                    <option value="TO_DO">To Do</option>
                    <option value="IN_PROCESS">In Progress</option>
                    <option value="COMPLETED">Done</option>
                    <option value="CANCELLED">Cancelled</option>
                </select>
            </div>
            <h2>Task List</h2>
            <ul>
                {filteredTasks.map((task) => (
                    <li
                        key={task.id}
                        id={`task-${task.id}`}
                        onMouseEnter={() => handleMouseEnter(task.id)}
                        onMouseLeave={handleMouseLeave}
                        className="task-item"
                        onClick={() => toggleComments(task.id)}
                    >
                        {editingTaskId === task.id ? (
                            <div>
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
                                <select
                                    value={taskStatus}
                                    onChange={(e) => setTaskStatus(e.target.value)}
                                >
                                    <option value="TO_DO">To Do</option>
                                    <option value="IN_PROCESS">In Progress</option>
                                    <option value="COMPLETED">Done</option>
                                    <option value="CANCELLED">Cancelled</option>
                                </select>
                                <button
                                    onClick={() => handleUpdateTask(task.id, taskName, taskDescription, taskStatus)}>Save
                                </button>
                                <button onClick={handleCancelEdit}>Cancel</button>
                            </div>
                        ) : (
                            <div>
                                {task.nameTask}: {task.description} ({task.taskStatus ? task.taskStatus.name : 'No Status'})
                                {hoveredTaskId === task.id && (
                                    <div className="task-menu">
                                        <button onClick={() => handleDeleteTask(task.id)}>Delete</button>
                                        <button onClick={() => handleEditTask(task)}>Edit</button>
                                    </div>
                                )}
                                {activeTaskId === task.id && (
                                    <Comments taskId={task.id} />
                                )}
                            </div>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default TaskPage;