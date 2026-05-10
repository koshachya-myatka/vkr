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
export const getBatch = (batchId) => API.get(`/batches/${batchId}`);
export const getMesByBatch = (batchId) => API.get(`/mes/${batchId}`);
export const getLimsWithoutResultsByBatch = (batchId) => API.get(`/lims/${batchId}`);

// ЛАБОРАТОРИЯ
export const getLastLims = () => API.get('/laboratory/last-lims');
export const getLimsByBatch = (batchId) => API.get(`/laboratory/lims/${batchId}`);

// ПРОИЗВОДСТВО
export const getLastBatches = () => API.get('/production/last-batches');
export const getScadaByBatch = (batchId) => API.get(`/production/scada/${batchId}`);

// МЕНЕДЖМЕНТ
export const getMetalStatisticsCards = () => API.get('/management/metal-statistics-cards');
export const getScadaAvgByBatch = (batchId) => API.get(`/management/scada/${batchId}`);