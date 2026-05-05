import './App.css'
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LaboratoryDashboard from './pages/LaboratoryDashboard'
import ProductionDashboard from './pages/ProductionDashboard'
import ManagementDashboard from './pages/ManagementDashboard'
import MetalPage from './components/MetalBatchesTable';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/laboratory" />} />

        <Route path="/laboratory" element={<LaboratoryDashboard />} />
        <Route path="/laboratory/metals/:id" element={<MetalPage />} />

        <Route path="/production" element={<ProductionDashboard />} />
        <Route path="/production/metals/:id" element={<MetalPage />} />

        <Route path="/management" element={<ManagementDashboard />} />
        <Route path="/management/metals/:id" element={<MetalPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App;