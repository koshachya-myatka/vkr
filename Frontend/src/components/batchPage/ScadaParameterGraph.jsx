import { ResponsiveContainer, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Scatter } from 'recharts';

const STATUS_COLORS = {
    NORMAL: '#22c55e',
    WARNING: '#eab308',
    ALARM: '#ef4444'
};

export default function ScadaParameterGraph({ parameter }) {
    const data = parameter.values.map(item => ({
        ...item,
        timestamp: new Date(item.time).toLocaleTimeString(),
        color: STATUS_COLORS[item.status]
    }));

    return (
        <div className="flex-column gap-md">
            <div className="flex-between">
                <h3>
                    {parameter.parameter}, {parameter.unit}
                </h3>
                <span className="badge badge-secondary">
                    {parameter.equipmentId}
                </span>
            </div>

            <div style={{ width: '100%', height: 320, minWidth: 0}}>
                <ResponsiveContainer>
                    <LineChart data={data}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis
                            dataKey="timestamp"
                            tick={{ fontSize: 12 }}
                        />
                        <YAxis
                            dataKey="value"
                            tick={{ fontSize: 12 }}
                        />
                        <Tooltip />
                        <Line
                            type="monotone"
                            dataKey="value"
                            stroke="#3b82f6"
                            strokeWidth={2}
                            dot={(props) => {
                                const { cx, cy, payload } = props;
                                return (
                                    <circle
                                        cx={cx}
                                        cy={cy}
                                        r={5}
                                        fill={payload.color}
                                        stroke="#fff"
                                        strokeWidth={1}
                                    />
                                );
                            }}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
}