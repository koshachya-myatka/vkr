import { useState } from "react";

const statuses = ["ARRIVAL", "PROCESSING", "ANALYSIS", "ACCEPTED", "DEFECTIVE"];

export default function SearchPanel({ onSearch }) {
    const [batchId, setBatchId] = useState("");
    const [startTime, setStartTime] = useState("");
    const [endTime, setEndTime] = useState("");
    const [processStatus, setProcessStatus] = useState("");

    const handleSubmit = () => {
        onSearch({
            batchId: batchId || null,
            startTime: startTime || null,
            endTime: endTime || null,
            processStatus: processStatus || null,
        });
    };

    const resetFilter = () => {
        setBatchId("");
        setStartTime("");
        setEndTime("");
        setProcessStatus("");
        onSearch({
            batchId: null,
            startTime: null,
            endTime: null,
            processStatus: null,
        });        
    };

    return (
        <div style={{ display: "flex", gap: "10px", marginBottom: "20px" }}>
            <button onClick={resetFilter}>Сброс</button>
            
            <input
                placeholder="Batch ID"
                value={batchId}
                onChange={(e) => setBatchId(e.target.value)}
            />

            <input
                type="datetime-local"
                value={startTime}
                onChange={(e) => setStartTime(e.target.value)}
            />

            <input
                type="datetime-local"
                value={endTime}
                onChange={(e) => setEndTime(e.target.value)}
            />

            <select
                value={processStatus}
                onChange={(e) => setProcessStatus(e.target.value)}
            >
                <option value="">All statuses</option>
                {statuses.map((s) => (
                    <option key={s} value={s}>
                        {s}
                    </option>
                ))}
            </select>

            <button onClick={handleSubmit}>Search</button>
        </div>
    );
}