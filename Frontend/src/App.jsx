import './App.css'
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LaboratoryDashboard from './pages/LaboratoryDashboard'
import ProductionDashboard from './pages/ProductionDashboard'
import ManagementDashboard from './pages/ManagementDashboard'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/laboratory" />} />
        <Route path="/laboratory" element={<LaboratoryDashboard />} />
        <Route path="/production" element={<ProductionDashboard />} />
        <Route path="/management" element={<ManagementDashboard />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App;