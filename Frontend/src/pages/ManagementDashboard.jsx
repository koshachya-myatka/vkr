import Header from "../components/general/Header";
import NotificationPanel from "../components/general/NotificationPanel";
import MetalGrid from "../components/dashboardPages/MetalGrid";
import LastMetalStatisticsGrid from "../components/dashboardPages/LastMetalStatisticsGrid";

export default function ManagementDashboard() {
    return (
        <div>
            <title>Главная - Руководство</title>

            <Header />

            <NotificationPanel />
            
            <MetalGrid />

            <LastMetalStatisticsGrid />
        </div>
    )
}