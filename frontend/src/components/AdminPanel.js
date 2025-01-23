import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {jwtDecode} from 'jwt-decode';
import axiosInstance from "../utils/axiosInstance";
import TaskPage from './taskPage';
import ProjectPage from './projectPage';
import AdminBackupRestore  from "./AdminBackupRestore";

const AdminPanel = () => {
    const [isAdmin, setIsAdmin] = useState(false);
    const [showUsers, setShowUsers] = useState(false);
    const [showTasks, setShowTasks] = useState(false);
    const [showProjects, setShowProjects] = useState(false);
    const [users, setUsers] = useState([]);
    const [roles, setRoles] = useState([]);
    const [editingUser, setEditingUser] = useState(null);
    const [tasks, setTasks] = useState([]);
    const [projects, setProjects] = useState([]);
    const [formData, setFormData] = useState({ userName: '', surnameUser: '', email: '', phoneNumber: '', role: '' });
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            const decodedToken = jwtDecode(token);
            if (Array.isArray(decodedToken.roles) && decodedToken.roles.includes('ADMIN')) {
                setIsAdmin(true);
            } else {
                navigate('/');
            }
        } else {
            navigate('/login');
        }
    }, [navigate]);

    const fetchUsers = async () => {
        const response = await axiosInstance.get('/users/admin');
        const data = await response.data;
        setUsers(data.users);
        setRoles(data.roles);
    };

    const fetchTasks = async () => {
        const response = await axiosInstance.get('/tasks/admin');
        const data = await response.data;
        setTasks(data);
    };

    const fetchProjects = async () => {
        const response = await axiosInstance.get('/projects/admin');
        const data = await response.data;
        setProjects(data);
    };

    const toggleUsers = async () => {
        setShowUsers(!showUsers);
        if (!showUsers) {
            await fetchUsers();
        } else {
            setEditingUser(null);
        }
    };

    const toggleTasks = async () => {
        setShowTasks(!showTasks);
        if (!showTasks) {
            await fetchTasks();
        }
    };

    const toggleProjects = async () => {
        setShowProjects(!showProjects);
        if (!showProjects) {
            await fetchProjects();
        }
    };

    const handleEdit = (user, role) => {
        setEditingUser(user);
        setFormData({ userName: user.userName, surnameUser: user.surnameUser, email: user.email, phoneNumber: user.phoneNumber, role: role });
    };

    const handleUpdate = async (e) => {
        e.preventDefault();
        try {
            const roleName = formData.role.nameSystemRole;
            await axiosInstance.put(`/users/${editingUser.id}/${roleName}`, formData);
            await fetchUsers();
            setEditingUser(null);
        } catch (error) {
            console.error('Failed to update user:', error);
        }
    };

    const handleDelete = async (userId) => {
        try {
            await axiosInstance.delete(`/users/delete/${userId}`);
            await fetchUsers();
        } catch (error) {
            console.error('Failed to delete user:', error);
        }
    };

    if (!isAdmin) {
        return null; // or a loading spinner
    }

    return (
        <div>
            <h1>Admin Panel</h1>
            <AdminBackupRestore />
            <div>
                <button onClick={toggleUsers}>
                    {showUsers ? 'Hide Users' : 'Show Users'}
                </button>
                {showUsers && (
                    <ul>
                        {users.map((user, index) => (
                            <li key={user.id} style={{ position: 'relative' }}>
                                {user.userName}, {user.surnameUser}, {user.email}, {user.phoneNumber}, {roles[index].nameSystemRole}
                                <div style={{ position: 'absolute', right: 0, top: 0 }}>
                                    <button onClick={() => handleEdit(user, roles[index])}>Edit</button>
                                    <button onClick={() => handleDelete(user.id)}>Delete</button>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
            <div>
                <button onClick={toggleTasks}>
                    {showTasks ? 'Hide Tasks' : 'Show Tasks'}
                </button>
                {showTasks && <TaskPage showTitle={false} showCreateProject={false} showMenu={false} fetchUrl={'/tasks/admin'}/>}
            </div>
            <div>
                <button onClick={toggleProjects}>
                    {showProjects ? 'Hide Projects' : 'Show Projects'}
                </button>
                {showProjects && <ProjectPage showTitle={false} showCreateProject={false} showMenu={false} fetchUrl={'/projects/admin'}/>}
            </div>
            {editingUser && (
                <form onSubmit={handleUpdate}>
                    <h2>Edit User</h2>
                    <label>
                        Username:
                        <input
                            type="text"
                            value={formData.userName}
                            onChange={(e) => setFormData({ ...formData, userName: e.target.value })}
                        />
                    </label>
                    <label>
                        Surname:
                        <input
                            type="text"
                            value={formData.surnameUser}
                            onChange={(e) => setFormData({ ...formData, surnameUser: e.target.value })}
                        />
                    </label>
                    <label>
                        Phone Number:
                        <input
                            type="text"
                            value={formData.phoneNumber}
                            onChange={(e) => setFormData({ ...formData, phoneNumber: e.target.value })}
                        />
                    </label>
                    <label>
                        Role:
                        <input
                            type="text"
                            value={formData.role.nameSystemRole}
                            onChange={(e) => setFormData({ ...formData, role: { ...formData.role, nameSystemRole: e.target.value } })}
                        />
                    </label>
                    <button type="submit">Update</button>
                    <button type="button" onClick={() => setEditingUser(null)}>Cancel</button>
                </form>
            )}
        </div>
    );
};

export default AdminPanel;