import './App.css'
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { WebSocketProvider } from './websocket/WebsocketProvider';
import ProtectedRoute from './components/security/ProtectedRoute';
import RoleRedirect from './components/security/RoleRedirect';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ErrorPage from './pages/ErrorPage';
import AdminPage from './pages/AdminPage';
import LaboratoryDashboard from './pages/LaboratoryDashboard'
import ProductionDashboard from './pages/ProductionDashboard'
import ManagementDashboard from './pages/ManagementDashboard'
import MetalPage from './pages/MetalPage';
import BatchPage from './pages/BatchPage';

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <WebSocketProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<RoleRedirect />} />
            <Route path="/login"
              element={
                localStorage.getItem('token')
                  ? <RoleRedirect />
                  : <LoginPage />
              }
            />
            <Route path="/register" element={<RegisterPage />} />
            <Route path='/error' element={<ErrorPage message={null} />} />

            <Route path="/admin"
              element={
                <ProtectedRoute roles={['ADMIN']}>
                  <AdminPage />
                </ProtectedRoute>
              }
            />

            <Route path="/laboratory"
              element={
                <ProtectedRoute roles={['LABORATORY', 'MANAGEMENT', 'ADMIN']}>
                  <LaboratoryDashboard />
                </ProtectedRoute>
              }
            />
            <Route path="/laboratory/metals/:id"
              element={
                <ProtectedRoute roles={['LABORATORY', 'MANAGEMENT', 'ADMIN']}>
                  <MetalPage />
                </ProtectedRoute>
              }
            />
            <Route path="/laboratory/batches/:id"
              element={
                <ProtectedRoute roles={['LABORATORY', 'MANAGEMENT', 'ADMIN']}>
                  <BatchPage />
                </ProtectedRoute>
              }
            />

            <Route path="/production"
              element={
                <ProtectedRoute roles={['PRODUCTION', 'MANAGEMENT', 'ADMIN']}>
                  <ProductionDashboard />
                </ProtectedRoute>
              }
            />
            <Route path="/production/metals/:id"
              element={
                <ProtectedRoute roles={['PRODUCTION', 'MANAGEMENT', 'ADMIN']}>
                  <MetalPage />
                </ProtectedRoute>
              }
            />
            <Route path="/production/batches/:id"
              element={
                <ProtectedRoute roles={['PRODUCTION', 'MANAGEMENT', 'ADMIN']}>
                  <BatchPage />
                </ProtectedRoute>
              }
            />

            <Route path="/management"
              element={
                <ProtectedRoute roles={['MANAGEMENT', 'ADMIN']}>
                  <ManagementDashboard />
                </ProtectedRoute>
              }
            />
            <Route path="/management/metals/:id"
              element={
                <ProtectedRoute roles={['MANAGEMENT', 'ADMIN']}>
                  <MetalPage />
                </ProtectedRoute>
              }
            />
            <Route path="/management/batches/:id"
              element={
                <ProtectedRoute roles={['MANAGEMENT', 'ADMIN']}>
                  <BatchPage />
                </ProtectedRoute>
              }
            />
          </Routes>
        </BrowserRouter>
      </WebSocketProvider>
    </QueryClientProvider>
  )
}

export default App;