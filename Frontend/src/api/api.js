import axios from 'axios';

const API = axios.create({
    baseURL: 'http://localhost:8081/api'
});

export const getNotifications = () => API.get('/notifications');
export const markNotificationViewed = (id) => API.post(`/notifications/${id}/viewed`);
export const deleteNotification = (id) => API.delete(`/notifications/${id}`);

// ОБЩИЕ
export const getMetalCards = () => API.get('/metal-cards');
export const getMetalBatches = (filter) => API.post('/metals', filter);

// ЛАБОРАТОРИЯ
export const getLastLims = () => API.get('/laboratory/last-lims');

// ПРОИЗВОДСТВО
export const getLastBatches = () => API.get('/production/last-batches');

// МЕНЕДЖМЕНТ