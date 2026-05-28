import { useEffect, useRef, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { MoonLoader } from 'react-spinners'
import { connectWebSocket, disconnectWebSocket } from './websocket';
import { optimizeRealtimePoints } from '../components/batchPage/optimizeRealtimePoints';

export const WebSocketProvider = ({ children }) => {
    const queryClient = useQueryClient();
    const [isConnected, setIsConnected] = useState(false);

    useEffect(() => {
        const timeout = setTimeout(() => {
            if (!isConnected) {
                console.error("WS connection timeout");
            }
        }, 10000);

        connectWebSocket((type, msg) => {
            switch (type) {
                case 'notifications':
                    queryClient.setQueryData(['unviewedNotificationsCount'], (old) => { return (old ?? 0) + 1; });
                    window.dispatchEvent(new Event("notifications-new"));
                    queryClient.invalidateQueries({ queryKey: ['notifications-active'] });
                    queryClient.invalidateQueries({ queryKey: ['notifications-stats'] });
                    break;
                case 'mes':
                    queryClient.invalidateQueries({ queryKey: ['dashboard-manag-last-statistics'] });
                    queryClient.invalidateQueries({ queryKey: ['batch-page-batchData', msg.batchId] });
                    queryClient.invalidateQueries({ queryKey: ['dashboard-prod-last-batches'] });
                    queryClient.invalidateQueries({ queryKey: ['batch-page-mes', msg.batchId] });
                    queryClient.invalidateQueries({ queryKey: ['dashboard-metal-grid'] });
                    break;
                case 'lims':
                    queryClient.invalidateQueries({ queryKey: ['batch-page-prod-lims', msg.batchId] });
                    queryClient.invalidateQueries({ queryKey: ['batch-page-lab-lims', msg.batchId] });
                    queryClient.invalidateQueries({ queryKey: ['dashboard-lab-last-lims'] });
                    break;
                case 'scada':
                    const graphKey = ['batch-page-prod-scada', msg.batchId];
                    const existingGraph = queryClient.getQueryData(graphKey);
                    if (!existingGraph) {
                        queryClient.invalidateQueries({ queryKey: graphKey });
                    } else {
                        queryClient.setQueryData(graphKey, (oldData) => {
                            if (!oldData) return oldData;

                            return oldData.map(parameter => {
                                const isCurrent =
                                    parameter.equipmentId === msg.scada.equipmentId &&
                                    parameter.parameter === msg.scada.parameter;
                                if (!isCurrent) {
                                    return parameter;
                                }
                                const exists = parameter.values.some(
                                    v =>
                                        v.time === msg.scada.time &&
                                        v.value === msg.scada.value
                                );
                                if (exists) {
                                    return parameter;
                                }
                                const newValue = {
                                    time: msg.scada.time,
                                    value: msg.scada.value,
                                    unit: msg.scada.unit,
                                    status: msg.scada.status
                                };
                                let values = [...parameter.values, newValue];
                                values = optimizeRealtimePoints(values);
                                return { ...parameter, values };
                            });
                        });
                    }

                    const avgKey = ['batch-page-manag-scada', msg.batchId];
                    const existingAvg = queryClient.getQueryData(avgKey);
                    if (!existingAvg) {
                        queryClient.invalidateQueries({ queryKey: avgKey });
                    } else {
                        queryClient.setQueryData(avgKey, (oldData) => {
                            if (!oldData) return oldData;

                            return oldData.map(parameter => {
                                const isCurrent =
                                    parameter.equipmentId === msg.scada.equipmentId &&
                                    parameter.parameter === msg.scada.parameter;
                                if (!isCurrent) return parameter;
                                const newCount = parameter.valuesCount + 1;
                                const newValue = msg.scada.value;
                                const newAvg = (parameter.avgValue * parameter.valuesCount + newValue) / newCount;
                                return {
                                    ...parameter,
                                    valuesCount: newCount,
                                    avgValue: Math.round(newAvg * 100) / 100,
                                    minValue: Math.min(parameter.minValue, newValue),
                                    maxValue: Math.max(parameter.maxValue, newValue)
                                };
                            });
                        });
                    }
                    break;
            }
        },

            () => {
                setIsConnected(true);
                clearTimeout(timeout);
            },

            () => {
                setIsConnected(false);
            }
        );

        return () => {
            clearTimeout(timeout);
            disconnectWebSocket();
        };

    }, [queryClient]);

    if (!isConnected) {
        return (
            <div className="page-loader">
                <MoonLoader
                    color="#2563eb"
                    loading
                    size={85}
                    speedMultiplier={0.85}
                />
            </div>
        );
    }

    return children;
};