import axios from 'axios';

const API = axios.create({
    baseURL: 'http://localhost:8081/api'
});

API.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization =
            `Bearer ${token}`;
    }
    return config;
});

// АВТОРИЗАЦИЯ
export const loginUser = (data) => API.post('/auth/login', data);
export const registerUser = (data) => API.post('/auth/register', data);

// УВЕДОМЛЕНИЯ
export const getActiveNotifications = () => API.get('/notifications/active');
export const markNotificationInProgress = (id) => API.post(`/notifications/${id}/in-progress`);
export const getAllNotifications = (dto) => API.post('/notifications', dto);
export const updateNotification = (id, dto) => API.put(`/notifications/${id}`, dto);
export const getNotificationsStats = () => API.get('/notifications/stats');

// АДМИН
export const getUsers = (filter) => API.post('/admin/users', filter);
export const updateUser = (id, data) => API.put(`/admin/users/${id}`, data);

// ОБЩИЕ
export const getMetalCards = () => API.get('/metal-cards');
export const getMetalBatches = (filter) => API.post('/metals', filter);
export const getBatch = (batchId) => API.get(`/batches/${batchId}`);
export const getMesByBatch = (batchId) => API.get(`/mes/${batchId}`);
export const getLimsWithoutResultsByBatch = (batchId) => API.get(`/lims/${batchId}`);
export const getCurrentUser = () => API.get('/users/me');
export const getScadaAvgByBatch = (batchId) => API.get(`/scada/${batchId}`);
export const createReport = (batchId) => API.get(`/report/${batchId}`, { responseType: 'blob' });

// ЛАБОРАТОРИЯ
export const getLastLims = () => API.get('/laboratory/last-lims');
export const getLimsByBatch = (batchId) => API.get(`/laboratory/lims/${batchId}`);

// ПРОИЗВОДСТВО
export const getLastBatches = () => API.get('/production/last-batches');
export const getScadaByBatch = (batchId) => API.get(`/production/scada/${batchId}`);

// МЕНЕДЖМЕНТ
export const getMetalStatisticsCards = () => API.get('/management/metal-statistics-cards');