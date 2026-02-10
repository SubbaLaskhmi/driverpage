import React, { useEffect, useState } from 'react';
import {
  ScrollView,
  Text,
  View,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';

const API = 'http://localhost:8080';

interface AuditLog {
  id: number;
  providerName: string;
  action: 'APPROVED' | 'REJECTED' | 'SUSPENDED' | 'REACTIVATED';
  reason: string;
  timestamp: string;
}

export default function AdminAuditLogsScreen() {
  const [logs, setLogs] = useState<AuditLog[]>([]);
  const [loading, setLoading] = useState(true);

  const token =
    typeof window !== 'undefined'
      ? localStorage.getItem('token')
      : null;

  /* ================= LOAD AUDIT LOGS ================= */
  const loadLogs = async () => {
    try {
      const res = await fetch(`${API}/api/admin/audit-logs`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) {
        throw new Error('Failed to load audit logs');
      }

      const data = await res.json();
      setLogs(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!token) return;
    loadLogs();
  }, [token]);

  /* ================= ACTION ICON ================= */
  const iconForAction = (action: string) => {
    switch (action) {
      case 'APPROVED':
        return { name: 'checkmark-circle', color: '#22C55E' };
      case 'REJECTED':
        return { name: 'close-circle', color: '#EF4444' };
      case 'SUSPENDED':
        return { name: 'pause-circle', color: '#F59E0B' };
      case 'REACTIVATED':
        return { name: 'refresh-circle', color: '#6366F1' };
      default:
        return { name: 'information-circle', color: '#9CA3AF' };
    }
  };

  if (loading) {
    return (
      <View className="flex-1 justify-center items-center">
        <Text>Loading audit logs…</Text>
      </View>
    );
  }

  return (
    <ScrollView className="flex-1 px-6 pt-24 pb-32">
      <Text className="text-3xl font-black mb-6">
        Admin Audit Logs
      </Text>

      {logs.length === 0 && (
        <Text className="text-gray-400">
          No audit activity recorded
        </Text>
      )}

      {logs.map((log) => {
        const icon = iconForAction(log.action);

        return (
          <View
            key={log.id}
            className="bg-white rounded-3xl p-6 mb-4 border border-gray-100"
          >
            <View className="flex-row items-center mb-3">
              <Ionicons
                name={icon.name as any}
                size={28}
                color={icon.color}
              />
              <View className="ml-3">
                <Text className="font-black text-lg">
                  {log.providerName}
                </Text>
                <Text className="text-xs text-gray-400">
                  {new Date(log.timestamp).toLocaleString()}
                </Text>
              </View>
            </View>

            <Text className="font-bold mb-1">
              Action: {log.action}
            </Text>

            <Text className="text-gray-500 text-sm">
              {log.reason || 'No reason provided'}
            </Text>
          </View>
        );
      })}
    </ScrollView>
  );
}
