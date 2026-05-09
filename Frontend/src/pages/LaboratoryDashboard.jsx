import Header from "../components/general/Header";
import NotificationPanel from "../components/general/NotificationPanel";
import MetalGrid from "../components/dashboardPages/MetalGrid";
import LastLaboratoryRecordTable from "../components/dashboardPages/LastLaboratoryRecordTable";

export default function LaboratoryDashboard() {
    return (        
        <>
            <title>Главная - Лаборатория</title>

            <Header />

            <main className="page-container">
                <div className="page-section">
                    <NotificationPanel />
                </div>
                <div className="page-section">
                    <h1>Лаборатория</h1>
                    <p>
                        Мониторинг анализа металлов,
                        контроль проб и статусов партий.
                    </p>
                </div>

                <div className="page-section">
                    <MetalGrid />
                </div>

                <div className="page-section">
                    <h2>Последние анализы</h2>
                    <LastLaboratoryRecordTable />
                </div>
            </main>
        </>
    );
}