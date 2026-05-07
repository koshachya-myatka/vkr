import Header from "../components/general/Header";
import NotificationPanel from "../components/general/NotificationPanel";
import MetalGrid from "../components/dashboardPages/MetalGrid";
import LastLaboratoryRecordTable from "../components/dashboardPages/LastLaboratoryRecordTable";

export default function LaboratoryDashboard() {
    return (
        <div>
            <title>Главная - Лаборатория</title>

            <Header />

            <NotificationPanel />

            <MetalGrid />

            <LastLaboratoryRecordTable />
        </div>
    );
}