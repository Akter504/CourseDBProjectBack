import React, { useEffect, useState } from 'react';
import axiosInstance from '../utils/axiosInstance';
import Menu from './Menu';

const UserProfile = () => {
    const [user, setUser] = useState(null);
    const [editing, setEditing] = useState(false);
    const [formData, setFormData] = useState({
        userName: '',
        surnameUser: '',
        phoneNumber: ''
    });

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axiosInstance.get('/users/profile');
                setUser(response.data);
                setFormData({
                    userName: response.data.userName,
                    surnameUser: response.data.surnameUser,
                    phoneNumber: response.data.phoneNumber
                });
            } catch (error) {
                console.error('Error fetching user data', error);
            }
        };

        fetchUserData();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        try {
            await axiosInstance.put(`/users/${user.id}`, formData);
            setUser({ ...user, ...formData });
            setEditing(false);
        } catch (error) {
            console.error('Error updating user data', error);
        }
    };

    if (!user) {
        return <div>Loading...</div>;
    }

    return (
        <div className="profile-container">
            <Menu />
            <h1 className="profile-title">User Profile</h1>
            {editing ? (
                <form onSubmit={handleFormSubmit}>
                    <div>
                        <label>Username:</label>
                        <input
                            type="text"
                            name="userName"
                            value={formData.userName}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div>
                        <label>Surname:</label>
                        <input
                            type="text"
                            name="surnameUser"
                            value={formData.surnameUser}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div>
                        <label>Phone Number:</label>
                        <input
                            type="text"
                            name="phoneNumber"
                            value={formData.phoneNumber}
                            onChange={handleInputChange}
                        />
                    </div>
                    <button type="submit">Save</button>
                    <button type="button" onClick={() => setEditing(false)}>Cancel</button>
                </form>
            ) : (
                <div className="profile-details">
                    <p><strong>Username:</strong> {user.userName}</p>
                    <p><strong>Surname:</strong> {user.surnameUser}</p>
                    <p><strong>Phone Number:</strong> {user.phoneNumber}</p>
                    <button onClick={() => setEditing(true)}>Edit</button>
                </div>
            )}
        </div>
    );
};


export default UserProfile;