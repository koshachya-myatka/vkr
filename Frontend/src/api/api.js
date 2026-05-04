import axios from 'axios';

const API = axios.create({
    baseURL: 'http://localhost:8081/api'
});

export const getNotifications = () => API.get('/notifications');
export const markNotificationViewed = (id) => API.post(`/notifications/${id}/viewed`);
export const deleteNotification = (id) => API.delete(`/notifications/${id}`);

export const getMetalCards = () => API.get('/metal-cards');

export const getLastLims = () => API.get('/laboratory/last-lims');

export const getLastBatches = () => API.get('/production/last-batches');