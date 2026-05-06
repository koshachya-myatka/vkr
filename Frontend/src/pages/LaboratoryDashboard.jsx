import Header from "../components/Header";
import NotificationPanel from "../components/NotificationPanel";
import MetalGrid from "../components/MetalGrid";
import LastLaboratoryRecordTable from "../components/LastLaboratoryRecordTable";

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