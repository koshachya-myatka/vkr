import './App.css'
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { WebSocketProvider } from './websocket/WebsocketProvider';
import LaboratoryDashboard from './pages/LaboratoryDashboard'
import ProductionDashboard from './pages/ProductionDashboard'
import ManagementDashboard from './pages/ManagementDashboard'
import MetalPage from './pages/MetalPage';
import BatchPage from './pages/BatchPage';
import ErrorPage from './pages/ErrorPage';

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <WebSocketProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<Navigate to="/laboratory" />} />

            <Route path="/laboratory" element={<LaboratoryDashboard />} />
            <Route path="/laboratory/metals/:id" element={<MetalPage />} />
            <Route path="/laboratory/batches/:id" element={<BatchPage />} />

            <Route path="/production" element={<ProductionDashboard />} />
            <Route path="/production/metals/:id" element={<MetalPage />} />
            <Route path="/production/batches/:id" element={<BatchPage />} />

            <Route path="/management" element={<ManagementDashboard />} />
            <Route path="/management/metals/:id" element={<MetalPage />} />
            <Route path="/management/batches/:id" element={<BatchPage />} />

            <Route path='/error' element={<ErrorPage />} />
          </Routes>
        </BrowserRouter>
      </WebSocketProvider>
    </QueryClientProvider>
  )
}

export default App;