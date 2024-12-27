import React, { useState, useEffect } from 'react';
import axiosInstance from "../utils/axiosInstance";

const Comments = ({ taskId }) => {
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const [editingCommentId, setEditingCommentId] = useState(null);
    const [editingCommentText, setEditingCommentText] = useState('');


    useEffect(() => {
        const fetchComments = async () => {
            try {
                const response = await axiosInstance.get(`/comments/${taskId}`);
                const commentsWithDetails = await Promise.all(response.data.map(async (comment) => {
                    const surnameResponse = await axiosInstance.get(`/comments/title/${comment.id}`);
                    return {
                        ...comment,
                        surname: surnameResponse.data,
                    };
                }));
                setComments(commentsWithDetails);
            } catch (error) {
                console.error('Error fetching comments', error);
            }
        };

        fetchComments();
    }, [taskId]);

    const handleAddComment = async (e) => {
        e.stopPropagation();
        try {
            await axiosInstance.post(`/comments/new/${taskId}`, { commentText: newComment });
            setNewComment('');
            const response = await axiosInstance.get(`/comments/${taskId}`);
            const commentsWithDetails = await Promise.all(response.data.map(async (comment) => {
                const surnameResponse = await axiosInstance.get(`/comments/title/${comment.id}`);
                return {
                    ...comment,
                    surname: surnameResponse.data,
                };
            }));
            setComments(commentsWithDetails);
        } catch (error) {
            console.error('Error adding comment', error);
        }
    };

    const handleDeleteComment = async (commentId, e) => {
        e.stopPropagation();
        try {
            await axiosInstance.delete(`/comments/delete/${commentId}`);
            setComments(comments.filter(comment => comment.id !== commentId));
        } catch (error) {
            console.error('Error deleting comment', error);
        }
    };

    const handleUpdateComment = async (commentId, e, currentCreator) => {
        e.stopPropagation();
        try {
            await axiosInstance.put(`/comments/${commentId}`, { commentText: editingCommentText, createdBy: currentCreator });
            const response = await axiosInstance.get(`/comments/${taskId}`);
            const commentsWithDetails = await Promise.all(response.data.map(async (comment) => {
                const surnameResponse = await axiosInstance.get(`/comments/title/${comment.id}`);
                return {
                    ...comment,
                    surname: surnameResponse.data,
                };
            }));
            setComments(commentsWithDetails);
            setEditingCommentId(null);
            setEditingCommentText('');
        } catch (error) {
            console.error('Error updating comment', error);
        }
    };

    const handleEditClick = (commentId, commentText, e) => {
        e.stopPropagation();
        setEditingCommentId(commentId);
        setEditingCommentText(commentText);
    };

    const handleCancelEdit = (e) => {
        e.stopPropagation();
        setEditingCommentId(null);
        setEditingCommentText('');
    };

    return (
        <div className="comments-section">
            <div className="add-comment">
                <input
                    type="text"
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    onClick={(e) => e.stopPropagation()}
                    placeholder="Add a comment"
                />
                <button onClick={(e) => handleAddComment(e)}>Add</button>
            </div>
            {comments.map(comment => (
                <div key={comment.id} className="comment-item">
                    {editingCommentId === comment.id ? (
                        <>
                            <input
                                type="text"
                                value={editingCommentText}
                                onChange={(e) => setEditingCommentText(e.target.value)}
                                onClick={(e) => e.stopPropagation()}
                            />
                            <button onClick={(e) => handleUpdateComment(comment.id, e, comment.createdBy)}>Save</button>
                            <button onClick={(e) => handleCancelEdit(e)}>Cancel</button>
                        </>
                    ) : (
                        <>
                            <p>{comment.commentText}</p>
                            <div className="comment-meta">
                                <span>Created by: {comment.surname}</span>
                                <span> at {new Date(comment.createdAt).toLocaleString()}</span>
                            </div>
                            <div className="comment-actions">
                                <button onClick={(e) => handleEditClick(comment.id, comment.commentText, e)}>Update</button>
                                <button onClick={(e) => handleDeleteComment(comment.id, e)}>Delete</button>
                            </div>
                        </>
                    )}
                </div>
            ))}
        </div>
    );
};

export default Comments;