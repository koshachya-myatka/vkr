import './App.css'
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import LaboratoryDashboard from './pages/LaboratoryDashboard'
import ProductionDashboard from './pages/ProductionDashboard'
import ManagementDashboard from './pages/ManagementDashboard'
import MetalPage from './pages/MetalPage';
import BatchPage from './pages/BatchPage';

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
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
        </Routes>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

export default App;