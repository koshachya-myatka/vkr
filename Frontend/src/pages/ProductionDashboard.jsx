import Header from "../components/Header";
import NotificationPanel from "../components/NotificationPanel";
import MetalGrid from "../components/MetalGrid";
import LastProductionRecordTable from "../components/LastProductionRecordTable";

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