import Header from "../components/Header";
import MetalBatchesTable from "../components/MetalBatchesTable";
import { useParams, Link } from "react-router-dom";

export default function MetalPage() {
    const { id } = useParams();

    return (
        <div>
            <Header />
            <MetalBatchesTable metalType={id} />
        </div>
    )
}