import Header from "../components/Header";
import NotificationPanel from "../components/NotificationPanel";
import MetalGrid from "../components/MetalGrid";
import LastMetalStatisticsGrid from "../components/LastMetalStatisticsGrid";

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