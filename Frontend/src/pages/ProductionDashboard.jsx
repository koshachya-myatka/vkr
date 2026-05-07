import Header from "../components/general/Header";
import NotificationPanel from "../components/general/NotificationPanel";
import MetalGrid from "../components/dashboardPages/MetalGrid";
import LastProductionRecordTable from "../components/dashboardPages/LastProductionRecordTable";

export default function ProductionDashboard() {
  return (
    <div>
      <title>Главная - Производство</title>
      <Header />
      <NotificationPanel />
      <MetalGrid />
      <LastProductionRecordTable />
    </div>
  );
}